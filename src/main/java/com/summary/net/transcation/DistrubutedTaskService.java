package com.summary.net.transcation;

import com.google.common.base.Splitter;
import com.google.common.collect.Lists;
import com.summary.net.transcation.config.PluginChain;
import com.summary.net.transcation.config.TranscationIntercetor;
import com.summary.net.transcation.constants.Constants;
import com.summary.net.transcation.dao.TranscationResposity;
import com.summary.net.transcation.model.TranscationDTO;
import com.summary.net.transcation.utils.JacksonUtils;
import com.summary.net.transcation.utils.SpringContextUtil;
import com.summary.net.transcation.utils.ThreadLocalUtils;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.ObjectUtils;

/**
 * @author xiao liang
 * @version V1.0
 * @Title: DistrubutedTaskPool
 * @Description: 定时任务做扫描
 * @date 2020/11/16 12:07
 */
@Slf4j
public class DistrubutedTaskService implements Runnable {

  private TranscationResposity resposity;

  private PluginChain pluginChain;

  private TranscationDTO transcationDTO;


  public DistrubutedTaskService(TranscationDTO transcationDTO,
      PluginChain pluginChain, TranscationResposity resposity) {
    this.transcationDTO = transcationDTO;
    this.pluginChain = pluginChain;
    this.resposity = resposity;
  }


  private List<String> properties = Lists
      .newArrayList("java.lang.String", "java.lang.Byte", "java.lang.Short",
          "java.lang.Integer", "java.lang.Long", "java.lang.Float", "java.lang.Double",
          "java.lang.Character");



  private void retry(TranscationDTO transcationEntity) {
    Exception excep = null;
    try {
      String beanName = transcationEntity.getBeanName();
      if (ObjectUtils.isEmpty(beanName)) {
        log.error("未找到bean对象, transcationEntity = {}", transcationEntity);
      }
      Method method = getMethod(transcationEntity);

      for (TranscationIntercetor transcationIntercetor : pluginChain.getAll()) {
        if (transcationIntercetor.isGlobal()) {
          transcationIntercetor.postHandle(transcationEntity);
        }
      }
      method.invoke(SpringContextUtil.getBean(beanName), getParamters(transcationEntity));

      resposity.retrySuccess(transcationEntity);
    } catch (Exception e) {
      excep = e;
      transcationEntity.setNextFailReason(
          Arrays.stream(e.getStackTrace()).map(StackTraceElement::toString).collect(
              Collectors.joining(",")));
      resposity.retryFail(transcationEntity);
      log.error("重试异常", e);
      e.printStackTrace();

    } finally {
      for (TranscationIntercetor transcationIntercetor : pluginChain.getAll()) {
        if (transcationIntercetor.isGlobal()) {
          transcationIntercetor.afterCompletetion(transcationEntity, excep);
        }
      }
    }
  }

  private Method getMethod(TranscationDTO transcationEntity) throws Exception {
    return Class.forName(transcationEntity.getClassName())
        .getMethod(transcationEntity.getMethodName(), getParamterTypes(transcationEntity));
  }


  private Object[] getParamters(TranscationDTO transcationEntity) throws Exception {
    //获取class类
    Class[] paramterTypes = getParamterTypes(transcationEntity);
    String paramContents = transcationEntity.getParamContents();
    List<String> contents = autoSpilit(paramContents);
    Object[] parameterParams = new Object[paramterTypes.length];
    for (int i = 0; i < paramterTypes.length; i++) {
      parameterParams[i] = JacksonUtils.json2pojo(contents.get(i), paramterTypes[i]);
    }
    return parameterParams;
  }

  private Object getProperty(String s, Class paramterType) {
    for (String property : properties) {
      if (property.equals(paramterType.getTypeName())) {
        return s;
      }
    }
    return new Object();
  }


  private Class[] getParamterTypes(TranscationDTO transcationEntity) throws Exception {
    List<String> classStrList = autoSpilit(transcationEntity.getParamClass());
    Class[] classes = new Class[classStrList.size()];
    for (int i = 0; i < classStrList.size(); i++) {
      classes[i] = Class.forName(classStrList.get(i));

    }
    return classes;
  }


  private static List<String> autoSpilit(String str) {
    return Splitter.on(Constants.SPILIT).omitEmptyStrings().trimResults()
        .splitToList(str);
  }


  @Override
  public void run() {
    try {
      ThreadLocalUtils.setResume();
      retry(this.transcationDTO);
    } catch (Exception e) {
      log.error("事务重试任务执行失败", e);
    } finally {
      ThreadLocalUtils.remove();
    }
  }


}
