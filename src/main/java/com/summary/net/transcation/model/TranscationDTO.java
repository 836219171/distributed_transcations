package com.summary.net.transcation.model;

import com.summary.net.transcation.utils.JacksonUtils;
import java.io.Serializable;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.Assert;
import org.springframework.util.StringUtils;

/**
 * @author xiao liang
 * @version V1.0
 * @Package com.picc.cms.common.distributed.transaction
 * @Title: TranscationEntityDTO
 * @Description:
 * @date 2020/11/16 11:57
 */
@Data
@AllArgsConstructor
@Builder
@Slf4j
public class TranscationDTO implements Serializable {

  private static final long serialVersionUID = -4784621516426302130L;

  private String id;

  private String className;

  private String methodName;

  private String beanName;

  private String paramContents;

  private String paramClass;

  private String failReason;

  private Integer retryCount;

  private Date lastRetryTime;

  private String validStatus;

  private Date insertTimeForHis;

  private Date operateTimeForHis;

  private Long threadId;

  private String extraData;

  private Map<String, Object> threadLocalMap;


  public Object[] getSnapshop() {
    reset();
    return new Object[]{getId(), getClassName(), getMethodName(), getBeanName(),
        getFailReason(), getRetryCount(), getLastRetryTime(), getInsertTimeForHis(),
        getOperateTimeForHis(), getParamContents(), getParamClass(), getValidStatus(),
        getThreadId(), mapToJson()};
  }


  public Object[] getUpdateFail() {
    reset();
    return new Object[]{getRetryCount(), getFailReason(), getLastRetryTime(),
        getOperateTimeForHis(), getValidStatus(), getId()};
  }

  public Object[] getUpdateSuccess() {
    reset();
    this.retryCount = retryCount++;
    validStatus = "1";
    return new Object[]{getRetryCount(), getLastRetryTime(),
        getOperateTimeForHis(), getValidStatus(), getId()};
  }


  public void setNextFailReason(String reason) {
    StringBuilder temp = new StringBuilder(failReason);
    String tempFailReason = temp.append("\n (").append(++retryCount).append(")").append(reason)
        .toString();
    this.failReason =
        tempFailReason.length() > 990 ? tempFailReason.substring(0, 990) : tempFailReason;

  }


  private void reset() {
    this.operateTimeForHis = new Date();
    this.lastRetryTime = new Date();
  }


  public String getExtraData() {
    return mapToJson();
  }

  public void setExtraData(String extraData) {
    this.extraData = extraData;
    jsonToMap();
  }

  public void setAllAttribute(NameSpace nameSpace) {
    if (this.threadLocalMap == null) {
      this.threadLocalMap = new HashMap<>();
    }
    this.threadLocalMap.putAll(nameSpace);
  }


  public Object getAttribute(String key) {
    if (threadLocalMap == null && !StringUtils.isEmpty(extraData)) {
      jsonToMap();
    }
    return this.threadLocalMap.get(key);
  }


  private String mapToJson() {
    try {
      return JacksonUtils.mapToJson(threadLocalMap);
    } catch (Exception e) {
      log.error("json转换出错, {}", e);
    }
    return "";
  }


  private void jsonToMap() {
    try {
      this.threadLocalMap = JacksonUtils.json2map(extraData);
    } catch (Exception e) {
      log.error("json转换出错", e);
    }
  }


  public static class NameSpace extends HashMap<String, Object> {

    private String prefix;

    public NameSpace(String prefix) {
      super();
      this.prefix = prefix;
    }

    public NameSpace(String prefix, int size) {
      super(size);
      this.prefix = prefix;
    }

    @Override
    public Object get(Object key) {
      Assert.notNull(String.valueOf(key), "key 不能为null");
      return super.get(String.valueOf(prefix + key).toLowerCase());
    }

    @Override
    public Object put(String key, Object value) {
      Assert.hasText(key, "key 不能为空");
      return super.put(String.valueOf(prefix + key).toLowerCase(), value);
    }


    public String getNameSpace() {
      return prefix.toLowerCase();
    }
  }

}
