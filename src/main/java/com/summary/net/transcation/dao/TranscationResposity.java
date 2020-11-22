package com.summary.net.transcation.dao;

import com.summary.net.transcation.model.TranscationDTO;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

/**
 * @author xiao liang
 * @version V1.0
 * @Package com.picc.cms.common.distributed.transaction
 * @Title: DistributedTranscation
 * @Description:
 * @date 2020/11/16 11:11
 */
@Repository
public class TranscationResposity {

  @Autowired
  private JdbcTemplate jdbcTemplate;

  private static final String QUERY_SQL = "select * from distributed_transcations where retrycount <= 3 and validstatus = '0'  ORDER BY RAND() limit 10";

  private static final String INSERT_SQL = "insert into distributed_transcations (id, "
      + "classname, methodname, beanname, failreason, retrycount ,lastretrytime, inserttimeforhis,"
      + "operatetimeforhis, paramcontents, paramclass, validstatus, threadId, extradata) values (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

  private static final String UPDATE_FAIL_SQL = "update distributed_transcations set retrycount = ? ,failreason = ?, lastretrytime  = ?, "
      + "operatetimeforhis = ?,validstatus = ? where id = ?";

  private static final String UPDATE_SUCCESS_SQL = "update distributed_transcations set retrycount = ? ,lastretrytime  = ?, "
      + "operatetimeforhis = ?,validstatus = ? where id = ?";


  /**
   * 异常入库
   *
   * @param transcationDTO
   */
  public void save(TranscationDTO transcationDTO) {
    jdbcTemplate.update(INSERT_SQL, transcationDTO.getSnapshop());
  }


  /**
   * 异常条件更新
   *
   * @param transcationDTO
   */
  public void retryFail(TranscationDTO transcationDTO) {
    jdbcTemplate.update(UPDATE_FAIL_SQL, transcationDTO.getUpdateFail());
  }

  public void retrySuccess(TranscationDTO transcationDTO) {
    jdbcTemplate.update(UPDATE_SUCCESS_SQL, transcationDTO.getUpdateSuccess());
  }


  /**
   * 查询待办事务任务
   * @return
   */
  public List<TranscationDTO> queryTranscationDTOList() {
    return jdbcTemplate.query(QUERY_SQL, (rs, rowNum) ->
        TranscationDTO.builder().id(rs.getString("id"))
        .beanName(rs.getString("beanname"))
        .methodName(rs.getString("methodname"))
        .className(rs.getString("classname"))
        .retryCount(rs.getInt("retrycount"))
        .failReason(rs.getString("failreason"))
        .paramClass(rs.getString("paramclass"))
        .paramContents(rs.getString("paramcontents"))
        .validStatus(rs.getString("validstatus"))
        .lastRetryTime(rs.getTimestamp("lastretrytime"))
        .insertTimeForHis(rs.getTimestamp("inserttimeforhis"))
        .operateTimeForHis(rs.getTimestamp("operatetimeforhis"))
        .threadId(rs.getLong("threadid"))
        .extraData(rs.getString("extradata")).build());
  }


}
