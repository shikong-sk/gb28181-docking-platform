package cn.skcks.docking.gb28181.sip.method.invite.request;

import cn.skcks.docking.gb28181.sdp.GB28181Description;
import cn.skcks.docking.gb28181.sdp.GB28181SDPBuilder;
import cn.skcks.docking.gb28181.sdp.media.MediaStreamMode;
import cn.skcks.docking.gb28181.sip.generic.SipBuilder;
import cn.skcks.docking.gb28181.sip.method.RequestBuilder;
import cn.skcks.docking.gb28181.sip.method.invite.InviteBuilder;
import gov.nist.javax.sip.message.SIPRequest;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import lombok.experimental.SuperBuilder;
import org.apache.commons.lang3.StringUtils;

import javax.sdp.Connection;
import javax.sip.address.Address;
import javax.sip.message.Request;

@Data
@SuperBuilder
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class InviteRequestBuilder extends RequestBuilder implements InviteBuilder {
    public Request createPlayInviteRequest(String callId, long cSeq, String channelId, String rtpIp, int rtpPort, String ssrc, MediaStreamMode mediaStreamMode, int receiveId){
        GB28181Description description = GB28181SDPBuilder.Receiver.play(getTargetId(), channelId, Connection.IP4,rtpIp,rtpPort,ssrc,mediaStreamMode);

        SIPRequest request = (SIPRequest) createRequest(METHOD, callId, cSeq, description);
        Address address = request.getFrom().getAddress();

        String subject = StringUtils.joinWith(",",
                // 发送者 channelId:流序号
                StringUtils.joinWith(":", channelId, ssrc),
                // 接收者 id:流序号号
                StringUtils.joinWith(":", getLocalId(), receiveId));
        return SipBuilder.addHeaders(request,
                SipBuilder.createContactHeader(address),
                SipBuilder.createSubjectHeader(subject));
    }

    public Request createPlayInviteRequest(String callId, long cSeq, String channelId, String rtpIp, int rtpPort, String ssrc, MediaStreamMode mediaStreamMode) {
        return createPlayInviteRequest(callId, cSeq, channelId, rtpIp, rtpPort, ssrc, mediaStreamMode, 0);
    }
}
