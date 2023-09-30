package cn.skcks.docking.gb28181.sip.method.invite.response;

import cn.skcks.docking.gb28181.sdp.GB28181Description;
import cn.skcks.docking.gb28181.sdp.GB28181SDPBuilder;
import cn.skcks.docking.gb28181.sdp.parser.GB28181DescriptionParser;
import cn.skcks.docking.gb28181.sip.generic.SipBuilder;
import cn.skcks.docking.gb28181.sip.generic.SipContentType;
import cn.skcks.docking.gb28181.sip.generic.SipResponseBuilder;
import cn.skcks.docking.gb28181.sip.method.invite.InviteBuilder;
import gov.nist.javax.sip.message.SIPRequest;
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
public class InviteResponseBuilder implements InviteBuilder {
    @SneakyThrows
    public Response createInviteResponse(Request request, GB28181Description gb28181Description, String toTag){
        Response response = SipResponseBuilder.createResponse(Response.OK, request, SipContentType.SDP, gb28181Description);
        return addHeader(response, toTag);
    }

    @SneakyThrows
    public Response createInviteResponse(Request request, String rtpIp, int rtpPort){
        return createInviteResponse(request, rtpIp, rtpPort, "");
    }

    @SneakyThrows
    public Response createInviteResponse(Request request, String rtpIp, int rtpPort, String toTag){
        SIPRequest sipRequest = (SIPRequest) request;
        GB28181DescriptionParser receive = new GB28181DescriptionParser(new String(sipRequest.getRawContent()));
        GB28181Description gb28181Description = GB28181SDPBuilder.Sender.build(receive.parse(), rtpIp, rtpPort);
        Response response = SipResponseBuilder.createResponse(Response.OK, request, SipContentType.SDP, gb28181Description);
        return addHeader(response, toTag);
    }

    @SneakyThrows
    private Response addHeader(Response response, String toTag){
        SIPResponse sipResponse = (SIPResponse) response;
        if (StringUtils.isNotBlank(toTag)) {
            sipResponse.getToHeader().setTag(toTag);
        }
        return SipBuilder.addHeaders(
                response,
                SipBuilder.createContactHeader(sipResponse.getFromHeader().getAddress()));
    }

    public Response createTryingInviteResponse(Request request){
        return SipResponseBuilder.createResponse(Response.TRYING,request);
    }
}
