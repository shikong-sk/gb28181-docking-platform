package cn.skcks.docking.gb28181.sip.method.register.request;

import cn.skcks.docking.gb28181.sip.generic.SipBuilder;
import cn.skcks.docking.gb28181.sip.generic.SipRequestBuilder;
import cn.skcks.docking.gb28181.sip.method.register.RegisterBuilder;
import cn.skcks.docking.gb28181.sip.utils.DigestAuthenticationHelper;
import cn.skcks.docking.gb28181.sip.utils.SipUtil;
import gov.nist.javax.sip.message.SIPRequest;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.SneakyThrows;
import lombok.ToString;
import lombok.experimental.SuperBuilder;
import lombok.extern.slf4j.Slf4j;

import javax.sip.address.Address;
import javax.sip.address.SipURI;
import javax.sip.header.AuthorizationHeader;
import javax.sip.header.WWWAuthenticateHeader;
import javax.sip.message.Request;

@Slf4j
@Data
@SuperBuilder
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class RegisterRequestBuilder extends RegisterBuilder {
    private String localIp;
    private int localPort;
    private String localId;
    private String targetIp;
    private int targetPort;
    private String targetId;
    private String transport;

    public Request createNoAuthorizationRequest(String callId, int expires) {
        String local = SipBuilder.createHostAddress(getLocalIp(), getLocalPort());
        Address localAddress = SipBuilder.createAddress(SipBuilder.createSipURI(getLocalId(), local));
        String target = SipBuilder.createHostAddress(getTargetIp(), getTargetPort());
        SipURI targetUri = SipBuilder.createSipURI(getTargetId(), target);

        Request request = SipRequestBuilder.createRequest(targetUri, getMethod(),
                SipBuilder.createCallIdHeader(callId),
                SipBuilder.createCSeqHeader(1L, getMethod()),
                SipBuilder.createFromHeader(localAddress, SipUtil.generateFromTag()),
                SipBuilder.createToHeader(localAddress),
                SipBuilder.createViaHeaders(getTargetIp(), getTargetPort(), getTransport(), SipUtil.generateViaTag()),
                SipBuilder.createMaxForwardsHeader(70));
        return SipBuilder.addHeaders(request,
                SipBuilder.createExpiresHeader(expires),
                SipBuilder.createContactHeader(localAddress));
    }

    @SneakyThrows
    public Request createAuthorizationRequest(String callId, int expires, String id, String passwd, long cSeq, WWWAuthenticateHeader wwwAuthenticateHeader) {
        SIPRequest request = (SIPRequest) createNoAuthorizationRequest(callId, expires);
        request.getCSeq().setSeqNumber(cSeq + 1);
        AuthorizationHeader authorization = DigestAuthenticationHelper.createAuthorization(getMethod(), getTargetIp(), getTargetPort(), getTargetId(), id, passwd, (int) cSeq,wwwAuthenticateHeader);
        return SipBuilder.addHeaders(request,authorization);
    }
}
