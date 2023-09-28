package cn.skcks.docking.gb28181.sip.method.message.response;

import cn.skcks.docking.gb28181.sip.generic.SipBuilder;
import cn.skcks.docking.gb28181.sip.generic.SipContentType;
import cn.skcks.docking.gb28181.sip.generic.SipResponseBuilder;
import cn.skcks.docking.gb28181.sip.method.message.MessageBuilder;
import gov.nist.javax.sip.message.SIPResponse;
import lombok.Data;
import lombok.SneakyThrows;
import lombok.ToString;
import lombok.experimental.SuperBuilder;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;

import javax.sip.message.Request;
import javax.sip.message.Response;

@Slf4j
@Data
@SuperBuilder
@ToString(callSuper = true)
public class MessageResponseBuilder implements MessageBuilder {
    @SneakyThrows
    public Response createMessageResponse(Request request,byte[] content){
        return createMessageResponse(request, content, null);
    }

    @SneakyThrows
    public Response createMessageResponse(Request request,byte[] content, String toTag) {
        Response response = SipResponseBuilder.createResponse(Response.OK, request, SipContentType.XML, content);
        SIPResponse sipResponse = (SIPResponse) response;
        if (StringUtils.isNotBlank(toTag)) {
            sipResponse.getToHeader().setTag(toTag);
        }
        return SipBuilder.addHeaders(response);
    }
}
