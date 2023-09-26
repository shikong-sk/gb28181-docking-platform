package cn.skcks.docking.gb28181.sip.method.register.response;

import cn.hutool.core.date.DateUtil;
import cn.skcks.docking.gb28181.sip.generic.SipBuilder;
import cn.skcks.docking.gb28181.sip.generic.SipResponseBuilder;
import cn.skcks.docking.gb28181.sip.header.GBDateHeader;
import cn.skcks.docking.gb28181.sip.method.register.RegisterBuilder;
import cn.skcks.docking.gb28181.sip.utils.DigestAuthenticationHelper;
import gov.nist.javax.sip.header.Authorization;
import gov.nist.javax.sip.message.SIPRequest;
import lombok.*;
import lombok.experimental.SuperBuilder;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;

import javax.sip.header.WWWAuthenticateHeader;
import javax.sip.message.Request;
import javax.sip.message.Response;

@Slf4j
@Data
@SuperBuilder
@ToString(callSuper = true)
public class RegisterResponseBuilder implements RegisterBuilder {
    /**
     * 不做任何校验 即无密码时 使用的认证响应 (如果请求的method错误 依然返回 401 认证失败)
     *
     * @param request 请求
     */
    public Response createPassedAuthorzatioinResponse(Request request) {
        SIPRequest sipRequest = (SIPRequest) request;
        if (!StringUtils.equalsIgnoreCase(sipRequest.getMethod(), METHOD)) {
            return SipBuilder.addHeaders(
                    SipResponseBuilder.createResponse(Response.UNAUTHORIZED, request),
                    sipRequest.getContactHeader());
        }
        return SipBuilder.addHeaders(
                SipResponseBuilder.createResponse(Response.OK, request),
                sipRequest.getContactHeader(),
                sipRequest.getExpires(),
                new GBDateHeader(DateUtil.calendar()));
    }

    public Response createAuthorzatioinResponse(Request request, String domain, String password) {
        SIPRequest sipRequest = (SIPRequest) request;
        Authorization authorization = sipRequest.getAuthorization();
        if (authorization == null) {
            WWWAuthenticateHeader wwwAuthenticateHeader = DigestAuthenticationHelper.generateChallenge(domain);
            return SipBuilder.addHeaders(
                    SipResponseBuilder.createResponse(Response.UNAUTHORIZED, request),
                    sipRequest.getContactHeader(),
                    wwwAuthenticateHeader);
        }
        boolean passed = DigestAuthenticationHelper.doAuthenticatePlainTextPassword(request, password);
        if (!passed) {
            sipRequest.removeHeader(Authorization.NAME);
            return createAuthorzatioinResponse(request, domain, password);
        }
        return SipBuilder.addHeaders(
                SipResponseBuilder.createResponse(Response.OK, request),
                sipRequest.getContactHeader(),
                sipRequest.getExpires(),
                new GBDateHeader(DateUtil.calendar()));
    }
}
