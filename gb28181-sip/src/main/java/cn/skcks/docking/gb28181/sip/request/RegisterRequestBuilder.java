package cn.skcks.docking.gb28181.sip.request;

import cn.skcks.docking.gb28181.sip.generic.SipBuilder;
import cn.skcks.docking.gb28181.sip.generic.SipRequestBuilder;
import cn.skcks.docking.gb28181.sip.utils.SipUtil;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import javax.sip.address.Address;
import javax.sip.address.SipURI;
import javax.sip.message.Request;

@AllArgsConstructor
@Slf4j
public class RegisterRequestBuilder {
    private static final String method = Request.REGISTER;
    private String localIp;
    private int localPort;
    private String localId;
    private String targetIp;
    private int targetPort;
    private String targetId;

    private String transport;

    public Request createNoAuthorizationRequest(String callId) {
        String local = SipBuilder.createHostAddress(localIp, localPort);
        Address localAddress = SipBuilder.createAddress(SipBuilder.createSipURI(localId, local));
        String target = SipBuilder.createHostAddress(targetIp, targetPort);
        SipURI targetUri = SipBuilder.createSipURI(targetId, target);

        return SipRequestBuilder.createRequest(targetUri, method,
                SipBuilder.createCallIdHeader(callId),
                SipBuilder.createCSeqHeader(1L, method),
                SipBuilder.createFromHeader(localAddress, SipUtil.generateFromTag()),
                SipBuilder.createToHeader(localAddress),
                SipBuilder.createViaHeaders(targetIp, targetPort, transport, SipUtil.generateViaTag()),
                SipBuilder.createMaxForwardsHeader(70));
    }
}
