package cn.skcks.docking.gb28181.sip.process;

import cn.hutool.core.util.IdUtil;
import cn.skcks.docking.gb28181.sip.method.register.request.RegisterRequestBuilder;
import cn.skcks.docking.gb28181.sip.method.register.response.RegisterResponseBuilder;
import cn.skcks.docking.gb28181.sip.utils.SipUtil;
import gov.nist.javax.sip.message.SIPResponse;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;

import javax.sip.ListeningPoint;
import javax.sip.header.WWWAuthenticateHeader;
import javax.sip.message.Request;
import javax.sip.message.Response;

@Slf4j
public class RequestTest {
    String localIp = "127.0.0.1";
    int localPort = 5060;
    String localId = "00000000000000000001";

    String remoteIp = "10.10.10.200";
    int remotePort = 5060;
    String remoteId = "00000000000000000002";

    public static final String domain = "4405010000";

    @Test
    @SneakyThrows
    void test() {
        SipUtil.setUserAgentVersion("0.1.0");
        String callId = IdUtil.fastSimpleUUID();
        RegisterRequestBuilder registerRequestBuilder = RegisterRequestBuilder.builder()
                .localIp(localIp)
                .localPort(localPort)
                .localId(localId)
                .targetIp(remoteIp)
                .targetPort(remotePort)
                .targetId(remoteId)
                .transport(ListeningPoint.UDP)
                .build();

        RegisterResponseBuilder registerResponseBuilder = RegisterResponseBuilder.builder().build();

        log.info("无密码的认证");
        Request noAuthorizationRequest = registerRequestBuilder.createNoAuthorizationRequest(callId, 3600);
        log.info("\n{}", noAuthorizationRequest);
        // 服务端不设置无密码直接通过
        Response passedAuthorzatioinResponse = registerResponseBuilder.createPassedAuthorzatioinResponse(noAuthorizationRequest);
        log.info("\n{}", passedAuthorzatioinResponse);

        log.info("有密码的认证");
        Response authorzatioinResponse = registerResponseBuilder.createAuthorzatioinResponse(noAuthorizationRequest, domain, "123456");
        log.info("\n{}", noAuthorizationRequest);
        // 401 响应
        log.info("\n{}", authorzatioinResponse);
        SIPResponse sipResponse = (SIPResponse) authorzatioinResponse;
        WWWAuthenticateHeader wwwAuthenticateHeader = (WWWAuthenticateHeader) sipResponse.getHeader(WWWAuthenticateHeader.NAME);
        long cSeq = sipResponse.getCSeq().getSeqNumber();
        // 重新发起带有认证信息的请求
        Request authorizationRequest = registerRequestBuilder.createAuthorizationRequest(callId, 3600, localId, "123456", cSeq, wwwAuthenticateHeader);
        log.info("\n{}", authorizationRequest);
        authorzatioinResponse = registerResponseBuilder.createAuthorzatioinResponse(authorizationRequest, domain, "123456");
        // 注册成功
        log.info("\n{}", authorzatioinResponse);
    }
}
