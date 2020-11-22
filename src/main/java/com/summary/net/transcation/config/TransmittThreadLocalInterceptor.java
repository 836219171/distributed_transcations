package com.summary.net.transcation.config;

import com.summary.net.transcation.model.TranscationDTO;
import com.summary.net.transcation.model.TranscationDTO.NameSpace;
import com.summary.net.transcation.utils.ThreadLocalTransmittUtils;
import com.sun.javafx.UnmodifiableArrayList;
import java.util.List;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

/**
 * @author xiao liang
 * @version V1.0
 * @Package com.picc.cms.common.distributed.transaction
 * @Title: TransmittThreadLocalInterceptor
 * @Description:
 * @date 2020/11/19 14:31
 */
@TranscationPlugin(sort = 10)
public class TransmittThreadLocalInterceptor implements TranscationIntercetor {

  private static final String AUTHORIZATION_HEADER = "Authorization";
  private static final String GLOBAL_TRACE_ID = "global-trace-id";
  private static final String TSF_CONTEXT = "brave.propagation.TraceContext";


  public static final String NAME_SPACE = "requestArrtibutes";

  private static final List<String> arrtibuteList = new UnmodifiableArrayList<>(new String[]{AUTHORIZATION_HEADER,
      GLOBAL_TRACE_ID, TSF_CONTEXT}, 3);
  

  @Override
  public boolean isGlobal() {
    return true;
  }

  @Override
  public TranscationDTO preSave(TranscationDTO transcationDTO) throws Exception {
//    ServletRequestAttributes requestAttributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
//    NameSpace nameSpace = new NameSpace(NAME_SPACE);
//    for (String key : arrtibuteList) {
//      nameSpace.put(key, requestAttributes.getAttribute(key, RequestAttributes.SCOPE_REQUEST));
//    }
//    transcationDTO.setAllAttribute(nameSpace);

    return transcationDTO;
  }


  @Override
  public void postHandle(TranscationDTO transcationDTO) throws Exception {
//    ServletRequestAttributes requestAttributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
//    for (String key : arrtibuteList) {
//      requestAttributes.setAttribute(key, transcationDTO.getAttribute(NAME_SPACE + key), RequestAttributes.SCOPE_REQUEST);
//    }
  }

  @Override
  public void afterCompletetion(TranscationDTO transcationDTO, Exception exception) {
//   RequestContextHolder.resetRequestAttributes();
  }


}
