package cn.skcks.docking.gb28181.sip;

import cn.hutool.core.date.DatePattern;
import cn.hutool.core.date.DateUtil;
import cn.skcks.docking.gb28181.constant.GB28181Constant;
import cn.skcks.docking.gb28181.sip.generic.SipBuilder;
import cn.skcks.docking.gb28181.sip.header.GBDateHeader;
import cn.skcks.docking.gb28181.sip.header.XGBVerHeader;
import cn.skcks.docking.gb28181.sip.header.impl.XGBVerHeaderImpl;
import cn.skcks.docking.gb28181.sip.generic.SipRequestBuilder;
import cn.skcks.docking.gb28181.sip.generic.SipResponseBuilder;
import cn.skcks.docking.gb28181.sip.parser.GbStringMsgParserFactory;
import cn.skcks.docking.gb28181.sip.property.DefaultProperties;
import cn.skcks.docking.gb28181.sip.utils.SipUtil;
import gov.nist.javax.sip.SipStackImpl;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;

import javax.sip.ListeningPoint;
import javax.sip.SipProvider;
import javax.sip.address.Address;
import javax.sip.address.SipURI;
import javax.sip.header.*;
import javax.sip.message.Request;
import javax.sip.message.Response;
import java.text.MessageFormat;
import java.util.Calendar;
import java.util.List;

@Slf4j
public class SipTest {
    @Test
    @SneakyThrows
    public void stackTest(){
        // 创建 sip
        SipStackImpl sipStack = (SipStackImpl) SipBuilder.getSipFactory()
                .createSipStack(DefaultProperties.getProperties(
                        SipUtil.UserAgent,
                        "cn.skcks.docking.gb28181.sip.logger.StackLoggerImpl",
                        "cn.skcks.docking.gb28181.sip.logger.ServerLoggerImpl"));
        sipStack.setMessageParserFactory(new GbStringMsgParserFactory());
        ListeningPoint listeningPoint = sipStack.createListeningPoint("127.0.0.1", 5060, ListeningPoint.UDP);
        SipProvider sipProvider = sipStack.createSipProvider(listeningPoint);
        // 各种操作...
        Thread.sleep(1000);
        // 关闭 sip
        sipStack.deleteSipProvider(sipProvider);
        sipStack.deleteListeningPoint(listeningPoint);
    }

    @Test
    public void test() {
        SipUtil.setUserAgent("GB28181-Docking-Platform Beta");
        SipUtil.setUserAgentVersion("0.1.0");
        // 发起方
        String localIp = "127.0.0.1";
        int localPort = 5060;
        String localId = "00000000000000000001";

        String localHostAddress = SipBuilder.createHostAddress(localIp, localPort);
        SipURI localSipUri = SipBuilder.createSipURI(localId, localHostAddress);
        Address localAddress = SipBuilder.createAddress(localSipUri);
        FromHeader fromHeader = SipBuilder.createFromHeader(localAddress, "000001");
        log.info("{}", localAddress);

        // 接收方
        String remoteIp = "10.10.10.200";
        int remotePort = 5060;
        String remoteId = "00000000000000000002";

        String remoteHostAddress = SipBuilder.createHostAddress(remoteIp, remotePort);
        SipURI remoteSipUri = SipBuilder.createSipURI(remoteId, remoteHostAddress);
        Address remoteAddress = SipBuilder.createAddress(remoteSipUri);
        ToHeader toHeader = SipBuilder.createToHeader(remoteAddress, null);
        log.info("{}", remoteAddress);

        // 目的地
        ContactHeader contactHeader = SipBuilder.createContactHeader(remoteAddress);

        // 公共部分
        String method = Request.REGISTER;
        CSeqHeader cSeqHeader = SipBuilder.createCSeqHeader(1, method);
        // CallId @ 后面可以是地址也可以为 域 id
        CallIdHeader callIdHeader = SipBuilder.createCallIdHeader(MessageFormat.format("{0}@{1}", "123456", localIp));
        List<ViaHeader> viaHeaders = SipBuilder.createViaHeaders(localIp, localPort, GB28181Constant.TransPort.TCP, "z9hG4bK" + "0000000001");
        MaxForwardsHeader maxForwardsHeader = SipBuilder.createMaxForwardsHeader(70);
        ExpiresHeader expiresHeader = SipBuilder.createExpiresHeader(3600);

        // 创建请求
        Request request = SipRequestBuilder.createRequest(remoteSipUri, method, callIdHeader, cSeqHeader, fromHeader, toHeader, viaHeaders, maxForwardsHeader);
        request.addHeader(contactHeader);
        request.addHeader(expiresHeader);
        request.addHeader(XGBVerHeaderImpl.GB28181_2016);
        request.addHeader(new GBDateHeader(Calendar.getInstance()));

        log.info("构造请求\n{}", request);
        XGBVerHeaderImpl xgbVerHeader = (XGBVerHeaderImpl) request.getHeader(XGBVerHeader.NAME);
        log.info("协议版本 {}", xgbVerHeader);
        GBDateHeader gbDateHeader = (GBDateHeader) request.getHeader(GBDateHeader.NAME);
        log.info("服务器时间 {}", gbDateHeader);

        // 创建响应
        Response response = SipResponseBuilder.createResponse(Response.OK, request);
        response.addHeader(XGBVerHeaderImpl.GB28181_2016);
        log.info("构造响应\n{}", response);
    }
}
