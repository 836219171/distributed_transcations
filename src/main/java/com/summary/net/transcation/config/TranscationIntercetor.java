package com.summary.net.transcation.config;

import com.summary.net.transcation.model.TranscationDTO;

/**
 * @author xiao liang
 * @version V1.0
 * @Package com.picc.cms.common.distributed.transaction
 * @Title: TranscationValve
 * @Description:
 * @date 2020/11/19 14:23
 */
public interface TranscationIntercetor {

  boolean isGlobal();

  default TranscationDTO preSave(TranscationDTO transcationDTO) throws Exception {
   return null;
  }

  default void postHandle(TranscationDTO transcationDTO)throws Exception {

  }

  default void afterCompletetion(TranscationDTO transcationDTO, Exception exception){

  }

}
