package com.summary.net.transcation.config;

import com.summary.net.transcation.DistrubutedTaskService;
import com.summary.net.transcation.constants.Constants;
import com.summary.net.transcation.dao.TranscationResposity;
import com.summary.net.transcation.model.TranscationDTO;
import com.sun.javafx.UnmodifiableArrayList;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.util.ObjectUtils;

/**
 * @author xiao liang
 * @version V1.0
 * @Package com.summary.net.transcation.config
 * @Title: TranscatinScheduledPool
 * @Description: TODO
 * @date 2020/11/22 19:24
 */
@Configuration
@EnableScheduling
public class TranscatinScheduled {


  @Autowired
  private TranscationResposity resposity;

  @Autowired
  private PluginChain pluginChain;

  private ExecutorService service = Executors.newFixedThreadPool(Constants.processors);

  private List<TranscationDTO> tasks = new ArrayList<>();;


  //每10秒随机取10条执行
  @Scheduled(cron = "0/10 * * * * ? ")
  public void configureTask() {
    System.out.println("开始执行.........");
    if (ObjectUtils.isEmpty(tasks) && !tasks.addAll(resposity.queryTranscationDTOList())) {
      return;
    }
    System.out.println("任务执行.........");
    for (int i = 0; i < tasks.size(); i++) {
      service.execute(new DistrubutedTaskService(tasks.get(i), pluginChain, resposity));
      tasks.remove(i);
    }
  }


}
