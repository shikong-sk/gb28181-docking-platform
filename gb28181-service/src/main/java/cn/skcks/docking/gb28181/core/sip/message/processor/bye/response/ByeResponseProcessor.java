package cn.skcks.docking.gb28181.core.sip.message.processor.bye.response;

import cn.skcks.docking.gb28181.core.sip.listener.SipListener;
import cn.skcks.docking.gb28181.core.sip.message.processor.MessageProcessor;
import cn.skcks.docking.gb28181.core.sip.message.subscribe.GenericSubscribe;
import cn.skcks.docking.gb28181.core.sip.message.subscribe.SipSubscribe;
import cn.skcks.docking.gb28181.core.sip.service.SipService;
import cn.skcks.docking.gb28181.sip.generic.SipResponseBuilder;
import cn.skcks.docking.gb28181.sip.method.invite.response.InviteResponseBuilder;
import gov.nist.javax.sip.message.SIPRequest;
import gov.nist.javax.sip.message.SIPResponse;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.sip.RequestEvent;
import javax.sip.SipException;
import javax.sip.SipProvider;
import javax.sip.message.Request;
import javax.sip.message.Response;
import java.util.EventObject;
import java.util.Optional;

@Slf4j
@RequiredArgsConstructor
@Component
public class ByeResponseProcessor implements MessageProcessor {
    private final SipListener sipListener;

    private final SipSubscribe subscribe;

    private final SipService sipService;

    @PostConstruct
    @Override
    public void init() {
        sipListener.addResponseProcessor(Request.BYE, this);
    }

    @Override
    public void process(EventObject eventObject) {
    }
}
