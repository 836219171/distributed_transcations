package com.summary.net.transcation.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author xiao liang
 * @version V1.0
 * @Package com.picc.cms.common.distributed.transaction
 * @Title: StockVo
 * @Description: TODO
 * @date 2020/11/19 11:50
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class StockVo {

  private String stockNo;

  private String productNo;

  private Long amount;

}
