package com.summary.net.transcation.controller;

import com.summary.net.transcation.service.TestService;
import com.summary.net.transcation.config.TranscationIntercetor;
import com.summary.net.transcation.model.ApiResponse;
import com.summary.net.transcation.model.OrderVo;
import com.summary.net.transcation.model.StockVo;
import com.summary.net.transcation.utils.ApiResponses;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;

/**
 * @author dzx
 */
@RestController
@RequestMapping("/")
public class TestController {

  @Autowired
  private TestService testService;

  @Autowired
  private List<TranscationIntercetor> list;


  @GetMapping("tt")
  public ApiResponse home() {
    testService.test(new OrderVo("123456", new Integer(1234)),
        new StockVo("abc", "2344", new Long(200L)));
    return ApiResponses.success("something");
  }



  @GetMapping("yy")
  public ApiResponse homes() {
    testService.doTask();
    return ApiResponses.success("something");
  }



  @GetMapping("test")
  public ApiResponse test() {
    System.out.println(list);
    System.out.println("^^^^^^^^^^^^^^^^^^^^");
    RequestAttributes requestAttributes = RequestContextHolder.getRequestAttributes();
    System.out.println(requestAttributes);
    ExecutorService service = Executors.newSingleThreadExecutor();
    service.execute(() -> {
      System.out.println("*********************");
      RequestAttributes requestAttribu = RequestContextHolder.getRequestAttributes();
      System.out.println(requestAttribu);
      System.out.println("*********************");
    });
    return ApiResponses.success("something");
  }




}
