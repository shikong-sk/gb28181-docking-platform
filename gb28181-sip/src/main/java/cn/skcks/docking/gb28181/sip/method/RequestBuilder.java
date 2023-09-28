package cn.skcks.docking.gb28181.sip.method;

import cn.skcks.docking.gb28181.sdp.GB28181Description;
import cn.skcks.docking.gb28181.sip.generic.SipBuilder;
import cn.skcks.docking.gb28181.sip.generic.SipContentType;
import cn.skcks.docking.gb28181.sip.generic.SipRequestBuilder;
import cn.skcks.docking.gb28181.sip.utils.SipUtil;
import lombok.Data;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

import javax.sip.address.Address;
import javax.sip.address.SipURI;
import javax.sip.message.Request;

@SuppressWarnings("Duplicates")
@Data
@SuperBuilder
public class RequestBuilder {
    @Setter
    public static int DEFAULT_MAX_FORWARD = 70;
    private String localIp;
    private int localPort;
    private String localId;
    private String targetIp;
    private int targetPort;
    private String targetId;
    private String transport;

    public Request createRequest(String method, String callId, long cSeq) {
        String local = SipBuilder.createHostAddress(getLocalIp(), getLocalPort());
        Address localAddress = SipBuilder.createAddress(SipBuilder.createSipURI(getLocalId(), local));
        String target = SipBuilder.createHostAddress(getTargetIp(), getTargetPort());
        SipURI targetUri = SipBuilder.createSipURI(getTargetId(), target);
        Address targetAddress = SipBuilder.createAddress(targetUri);

        return SipRequestBuilder.createRequest(targetUri, method,
                SipBuilder.createCallIdHeader(callId),
                SipBuilder.createCSeqHeader(cSeq, method),
                SipBuilder.createFromHeader(localAddress, SipUtil.generateFromTag()),
                SipBuilder.createToHeader(targetAddress),
                SipBuilder.createViaHeaders(getTargetIp(), getTargetPort(), getTransport(), SipUtil.generateViaTag()),
                SipBuilder.createMaxForwardsHeader(DEFAULT_MAX_FORWARD));
    }

    public Request createRequest(String method, String callId, long cSeq, byte[] content) {
        String local = SipBuilder.createHostAddress(getLocalIp(), getLocalPort());
        Address localAddress = SipBuilder.createAddress(SipBuilder.createSipURI(getLocalId(), local));
        String target = SipBuilder.createHostAddress(getTargetIp(), getTargetPort());
        SipURI targetUri = SipBuilder.createSipURI(getTargetId(), target);
        Address targetAddress = SipBuilder.createAddress(targetUri);

        return SipRequestBuilder.createRequest(targetUri, method,
                SipBuilder.createCallIdHeader(callId),
                SipBuilder.createCSeqHeader(cSeq, method),
                SipBuilder.createFromHeader(localAddress, SipUtil.generateFromTag()),
                SipBuilder.createToHeader(targetAddress),
                SipBuilder.createViaHeaders(getTargetIp(), getTargetPort(), getTransport(), SipUtil.generateViaTag()),
                SipBuilder.createMaxForwardsHeader(DEFAULT_MAX_FORWARD), SipContentType.XML, content);
    }

    public Request createRequest(String method, String callId, long cSeq, GB28181Description description) {
        String local = SipBuilder.createHostAddress(getLocalIp(), getLocalPort());
        Address localAddress = SipBuilder.createAddress(SipBuilder.createSipURI(getLocalId(), local));
        String target = SipBuilder.createHostAddress(getTargetIp(), getTargetPort());
        SipURI targetUri = SipBuilder.createSipURI(getTargetId(), target);
        Address targetAddress = SipBuilder.createAddress(targetUri);

        return SipRequestBuilder.createRequest(targetUri, method,
                SipBuilder.createCallIdHeader(callId),
                SipBuilder.createCSeqHeader(cSeq, method),
                SipBuilder.createFromHeader(localAddress, SipUtil.generateFromTag()),
                SipBuilder.createToHeader(targetAddress),
                SipBuilder.createViaHeaders(getTargetIp(), getTargetPort(), getTransport(), SipUtil.generateViaTag()),
                SipBuilder.createMaxForwardsHeader(DEFAULT_MAX_FORWARD), SipContentType.SDP, description.toString());
    }
}
