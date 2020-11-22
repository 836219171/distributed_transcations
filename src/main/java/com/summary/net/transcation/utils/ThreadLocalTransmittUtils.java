package com.summary.net.transcation.utils;

/**
 * @author xiao liang
 * @version V1.0
 * @Package com.summary.net.transcation.utils
 * @Title: ThreadLocalTransmittUtils
 * @Description: TODO
 * @date 2020/11/22 20:25
 */
public class ThreadLocalTransmittUtils {

  public static ThreadLocal<Object> threadLocal = ThreadLocal.withInitial(() -> false);



}
