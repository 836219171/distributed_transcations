package com.summary.net.transcation.config;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import org.springframework.stereotype.Component;

/**
 * @author xiao liang
 * @version V1.0
 * @Package com.picc.cms.common.distributed.transaction
 * @Title: TranscationPlugin
 * @Description:
 * @date 2020/11/19 15:39
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
@Documented
@Component
public @interface TranscationPlugin {


  /**
   * 插件执行顺序控制，越小越早执行
   */
  int sort();

}
