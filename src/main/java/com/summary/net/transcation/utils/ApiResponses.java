package com.summary.net.transcation.utils;

import com.summary.net.transcation.model.ApiResponse;

/**
 * @author xiao liang
 * @version V1.0
 * @Package com.picc.cms.utils
 * @Title: ApiResponses
 * @Description: TODO
 * @date 2020/4/15 9:13
 */
public class ApiResponses<T> {


  public static ApiResponse error(String msg) {
    ApiResponse apiResponse = new ApiResponse();
    apiResponse.setStatus(ApiResponse.FAIL);
    apiResponse.setStatusText(msg);
    return apiResponse;
  }

  public  static <T> ApiResponse success(T data) {
    ApiResponse apiResponse = new ApiResponse();
    apiResponse.setStatus(ApiResponse.SUCCESS);
    apiResponse.setStatusText("Success");
    apiResponse.setData(data);
    return apiResponse;
  }

  public  static <T> ApiResponse success() {
    return ApiResponse.ok();
  }

}
