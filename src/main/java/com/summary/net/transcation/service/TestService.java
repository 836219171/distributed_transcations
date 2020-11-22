package com.summary.net.transcation.service;

import com.summary.net.transcation.config.DistributedTranscation;
import com.summary.net.transcation.model.OrderVo;
import com.summary.net.transcation.model.StockVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

/**
 * @author xiao liang
 * @version V1.0
 * @Package com.picc.cms.common.distributed.transaction
 * @Title: TestService
 * @Description: TODO
 * @date 2020/11/16 14:37
 */
@Service
public class TestService {

  @Autowired
  private RestTemplate restTemplate;




  @DistributedTranscation
  public void test(OrderVo orderVo, StockVo stockVo) {
    System.out.println(orderVo);
    System.out.println(stockVo);
    String result1 = restTemplate.getForObject("http://localhost:8808/t1", String.class);
    String result2 = restTemplate.getForObject("http://localhost:8808/t2", String.class);
    System.out.println(result1 + result2);
    if (!"{\"status\":1,\"statusText\":\"\",\"data\":\"something  1\"}".equals(result1)) {
      throw new RuntimeException("异常测试");
    }

  }


  @DistributedTranscation
  public void doTask() {
  }

}
