package cn.skcks.docking.gb28181.sip.method.register.response;

import cn.skcks.docking.gb28181.sip.generic.SipBuilder;
import cn.skcks.docking.gb28181.sip.generic.SipResponseBuilder;
import cn.skcks.docking.gb28181.sip.method.register.RegisterBuilder;
import cn.skcks.docking.gb28181.sip.utils.DigestAuthenticationHelper;
import cn.skcks.docking.gb28181.sip.utils.SipUtil;
import gov.nist.javax.sip.header.Authorization;
import gov.nist.javax.sip.message.SIPRequest;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import lombok.experimental.SuperBuilder;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;

import javax.sip.header.WWWAuthenticateHeader;
import javax.sip.message.Request;
import javax.sip.message.Response;

@Slf4j
@Data
@SuperBuilder
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class RegisterResponseBuilder extends RegisterBuilder {
    /**
     * 不做任何校验 即无密码时 使用的认证响应 (如果请求的method错误 依然返回 401 认证失败)
     * @param request 请求
     */
    public Response createPassedAuthorzatioinResponse(Request request){
        SIPRequest sipRequest = (SIPRequest) request;
        if(!StringUtils.equalsIgnoreCase(sipRequest.getMethod(), getMethod())){
            return SipBuilder.addHeaders(
                    SipResponseBuilder.createResponse(Response.UNAUTHORIZED, request),
                    sipRequest.getContactHeader());
        }
        return SipBuilder.addHeaders(
                SipResponseBuilder.createResponse(Response.OK, request),
                sipRequest.getContactHeader(),
                sipRequest.getExpires());
    }

    public Response createAuthorzatioinResponse(Request request, String password){
        SIPRequest sipRequest = (SIPRequest) request;
        Authorization authorization = sipRequest.getAuthorization();
        if(authorization == null){
            String realm = SipUtil.nanoId();
            WWWAuthenticateHeader wwwAuthenticateHeader = DigestAuthenticationHelper.generateChallenge(realm);
            return SipBuilder.addHeaders(
                    SipResponseBuilder.createResponse(Response.UNAUTHORIZED, request),
                    sipRequest.getContactHeader(),
                    wwwAuthenticateHeader);
        }
        boolean passed = DigestAuthenticationHelper.doAuthenticatePlainTextPassword(request,password);
        if(!passed){
            sipRequest.removeHeader(Authorization.NAME);
            return createAuthorzatioinResponse(request, password);
        }
        return SipBuilder.addHeaders(
                SipResponseBuilder.createResponse(Response.OK, request),
                sipRequest.getContactHeader(),
                sipRequest.getExpires());
    }
}
