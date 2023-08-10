package cn.skcks.docking.gb28181.core.sip.message.sender;

import cn.skcks.docking.gb28181.core.sip.service.SipService;
import cn.skcks.docking.gb28181.core.sip.utils.SipUtil;
import gov.nist.javax.sip.message.SIPMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.ObjectUtils;
import org.springframework.stereotype.Component;

import javax.sip.ListeningPoint;
import javax.sip.PeerUnavailableException;
import javax.sip.SipException;
import javax.sip.SipProvider;
import javax.sip.header.CallIdHeader;
import javax.sip.header.UserAgentHeader;
import javax.sip.header.ViaHeader;
import javax.sip.message.Message;
import javax.sip.message.Request;
import javax.sip.message.Response;
import java.text.ParseException;
import java.util.Optional;

@Slf4j
@RequiredArgsConstructor
@Component
public class SipMessageSender {
    private final SipService sipService;

    public void send(String ip, Message message) {
        SIPMessage sipMessage = (SIPMessage)message;
        ViaHeader viaHeader = sipMessage.getTopmostViaHeader();
        String transport;
        if(ObjectUtils.anyNull(viaHeader)){
            transport = ListeningPoint.UDP;
            log.warn("缺少信息头 ViaHeader, 默认使用 {} 连接发送信息",transport);
        } else {
            transport = viaHeader.getTransport();
        }
        log.debug("将使用 {} 连接发送消息", transport);

        if (message.getHeader(UserAgentHeader.NAME) == null) {
            try {
                message.addHeader(SipUtil.createUserAgentHeader());
            } catch (PeerUnavailableException | ParseException e) {
               log.error("UserAgent 添加失败");
            }
        }

        CallIdHeader callIdHeader = sipMessage.getCallIdHeader();
        SipProvider sipProvider = sipService.getProvider(transport, ip);
        Optional.ofNullable(sipProvider).ifPresentOrElse(provider->{
            try {
                if (message instanceof Request) {
                    provider.sendRequest((Request) message);
                } else if (message instanceof Response) {
                    provider.sendResponse((Response) message);
                }
            } catch (SipException e) {
                log.error("消息发送失败");
            }
        },()-> log.error("未能找到 {}://{} 监听信息", transport,ip));
    }
}
