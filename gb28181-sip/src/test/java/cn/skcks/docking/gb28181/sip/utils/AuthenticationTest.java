package cn.skcks.docking.gb28181.sip.utils;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;

import javax.sip.header.AuthorizationHeader;
import javax.sip.header.WWWAuthenticateHeader;
import javax.sip.message.Request;

@Slf4j
public class AuthenticationTest {
    public static final String serverId = "44050100002000000001";
    public static final String serverIp = "10.10.10.200";
    public static final int serverPort = 5060;
    public static final String domain = "4405010000";
    public static final String deviceId = "44050100001110000010";

    @Test
    void test() {
        AuthorizationHeader authorization = DigestAuthenticationHelper.createAuthorization(serverIp, serverPort, serverId, deviceId, "123456",null);
        log.info("\n{}", authorization);

        WWWAuthenticateHeader wwwAuthenticateHeader = DigestAuthenticationHelper.generateChallenge(domain);
        log.info("\n{}", wwwAuthenticateHeader);

        authorization = DigestAuthenticationHelper.createAuthorization(serverIp, serverPort, serverId, deviceId, "123456",wwwAuthenticateHeader);
        log.info("\n{}", authorization);

        boolean passed = DigestAuthenticationHelper.doAuthenticatePlainTextPassword(Request.REGISTER, authorization, "123456");
        log.info("authorization passed {}",passed);

    }
}
