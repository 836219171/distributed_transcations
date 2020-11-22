package com.summary.net.transcation.utils;

/**
 * @author xiao liang
 * @version V1.0
 * @Package com.picc.cms.common.distributed.transaction
 * @Title: ThreadLocalUtils
 * @Description: TODO
 * @date 2020/11/17 14:02
 */
public class ThreadLocalUtils {

  private static ThreadLocal<Boolean> threadLocal = ThreadLocal.withInitial(() -> false);


  public static void setResume() {
    threadLocal.set(true);
  }

  public static boolean isResume() {
    return threadLocal.get();
  }

  public static void remove() {
    threadLocal.remove();
  }

}
