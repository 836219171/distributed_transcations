package com.summary.net.transcation.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author xiao liang
 * @version V1.0
 * @Package com.picc.cms.common.distributed.transaction
 * @Title: UserTest
 * @Description: TODO
 * @date 2020/11/19 10:51
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class OrderVo {

  private String orderNo;

  private Integer price;

}
