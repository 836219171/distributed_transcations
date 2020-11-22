package com.summary.net.transcation.other;

import com.summary.net.transcation.model.ApiResponse;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author xiao liang
 * @version V1.0
 * @Package com.summary.net.transcation
 * @Title: OtherController
 * @Description: TODO
 * @date 2020/11/21 13:13
 */
@RestController
public class OtherController {


  @GetMapping("t1")
  public ApiResponse home1() {
    ApiResponse apiResponse = new ApiResponse("something  1");
    apiResponse.setStatus(1L);
    return apiResponse;
  }


  @GetMapping("t2")
  public ApiResponse home2() {
    return new ApiResponse("something  2");
  }

}
