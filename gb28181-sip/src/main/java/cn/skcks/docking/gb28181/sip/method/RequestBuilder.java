package cn.skcks.docking.gb28181.sip.method;

import cn.skcks.docking.gb28181.sip.generic.SipBuilder;
import cn.skcks.docking.gb28181.sip.generic.SipRequestBuilder;
import cn.skcks.docking.gb28181.sip.utils.SipUtil;
import lombok.Data;
import lombok.experimental.SuperBuilder;

import javax.sip.address.Address;
import javax.sip.address.SipURI;
import javax.sip.message.Request;

@Data
@SuperBuilder
public class RequestBuilder {
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

        return SipRequestBuilder.createRequest(targetUri, method,
                SipBuilder.createCallIdHeader(callId),
                SipBuilder.createCSeqHeader(cSeq, method),
                SipBuilder.createFromHeader(localAddress, SipUtil.generateFromTag()),
                SipBuilder.createToHeader(localAddress),
                SipBuilder.createViaHeaders(getTargetIp(), getTargetPort(), getTransport(), SipUtil.generateViaTag()),
                SipBuilder.createMaxForwardsHeader(70));
    }
}
