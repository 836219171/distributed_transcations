package com.summary.net.transcation.model;

/**
 * @author xiao liang
 * @version V1.0
 * @Package com.summary.net.transcation
 * @Title: ApiResponse
 * @Description: TODO
 * @date 2020/11/21 13:14
 */
public class ApiResponse<T> {

  private static final long serialVersionUID = 1L;
  public static final int SUCCESS = 0;
  public static final int FAIL = -1;
  public static final int BUSY = -100;
  public static final String SUCCESS_TEXT = "Success";
  public static final String FAIL_TEXT = "Fail";
  public static final String BUSY_TEXT = "Busy";
  private long status;
  private String statusText;
  private T data;

  public ApiResponse() {
    this.status = 0L;
    this.statusText = "Success";
  }

  public ApiResponse(long status, String statusText, T data) {
    this.status = status;
    this.statusText = statusText;
    this.data = data;
  }

  public ApiResponse(T data) {
    if (data instanceof Exception) {
      this.status = -1L;
      this.statusText = ((Exception)data).getLocalizedMessage();
    } else {
      this.status = 0L;
      this.statusText = "Success";
      this.data = data;
    }

  }

  public long getStatus() {
    return this.status;
  }

  public void setStatus(long status) {
    this.status = status;
    if (status == 0L) {
      this.statusText = "Success";
    } else if (status == -100L) {
      this.statusText = "Busy";
    } else {
      this.statusText = "";
    }

  }

  public String getStatusText() {
    return this.statusText;
  }

  public void setStatusText(String statusText) {
    this.statusText = statusText;
  }

  public T getData() {
    return this.data;
  }

  public void setData(T data) {
    this.data = data;
  }

  public static <M> ApiResponse<M> ok() {
    return new ApiResponse();
  }

  public static <T> ApiResponse<T> ok(T data) {
    return new ApiResponse(data);
  }

}
