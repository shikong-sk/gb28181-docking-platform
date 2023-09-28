package cn.skcks.docking.gb28181.sip.method.register.request;

import cn.skcks.docking.gb28181.sip.generic.SipBuilder;
import cn.skcks.docking.gb28181.sip.method.RequestBuilder;
import cn.skcks.docking.gb28181.sip.method.register.RegisterBuilder;
import cn.skcks.docking.gb28181.sip.utils.DigestAuthenticationHelper;
import gov.nist.javax.sip.message.SIPRequest;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.SneakyThrows;
import lombok.ToString;
import lombok.experimental.SuperBuilder;

import javax.sip.address.Address;
import javax.sip.header.AuthorizationHeader;
import javax.sip.header.WWWAuthenticateHeader;
import javax.sip.message.Request;

@Data
@SuperBuilder
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class RegisterRequestBuilder extends RequestBuilder implements RegisterBuilder {


    public Request createNoAuthorizationRequest(String callId, int expires) {
        String local = SipBuilder.createHostAddress(getLocalIp(), getLocalPort());
        Address localAddress = SipBuilder.createAddress(SipBuilder.createSipURI(getLocalId(), local));

        SIPRequest request = (SIPRequest) createRequest(METHOD, callId, 1);
        Address address = request.getFromHeader().getAddress();
        request.getToHeader().setAddress(address);
        return SipBuilder.addHeaders(request,
                SipBuilder.createExpiresHeader(expires),
                SipBuilder.createContactHeader(localAddress));
    }

    @SneakyThrows
    public Request createAuthorizationRequest(String callId, int expires, String id, String passwd, long cSeq, WWWAuthenticateHeader wwwAuthenticateHeader) {
        SIPRequest request = (SIPRequest) createNoAuthorizationRequest(callId, expires);
        request.getCSeq().setSeqNumber(cSeq);
        AuthorizationHeader authorization = DigestAuthenticationHelper.createAuthorization(METHOD, getTargetIp(), getTargetPort(), getTargetId(), id, passwd, (int) cSeq, wwwAuthenticateHeader);
        return SipBuilder.addHeaders(request, authorization);
    }
}
