package com.summary.net.transcation.config;

import com.summary.net.transcation.model.TranscationDTO;
import com.summary.net.transcation.dao.TranscationResposity;
import com.summary.net.transcation.service.idservice.GenerateIDSequenceService;
import com.summary.net.transcation.utils.JacksonUtils;
import com.summary.net.transcation.utils.SpringContextUtil;
import com.summary.net.transcation.utils.ThreadLocalUtils;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.stream.Collectors;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @author xiao liang
 * @version V1.0
 * @Package com.picc.cms.common.distributed.transaction
 * @Title: DistributedTranscationAspect
 * @Description:
 * @date 2020/11/16 12:00
 */
@Aspect
@Slf4j
@Component
public class DistributedTranscationAspect {

  @Autowired
  private TranscationResposity resposity;

  @Autowired
  private GenerateIDSequenceService generateIDSequenceService;

  @Autowired
  private PluginChain pluginChain;


  private static final StringBuilder START = new StringBuilder("(1)");


  @Around(value = "@annotation(com.summary.net.transcation.config.DistributedTranscation)")
  public Object invoke(ProceedingJoinPoint point) throws Throwable {
    try {
      //如果是恢复线程，退出切面
      return point.proceed(point.getArgs());
    } catch (Throwable throwable) {
      //如果是事务恢复线程，退出切面
      if (ThreadLocalUtils.isResume()) {
        throw throwable;
      }
      try {
        //立即重试一次
        return point.proceed(point.getArgs());
      } catch (Throwable e) {
        TranscationDTO transcationDTO = TranscationDTO.builder()
            .id(generateIDSequenceService.generateSequenceByRedis("transcation"))
            .className(getClassName(point))
            .methodName(getMethodName(point))
            .paramClass(getParamTypeList(point))
            .paramContents(getParamContentList(point))
            .failReason(START.append(getFailReason(e)).toString())
            .retryCount(1)
            .insertTimeForHis(new Date())
            .validStatus("0")
            .threadId(Thread.currentThread().getId())
            .beanName(getBeanName(point)).build();
        log.error("方法异常, {}", e);

        for (TranscationIntercetor transcationIntercetor : pluginChain.getAll()) {
          transcationDTO =   transcationIntercetor.preSave(transcationDTO);
        }

        resposity.save(transcationDTO);
        //重试失败后,保存失败信息
        //保存异常信息
        throw new RuntimeException(throwable);
      }
    }
  }


  private String getFailReason(Throwable e) {
    if(e == null || e.getMessage() == null) {
      return "";
    }
    return e.getMessage().length() > 990 ? e.getMessage().substring(0, 990)
        : e.getMessage();
  }

  private String getBeanName(ProceedingJoinPoint point) {
    String[] beanNames = SpringContextUtil.getApplicationContext()
        .getBeanNamesForType(point.getTarget().getClass());
    if (beanNames == null && beanNames.length <= 1) {
      throw new IllegalArgumentException(
          "无法获取bean实例 class is " + point.getTarget().getClass().getName());
    }
    return beanNames[0];
  }

  private String getParamTypeList(ProceedingJoinPoint point) {
    return Arrays.stream(point.getArgs()).map(e -> e.getClass().getTypeName())
        .collect(Collectors.joining("||"));
  }

  private String getParamContentList(ProceedingJoinPoint point) {
    return Arrays.stream(point.getArgs()).map(this::toJson).collect(Collectors.joining("||"));
  }

  private String getClassName(ProceedingJoinPoint point) {
    return point.getTarget().getClass().getName();
  }


  private String getMethodName(ProceedingJoinPoint point) {
    return point.getSignature().getName();
  }

  private String toParamContentsJson(ProceedingJoinPoint point) {
//    Object[] args = point.getArgs();
//    StringBuilder paramContentBuilder = new StringBuilder();
//    for (int i = 0; i < args.length; i++) {
//      try {
//        paramContentBuilder.append(JacksonUtils.obj2json(args[i]));
//        if (i < args.length - 1) {
//          paramContentBuilder.append("||");
//        }
//      } catch (Exception e) {
//        log.error("json 序列化出错", e);
//      }
//    }
    return Arrays.stream(point.getArgs()).map(this::toJson).collect(Collectors.joining("||"));
  }

  private String toJson(Object obj) {
    try {
      return JacksonUtils.obj2json(obj);
    } catch (Exception e) {
      log.error("json 序列化出错", e);
      throw new IllegalArgumentException("参数非法, " + e);
    }

  }

  private static <E> ArrayList<E> newArrayList(E... elements) {
    if (elements == null) {
      throw new NullPointerException();
    }
    ArrayList<E> list = new ArrayList<>(elements.length + 1);
    Collections.addAll(list, elements);
    return list;
  }


}
