package com.summary.net.transcation.service.idservice;

import java.util.UUID;
import org.springframework.stereotype.Service;

/**
 * @author xiao liang
 * @version V1.0
 * @Package com.summary.net.transcation.idservice
 * @Title: GenerateIDSequenceService
 * @Description: TODO
 * @date 2020/11/21 13:46
 */
@Service
public class GenerateIDSequenceService {


  public String generateSequenceByRedis(String transcation) {
    return UUID.randomUUID().toString();
  }
}
