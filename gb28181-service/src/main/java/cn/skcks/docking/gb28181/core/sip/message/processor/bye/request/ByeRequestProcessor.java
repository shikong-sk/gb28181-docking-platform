package cn.skcks.docking.gb28181.core.sip.message.processor.bye.request;

import cn.skcks.docking.gb28181.core.sip.listener.SipListener;
import cn.skcks.docking.gb28181.core.sip.message.processor.MessageProcessor;
import cn.skcks.docking.gb28181.core.sip.message.subscribe.GenericSubscribe;
import cn.skcks.docking.gb28181.core.sip.message.subscribe.SipSubscribe;
import cn.skcks.docking.gb28181.core.sip.service.SipService;
import cn.skcks.docking.gb28181.sip.method.invite.response.InviteResponseBuilder;
import gov.nist.javax.sip.message.SIPRequest;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.sip.RequestEvent;
import javax.sip.SipException;
import javax.sip.SipProvider;
import javax.sip.message.Request;
import java.util.EventObject;
import java.util.Optional;

@Slf4j
@RequiredArgsConstructor
@Component
public class ByeRequestProcessor implements MessageProcessor {
    private final SipListener sipListener;

    private final SipSubscribe subscribe;

    private final SipService sipService;

    @PostConstruct
    @Override
    public void init() {
        sipListener.addRequestProcessor(Request.BYE, this);
    }

    @Override
    public void process(EventObject eventObject) {
        RequestEvent requestEvent = (RequestEvent) eventObject;
        SIPRequest request = (SIPRequest) requestEvent.getRequest();
        String callId = request.getCallId().getCallId();
        String key = GenericSubscribe.Helper.getKey(Request.BYE, callId);
        log.info("key {}", key);
        String ip = request.getLocalAddress().getHostAddress();
        String transport = request.getTopmostViaHeader().getTransport();
        SipProvider provider= sipService.getProvider(transport, ip);
        Optional.ofNullable(subscribe.getSipRequestSubscribe().getPublisher(key))
                .ifPresentOrElse(
                        publisher -> publisher.submit(request),
                        () -> {
                            try {
                                provider.sendResponse(InviteResponseBuilder.builder().build().createTryingInviteResponse(request));
                            } catch (SipException e) {
                                throw new RuntimeException(e);
                            }
                        });
    }
}
