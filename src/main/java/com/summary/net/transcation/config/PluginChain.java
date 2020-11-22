package com.summary.net.transcation.config;

import java.util.ArrayList;
import java.util.List;

/**
 * @author xiao liang
 * @version V1.0
 * @Package com.picc.cms.common.distributed.transaction
 * @Title: PluginInit
 * @Description:
 * @date 2020/11/19 15:40
 */
public class PluginChain  {

  private List<TranscationIntercetor> plugins = new ArrayList<>();

  /**
   * 添加插件
   */
  public void addPlugin(TranscationIntercetor plugin) {
    plugins.add(plugin);
  }

  /**
   * 获取所有的插件
   */
  public List<TranscationIntercetor> getAll() {
    return plugins;
  }






}
