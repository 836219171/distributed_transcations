package com.summary.net.transcation.config;

import java.util.List;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.util.CollectionUtils;

/**
 * @author xiao liang
 * @version V1.0
 * @Package com.picc.cms.common.distributed.transaction
 * @Title: PluginConfig
 * @Description: TODO
 * @date 2020/11/19 15:58
 */
@Configuration
public class PluginConfig {


  @Bean
  public PluginChain configPluginChain(List<TranscationIntercetor> plugins) {
    PluginChain pluginChain = new PluginChain();
    if (CollectionUtils.isEmpty(plugins)) {
      return pluginChain;
    }
    for (TranscationIntercetor plugin : plugins) {
      pluginChain.addPlugin(plugin);
    }
    return pluginChain;
  }


}
