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
import javax.sip.header.SubjectHeader;
import javax.sip.message.Request;
import java.util.Date;

@Data
@SuperBuilder
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class InviteRequestBuilder extends RequestBuilder implements InviteBuilder {
    private SubjectHeader createSubject(String senderId, String senderStreamId, String receiveId, String receiveStreamId) {
        String subject = StringUtils.joinWith(",",
                // 发送者 channelId:流序号
                StringUtils.joinWith(":", senderId, senderStreamId),
                // 接收者 id:流序号号
                StringUtils.joinWith(":", receiveId, receiveStreamId));
        return SipBuilder.createSubjectHeader(subject);
    }

    public Request createPlayInviteRequest(String callId, long cSeq, String channelId, String rtpIp, int rtpPort, String ssrc, MediaStreamMode mediaStreamMode, String receiveId) {
        GB28181Description description = GB28181SDPBuilder.Receiver.play(getTargetId(), channelId, Connection.IP4, rtpIp, rtpPort, ssrc, mediaStreamMode);

        SIPRequest request = (SIPRequest) createRequest(METHOD, callId, cSeq, description);
        Address address = request.getFrom().getAddress();

        return SipBuilder.addHeaders(request,
                SipBuilder.createContactHeader(address),
                createSubject(channelId, ssrc, getLocalId(), receiveId));
    }

    public Request createPlayInviteRequest(String callId, long cSeq, String channelId, String rtpIp, int rtpPort, String ssrc, MediaStreamMode mediaStreamMode) {
        return createPlayInviteRequest(callId, cSeq, channelId, rtpIp, rtpPort, ssrc, mediaStreamMode, String.valueOf(0));
    }

    public Request createPlaybackInviteRequest(String callId, long cSeq, String channelId, String rtpIp, int rtpPort, String ssrc, MediaStreamMode mediaStreamMode, String receiveId, Date startTime, Date endTime) {
        GB28181Description description = GB28181SDPBuilder.Receiver.playback(getTargetId(), channelId, Connection.IP4, rtpIp, rtpPort, ssrc, mediaStreamMode, startTime, endTime);

        SIPRequest request = (SIPRequest) createRequest(METHOD, callId, cSeq, description);
        Address address = request.getFrom().getAddress();

        return SipBuilder.addHeaders(request,
                SipBuilder.createContactHeader(address),
                createSubject(channelId, ssrc, getLocalId(), receiveId));
    }

    public Request createPlaybackInviteRequest(String callId, long cSeq, String channelId, String rtpIp, int rtpPort, String ssrc, MediaStreamMode mediaStreamMode, Date startTime, Date endTime) {
        return createPlaybackInviteRequest(callId, cSeq, channelId, rtpIp, rtpPort, ssrc, mediaStreamMode, String.valueOf(0), startTime, endTime);
    }

    public Request createDownloadInviteRequest(String callId, long cSeq, String channelId, String rtpIp, int rtpPort, String ssrc, MediaStreamMode mediaStreamMode, String receiveId, Date startTime, Date endTime, Double downloadSpeed) {
        GB28181Description description = GB28181SDPBuilder.Receiver.download(getTargetId(), channelId, Connection.IP4, rtpIp, rtpPort, ssrc, mediaStreamMode, startTime, endTime, downloadSpeed);

        SIPRequest request = (SIPRequest) createRequest(METHOD, callId, cSeq, description);
        Address address = request.getFrom().getAddress();

        return SipBuilder.addHeaders(request,
                SipBuilder.createContactHeader(address),
                createSubject(channelId, ssrc, getLocalId(), receiveId));
    }

    public Request createDownloadInviteRequest(String callId, long cSeq, String channelId, String rtpIp, int rtpPort, String ssrc, MediaStreamMode mediaStreamMode, Date startTime, Date endTime, Double downloadSpeed) {
        return createDownloadInviteRequest(callId, cSeq, channelId, rtpIp, rtpPort, ssrc, mediaStreamMode, String.valueOf(0), startTime, endTime, downloadSpeed);
    }

    public Request createByeRequest(String callId, long cSeq) {
        return createRequest(Request.BYE, callId, cSeq);
    }
}
