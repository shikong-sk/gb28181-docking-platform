package cn.skcks.docking.gb28181.core.sip.message.processor.message.response;

import cn.skcks.docking.gb28181.core.sip.gb28181.sdp.Gb28181Sdp;
import cn.skcks.docking.gb28181.core.sip.listener.SipListener;
import cn.skcks.docking.gb28181.core.sip.message.processor.MessageProcessor;
import cn.skcks.docking.gb28181.core.sip.message.request.SipRequestBuilder;
import cn.skcks.docking.gb28181.core.sip.message.sender.SipMessageSender;
import cn.skcks.docking.gb28181.core.sip.utils.SipUtil;
import gov.nist.javax.sip.ResponseEventExt;
import gov.nist.javax.sip.message.SIPResponse;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.sdp.SdpParseException;
import javax.sdp.SessionDescription;
import javax.sip.*;
import javax.sip.address.SipURI;
import javax.sip.message.Request;
import javax.sip.message.Response;
import java.text.ParseException;
import java.util.EventObject;

@Slf4j
@Component
@RequiredArgsConstructor
public class InviteResponseProcessor implements MessageProcessor {
    private final SipListener sipListener;
    private final SipMessageSender sender;

    @PostConstruct
    @Override
    public void init() {
        sipListener.addResponseProcessor(Method.INVITE, this);
    }

    public void process(EventObject eventObject) {
        ResponseEvent requestEvent = (ResponseEvent) eventObject;
        try {
            SIPResponse response = (SIPResponse) requestEvent.getResponse();
            int statusCode = response.getStatusCode();
            // trying不会回复
            if (statusCode == Response.TRYING) {
                return;
            }
            // 成功响应
            // 下发ack
            if (statusCode == Response.OK) {
                ResponseEventExt event = (ResponseEventExt) requestEvent;

                String contentString = new String(response.getRawContent());
                Gb28181Sdp gb28181Sdp = SipUtil.parseSDP(contentString);
                SessionDescription sdp = gb28181Sdp.getBaseSdb();
                SipURI requestUri = SipFactory.getInstance().createAddressFactory().createSipURI(sdp.getOrigin().getUsername(), event.getRemoteIpAddress() + ":" + event.getRemotePort());
                Request reqAck = SipRequestBuilder.createAckRequest(response.getLocalAddress().getHostAddress(), requestUri, response);

                log.info("[回复ack] {}-> {}:{} ", sdp.getOrigin().getUsername(), event.getRemoteIpAddress(), event.getRemotePort());
                log.debug("{}", reqAck);
                sender.send(response.getLocalAddress().getHostAddress(), reqAck);
            }
        } catch (InvalidArgumentException | ParseException | SipException | SdpParseException e) {
            log.info("[点播回复ACK]，异常：", e);
        }
    }
}
