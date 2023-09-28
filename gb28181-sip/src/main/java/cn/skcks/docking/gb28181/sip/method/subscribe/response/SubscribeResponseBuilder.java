package cn.skcks.docking.gb28181.sip.method.subscribe.response;

import cn.skcks.docking.gb28181.sip.generic.SipResponseBuilder;
import lombok.Data;
import lombok.ToString;
import lombok.experimental.SuperBuilder;
import lombok.extern.slf4j.Slf4j;

import javax.sip.message.Request;
import javax.sip.message.Response;

@Slf4j
@Data
@SuperBuilder
@ToString(callSuper = true)
public class SubscribeResponseBuilder {
    public Response createSubscribeResponse(Request request, byte[] content) {
        return SipResponseBuilder.createXmlResponse(Response.OK, request, content);
    }
}
