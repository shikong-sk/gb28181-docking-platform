package cn.skcks.docking.gb28181.sip.process;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.IdUtil;
import cn.hutool.core.util.RandomUtil;
import cn.skcks.docking.gb28181.constant.CmdType;
import cn.skcks.docking.gb28181.sdp.media.MediaStreamMode;
import cn.skcks.docking.gb28181.sip.generic.SipBuilder;
import cn.skcks.docking.gb28181.sip.manscdp.catalog.query.CatalogQueryDTO;
import cn.skcks.docking.gb28181.sip.manscdp.catalog.response.CatalogDeviceListDTO;
import cn.skcks.docking.gb28181.sip.manscdp.catalog.response.CatalogResponseDTO;
import cn.skcks.docking.gb28181.sip.manscdp.catalog.response.CatalogSubscribeResponseDTO;
import cn.skcks.docking.gb28181.sip.manscdp.keepalive.notify.KeepaliveNotifyDTO;
import cn.skcks.docking.gb28181.sip.method.invite.request.InviteRequestBuilder;
import cn.skcks.docking.gb28181.sip.method.invite.response.InviteResponseBuilder;
import cn.skcks.docking.gb28181.sip.method.message.request.MessageRequestBuilder;
import cn.skcks.docking.gb28181.sip.method.message.response.MessageResponseBuilder;
import cn.skcks.docking.gb28181.sip.method.notify.request.NotifyRequestBuilder;
import cn.skcks.docking.gb28181.sip.method.register.request.RegisterRequestBuilder;
import cn.skcks.docking.gb28181.sip.method.register.response.RegisterResponseBuilder;
import cn.skcks.docking.gb28181.sip.method.subscribe.request.SubscribeRequestBuilder;
import cn.skcks.docking.gb28181.sip.method.subscribe.response.SubscribeResponseBuilder;
import cn.skcks.docking.gb28181.sip.utils.MANSCDPUtils;
import cn.skcks.docking.gb28181.sip.utils.SipUtil;
import gov.nist.javax.sip.message.SIPRequest;
import gov.nist.javax.sip.message.SIPResponse;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;

import javax.sip.ListeningPoint;
import javax.sip.header.WWWAuthenticateHeader;
import javax.sip.message.Request;
import javax.sip.message.Response;
import java.util.Collections;
import java.util.Date;

@Slf4j
public class RequestTest {
    String localIp = "127.0.0.1";
    int localPort = 5060;
    String localId = "00000000000000000001";

    String remoteIp = "10.10.10.200";
    int remotePort = 5060;
    String remoteId = "00000000000000000002";

    public static final String domain = "4405010000";

    public static final String receiveRtp = "10.10.10.200";
    public static int receiveRtpPort = RandomUtil.randomInt(30000,40000);

    public static final String senderRtp = "10.10.10.200";
    public static int senderRtpPort = RandomUtil.randomInt(40000,50000);

    @Test
    void inviteTest(){
        // 服务端 向 客户端 发起 请求
        InviteRequestBuilder inviteRequestBuilder = InviteRequestBuilder.builder()
                .localIp(remoteIp)
                .localPort(remotePort)
                .localId(remoteId)
                .targetIp(localIp)
                .targetPort(localPort)
                .targetId(localId)
                .transport(ListeningPoint.TCP)
                .build();
        String callId = SipUtil.nanoId(10);
        // 2~6位为 域 4~8位
        String ssrcPrefix = domain.substring(3, 8);
        // 7~10 为 流标识
        String ssrc = String.format("%s%04d", ssrcPrefix, RandomUtil.randomInt(1,10000));
        // 0 开头的为实时
        String playSsrc = "0" + ssrc;
        // 1 开头的为历史
        String playBackSsrc = "1" + ssrc;

        InviteResponseBuilder inviteResponseBuilder = InviteResponseBuilder.builder().build();
        // 实时点播请求
        Request playInviteRequest = inviteRequestBuilder.createPlayInviteRequest(callId, 1, localId, receiveRtp, receiveRtpPort, playSsrc, MediaStreamMode.TCP_ACTIVE);
        log.info("\n{}", playInviteRequest);
        Response inviteResponse = inviteResponseBuilder.createInviteResponse(playInviteRequest, senderRtp, senderRtpPort, SipUtil.nanoId());
        log.info("\n{}", inviteResponse);

        Date now = DateUtil.date();
        Date startTime = DateUtil.beginOfDay(DateUtil.offsetDay(now,-1));
        Date endTime = DateUtil.endOfDay(DateUtil.offsetDay(now,-1));
        // 回放请求
        Request playbackInviteRequest = inviteRequestBuilder.createPlaybackInviteRequest(callId, 1, localId, receiveRtp, receiveRtpPort, playBackSsrc, MediaStreamMode.TCP_ACTIVE,startTime,endTime);
        log.info("\n{}", playbackInviteRequest);
        inviteResponse = inviteResponseBuilder.createInviteResponse(playbackInviteRequest, senderRtp, senderRtpPort, SipUtil.nanoId());
        log.info("\n{}", inviteResponse);

        // 下载请求
        Request downloadInviteRequest = inviteRequestBuilder.createDownloadInviteRequest(callId, 1, localId, receiveRtp, receiveRtpPort, playBackSsrc, MediaStreamMode.TCP_ACTIVE,startTime,endTime,4.0);
        log.info("\n{}", downloadInviteRequest);
        Response tryingInviteResponse = inviteResponseBuilder.createTryingInviteResponse(downloadInviteRequest);
        log.info("\n{}", tryingInviteResponse);
        inviteResponse = inviteResponseBuilder.createInviteResponse(downloadInviteRequest, senderRtp, senderRtpPort, SipUtil.nanoId());
        log.info("\n{}", inviteResponse);

        Request byeRequest = inviteRequestBuilder.createByeRequest(callId, 2);
        log.info("\n{}", byeRequest);
        Response byeResponse = inviteResponseBuilder.createByeResponse(byeRequest, SipUtil.nanoId());
        log.info("\n{}", byeResponse);
    }


    @Test
    void messageTest(){
        MessageRequestBuilder messageRequestBuilder = MessageRequestBuilder.builder()
                .localIp(localIp)
                .localPort(localPort)
                .localId(localId)
                .targetIp(remoteIp)
                .targetPort(remotePort)
                .targetId(remoteId)
                .transport(ListeningPoint.TCP)
                .build();
        String callId = IdUtil.fastSimpleUUID();
        Request messageRequest = messageRequestBuilder.createMessageRequest(callId, 1, new byte[]{});
        log.info("\n{}", messageRequest);

        MessageResponseBuilder messageResponseBuilder = MessageResponseBuilder.builder().build();
        Response messageResponse = messageResponseBuilder.createMessageResponse(messageRequest,  new byte[]{}, SipUtil.nanoId());
        log.info("\n{}", messageResponse);
    }

    @Test
    @SneakyThrows
    void subscribeTest(){
        // 服务端 向 客户端 发起订阅
        SubscribeRequestBuilder subscribeRequestBuilder = SubscribeRequestBuilder.builder()
                .localIp(remoteIp)
                .localPort(remotePort)
                .localId(remoteId)
                .targetIp(localIp)
                .targetPort(localPort)
                .targetId(localId)
                .transport(ListeningPoint.TCP)
                .build();
        String callId = SipUtil.nanoId(10);
        CatalogQueryDTO catalogQueryDTO = CatalogQueryDTO.builder()
                .deviceId(localId)
                .sn(String.valueOf(1))
                .build();

        Request subscribeRequest = subscribeRequestBuilder.createSubscribeRequest(callId,
                1, catalogQueryDTO.getCmdType(), MANSCDPUtils.toByteXml(catalogQueryDTO),90);
        log.info("\n{}",subscribeRequest);

        catalogQueryDTO = MANSCDPUtils.parse(subscribeRequest.getRawContent(), CatalogQueryDTO.class);
        CatalogSubscribeResponseDTO catalogSubscribeResponseDTO = CatalogSubscribeResponseDTO.builder()
                .sn(catalogQueryDTO.getSn())
                .deviceId(catalogQueryDTO.getDeviceId())
                .build();
        SubscribeResponseBuilder subscribeResponseBuilder = SubscribeResponseBuilder.builder().build();
        Response subscribeResponse = subscribeResponseBuilder.createSubscribeResponse(subscribeRequest, MANSCDPUtils.toByteXml(catalogSubscribeResponseDTO));
        log.info("\n{}",subscribeResponse);

        // 客户端向服务端发送 事件通知
        NotifyRequestBuilder notifyRequestBuilder = NotifyRequestBuilder.builder()
                .localIp(localIp)
                .localPort(localPort)
                .localId(localId)
                .targetIp(remoteIp)
                .targetPort(remotePort)
                .targetId(remoteId)
                .transport(ListeningPoint.TCP)
                .build();

        KeepaliveNotifyDTO keepaliveNotifyDTO = KeepaliveNotifyDTO.builder()
                .sn(String.valueOf(2))
                .deviceId(localId)
                .build();
        Request notifyRequest = notifyRequestBuilder.createNotifyRequest(callId, 2, CmdType.KEEPALIVE, MANSCDPUtils.toByteXml(keepaliveNotifyDTO));
        log.info("\n{}", notifyRequest);

        CatalogDeviceListDTO catalogDeviceListDTO = CatalogDeviceListDTO.builder()
                .deviceList(Collections.emptyList())
                .build();
        CatalogResponseDTO catalogResponseDTO = CatalogResponseDTO.builder()
                .sn(String.valueOf(2))
                .deviceId(localId)
                .sumNum(0L)
                .deviceList(catalogDeviceListDTO)
                .build();

        String toTag = ((SIPRequest)subscribeRequest).getFromTag();
        int expires = subscribeRequest.getExpires().getExpires();
        Request catalogNotifyRequest = notifyRequestBuilder.createNotifyRequest(callId, 3, CmdType.CATALOG, MANSCDPUtils.toByteXml(catalogResponseDTO), toTag,expires);
        log.info("\n{}", catalogNotifyRequest);
    }

    @Test
    @SneakyThrows
    void registerTest() {
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
        long cSeq = sipResponse.getCSeq().getSeqNumber() + 1;
        // 重新发起带有认证信息的请求
        Request authorizationRequest = registerRequestBuilder.createAuthorizationRequest(callId, 3600, localId, "123456", cSeq, wwwAuthenticateHeader);
        log.info("\n{}", authorizationRequest);
        authorzatioinResponse = registerResponseBuilder.createAuthorzatioinResponse(authorizationRequest, domain, "123456");
        // 注册成功
        log.info("\n{}", authorzatioinResponse);
    }
}
