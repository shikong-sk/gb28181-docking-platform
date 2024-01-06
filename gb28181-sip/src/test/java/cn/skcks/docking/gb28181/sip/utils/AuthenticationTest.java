package cn.skcks.docking.gb28181.sip.utils;

import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;

import javax.sip.header.AuthorizationHeader;
import javax.sip.header.WWWAuthenticateHeader;
import javax.sip.message.Request;

@Slf4j
public class AuthenticationTest {
    public static final String serverId = "44050100002000000003";
    public static final String serverIp = "10.10.10.20";
    public static final int serverPort = 5060;
    public static final String domain = "4405010000";
    public static final String deviceId = "44050100001110000035";

    @SneakyThrows
    @Test
    void test() {
        AuthorizationHeader authorization = DigestAuthenticationHelper.createAuthorization(Request.REGISTER, serverIp, serverPort, serverId, deviceId, "123456", 1,null);
        log.info("\n{}", authorization);

        WWWAuthenticateHeader wwwAuthenticateHeader = DigestAuthenticationHelper.generateChallenge(domain);


        wwwAuthenticateHeader.setAlgorithm("MD5");
        wwwAuthenticateHeader.setQop("auth");
        wwwAuthenticateHeader.setNonce("08a895ede05c7ac592ced4070c1ef4aa");
        wwwAuthenticateHeader.setRealm(domain);
        log.info("\n{}", wwwAuthenticateHeader);


        authorization = DigestAuthenticationHelper.createAuthorization(Request.REGISTER, serverIp, serverPort, serverId, deviceId, "123456", 1, wwwAuthenticateHeader);
        log.info("\n{}", authorization);

        boolean passed = DigestAuthenticationHelper.doAuthenticatePlainTextPassword(Request.REGISTER, authorization, "123456");
        log.info("authorization passed {}", passed);

        authorization = DigestAuthenticationHelper.createAuthorization(Request.REGISTER, domain, serverId, deviceId, "123456", 1, wwwAuthenticateHeader);
        log.info("\n{}", authorization);

        passed = DigestAuthenticationHelper.doAuthenticatePlainTextPassword(Request.REGISTER, authorization, "123456");
        log.info("authorization passed {}", passed);

    }
}
