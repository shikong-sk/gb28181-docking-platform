package cn.skcks.docking.gb28181.sip.utils;

import cn.hutool.core.util.IdUtil;
import cn.hutool.crypto.digest.DigestAlgorithm;
import cn.hutool.crypto.digest.DigestUtil;
import cn.skcks.docking.gb28181.sip.generic.SipBuilder;
import lombok.*;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.ObjectUtils;

import javax.sip.SipFactory;
import javax.sip.address.SipURI;
import javax.sip.address.URI;
import javax.sip.header.AuthorizationHeader;
import javax.sip.header.WWWAuthenticateHeader;
import javax.sip.message.Request;
import javax.sip.message.Response;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.time.Instant;
import java.util.Random;

@Slf4j
public class DigestAuthenticationHelper {
    private static final MessageDigest messageDigest;

    public static final String DEFAULT_ALGORITHM = "MD5";
    public static final String DEFAULT_SCHEME = "Digest";


    /** to hex converter */
    private static final char[] toHex = { '0', '1', '2', '3', '4', '5', '6',
            '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f' };

    /**
     * Default constructor.
     * @throws NoSuchAlgorithmException
     */
    static{
        try {
            messageDigest = MessageDigest.getInstance(DEFAULT_ALGORITHM);
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
    }

    public static String toHexString(byte[] b) {
        int pos = 0;
        char[] c = new char[b.length * 2];
        for (byte value : b) {
            c[pos++] = toHex[(value >> 4) & 0x0F];
            c[pos++] = toHex[value & 0x0f];
        }
        return new String(c);
    }

    /**
     * Generate the challenge string.
     *
     * @return a generated nonce.
     */
    private static String generateNonce() {
        long time = Instant.now().toEpochMilli();
        Random rand = new Random();
        long pad = rand.nextLong();
        String nonceString = Long.valueOf(time).toString() + Long.valueOf(pad).toString();
        byte[] mdBytes = messageDigest.digest(nonceString.getBytes());
        return toHexString(mdBytes);
    }

    @Data
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    public static class Authentication {
        private String realm;
        private String userName;
    }

    @SneakyThrows
    public static WWWAuthenticateHeader generateChallenge(String realm) {
        WWWAuthenticateHeader proxyAuthenticate = SipBuilder.getHeaderFactory().createWWWAuthenticateHeader(DEFAULT_SCHEME);
        proxyAuthenticate.setParameter("realm", realm);
        proxyAuthenticate.setParameter("qop", "auth");
        proxyAuthenticate.setParameter("nonce", generateNonce());
        proxyAuthenticate.setParameter("algorithm", DEFAULT_ALGORITHM);

        return proxyAuthenticate;
    }

    public static void generateChallenge(Response response, String realm) {
        response.setHeader(generateChallenge(realm));
    }

    /**
     * Authenticate the inbound request.
     *
     * @param request - the request to authenticate.
     * @param hashedPassword -- the MD5 hashed string of username:realm:plaintext password.
     *
     * @return true if authentication succeded and false otherwise.
     */
    public static boolean doAuthenticateHashedPassword(Request request, String hashedPassword) {
        AuthorizationHeader authHeader = (AuthorizationHeader) request.getHeader(AuthorizationHeader.NAME);
        if ( authHeader == null ) {
            return false;
        }
        String realm = authHeader.getRealm();
        String username = authHeader.getUsername();

        if ( username == null || realm == null ) {
            return false;
        }

        String nonce = authHeader.getNonce();
        URI uri = authHeader.getURI();
        if (uri == null) {
            return false;
        }

        String A2 = request.getMethod().toUpperCase() + ":" + uri;
        String HA1 = hashedPassword;

        byte[] mdbytes = messageDigest.digest(A2.getBytes());
        String HA2 = toHexString(mdbytes);

        String cnonce = authHeader.getCNonce();
        String KD = HA1 + ":" + nonce;
        if (cnonce != null) {
            KD += ":" + cnonce;
        }
        KD += ":" + HA2;
        mdbytes = messageDigest.digest(KD.getBytes());
        String mdString = toHexString(mdbytes);
        String response = authHeader.getResponse();


        return mdString.equals(response);
    }

    /**
     * Authenticate the inbound request given plain text password.
     *
     * @param request - the request to authenticate.
     * @param password -- the plain text password.
     *
     * @return true if authentication succeded and false otherwise.
     */
    public static boolean doAuthenticatePlainTextPassword(Request request, String password) {
        AuthorizationHeader authorizationHeader = (AuthorizationHeader) request.getHeader(AuthorizationHeader.NAME);
        if ( authorizationHeader == null || authorizationHeader.getRealm() == null) {
            return false;
        }
        return doAuthenticatePlainTextPassword(request.getMethod().toUpperCase(),authorizationHeader,password);
    }

    public static boolean doAuthenticatePlainTextPassword(String method,AuthorizationHeader authorizationHeader, String password) {
        if ( authorizationHeader == null || authorizationHeader.getRealm() == null) {
            return false;
        }

        String realm = authorizationHeader.getRealm().trim();
        String username = authorizationHeader.getUsername().trim();

        if(ObjectUtils.anyNull(username,realm)){
            return false;
        }

        String nonce = authorizationHeader.getNonce();
        URI uri = authorizationHeader.getURI();
        if (uri == null) {
            return false;
        }
        // qop 保护质量 包含auth（默认的）和auth-int（增加了报文完整性检测）两种策略
        String qop = authorizationHeader.getQop();

        // 客户端随机数，这是一个不透明的字符串值，由客户端提供，并且客户端和服务器都会使用，以避免用明文文本。
        // 这使得双方都可以查验对方的身份，并对消息的完整性提供一些保护
        String cnonce = authorizationHeader.getCNonce();

        // nonce计数器，是一个16进制的数值，表示同一nonce下客户端发送出请求的数量
        int nc = authorizationHeader.getNonceCount();

        String ncStr = String.format("%08x", nc).toUpperCase();
        String A1 = String.join(":",username , realm , password);
        String A2 = String.join(":", method.toUpperCase() , uri.toString());

        byte[] mdbytes = messageDigest.digest(A1.getBytes());
        String HA1 = toHexString(mdbytes);
        log.debug("A1: " + A1);
        log.debug("A2: " + A2);
        mdbytes = messageDigest.digest(A2.getBytes());
        String HA2 = toHexString(mdbytes);
        log.debug("HA1: " + HA1);
        log.debug("HA2: " + HA2);
        // String cnonce = authorizationHeader.getCNonce();
        log.debug("nonce: " + nonce);
        log.debug("nc: " + ncStr);
        log.debug("cnonce: " + cnonce);
        log.debug("qop: " + qop);
        String KD = HA1 + ":" + nonce;

        if (qop != null && qop.equalsIgnoreCase("auth") ) {
            if (nc != -1) {
                KD += ":" + ncStr;
            }
            if (cnonce != null) {
                KD += ":" + cnonce;
            }
            KD += ":" + qop;
        }
        KD += ":" + HA2;
        log.debug("KD: " + KD);
        mdbytes = messageDigest.digest(KD.getBytes());
        String mdString = toHexString(mdbytes);
        log.debug("mdString: " + mdString);
        String response = authorizationHeader.getResponse();
        log.debug("response: " + response);
        return mdString.equals(response);
    }

    @SneakyThrows
    public static AuthorizationHeader createAuthorization(String serverIp, int serverPort, String serverId, String deviceId,String password, WWWAuthenticateHeader www){
        String hostAddress = SipBuilder.createHostAddress(serverIp, serverPort);
        SipURI sipURI = SipBuilder.createSipURI(serverId, hostAddress);
        if (www == null) {
            AuthorizationHeader authorizationHeader = SipBuilder.getHeaderFactory().createAuthorizationHeader("Digest");
            authorizationHeader.setUsername(deviceId);
            authorizationHeader.setURI(sipURI);
            authorizationHeader.setAlgorithm("MD5");
            return authorizationHeader;
        }
        String realm = www.getRealm();
        String nonce = www.getNonce();
        String scheme = www.getScheme();
        String qop = www.getQop();

        String cNonce = null;
        int nc = 1;
        String ncStr = String.format("%08x", nc).toUpperCase();
        if (qop != null) {
            if ("auth".equalsIgnoreCase(qop)) {
                // 客户端随机数，这是一个不透明的字符串值，由客户端提供，并且客户端和服务器都会使用，以避免用明文文本。
                // 这使得双方都可以查验对方的身份，并对消息的完整性提供一些保护
                cNonce = IdUtil.fastSimpleUUID();

            }else if ("auth-int".equalsIgnoreCase(qop)){
                // TODO
            }
        }
        String HA1 = DigestUtil.md5Hex((deviceId + ":" + realm + ":" + password).getBytes());
        String HA2= DigestUtil.md5Hex((Request.REGISTER + ":" + sipURI.toString()).getBytes());

        StringBuilder reStr = new StringBuilder();
        reStr.append(HA1);
        reStr.append(":");
        reStr.append(nonce);
        reStr.append(":");
        if (qop != null) {
            reStr.append(ncStr);
            reStr.append(":");
            reStr.append(cNonce);
            reStr.append(":");
            reStr.append(qop);
            reStr.append(":");
        }
        reStr.append(HA2);

        String response = DigestUtil.md5Hex(reStr.toString().getBytes());

        AuthorizationHeader authorizationHeader = SipFactory.getInstance().createHeaderFactory().createAuthorizationHeader(scheme);
        authorizationHeader.setUsername(deviceId);
        authorizationHeader.setRealm(realm);
        authorizationHeader.setNonce(nonce);
        authorizationHeader.setURI(sipURI);
        authorizationHeader.setResponse(response);
        authorizationHeader.setAlgorithm(DigestAlgorithm.MD5.name());
        if (qop != null) {
            authorizationHeader.setQop(qop);
            authorizationHeader.setCNonce(cNonce);
            authorizationHeader.setNonceCount(nc);
        }
        return authorizationHeader;
    }
}
