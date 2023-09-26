package cn.skcks.docking.gb28181.sip.process;

import cn.skcks.docking.gb28181.sip.generic.SipBuilder;
import cn.skcks.docking.gb28181.sip.parser.GbStringMsgParserFactory;
import cn.skcks.docking.gb28181.sip.property.DefaultProperties;
import cn.skcks.docking.gb28181.sip.request.RegisterRequestBuilder;
import cn.skcks.docking.gb28181.sip.utils.SipUtil;
import gov.nist.javax.sip.SipStackImpl;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;

import javax.sip.ListeningPoint;
import javax.sip.SipProvider;
import javax.sip.header.UserAgentHeader;
import javax.sip.message.Request;

@Slf4j
public class RequestTest {
    String localIp = "127.0.0.1";
    int localPort = 5060;
    String localId = "00000000000000000001";

    String remoteIp = "10.10.10.200";
    int remotePort = 5060;
    String remoteId = "00000000000000000002";

    @Test
    @SneakyThrows
    void test(){
        SipUtil.setUserAgentVersion("0.1.0");
        UserAgentHeader userAgentHeader = SipBuilder.userAgentHeader;
        log.info("\n{}",userAgentHeader);

        SipStackImpl sipStack = (SipStackImpl) SipBuilder.getSipFactory()
                .createSipStack(DefaultProperties.getProperties(
                        SipUtil.UserAgent,
                        "cn.skcks.docking.gb28181.sip.logger.StackLoggerImpl",
                        "cn.skcks.docking.gb28181.sip.logger.ServerLoggerImpl"));
        sipStack.setMessageParserFactory(new GbStringMsgParserFactory());
        ListeningPoint listeningPoint = sipStack.createListeningPoint("127.0.0.1", 5060, ListeningPoint.UDP);
        SipProvider sipProvider = sipStack.createSipProvider(listeningPoint);
        String callId = sipProvider.getNewCallId().getCallId();

        RegisterRequestBuilder registerRequestBuilder = new RegisterRequestBuilder(localIp, localPort, localId, remoteIp, remotePort, remoteId, ListeningPoint.UDP);
        Request noAuthorizationRequest = registerRequestBuilder.createNoAuthorizationRequest(callId);
        log.info("\n{}",noAuthorizationRequest);
        sipStack.deleteSipProvider(sipProvider);
        sipStack.deleteListeningPoint(listeningPoint);
    }
}
