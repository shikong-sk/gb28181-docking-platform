package cn.skcks.docking.gb28181.core.sip.message.processor.request;

import cn.skcks.docking.gb28181.core.sip.listener.SipListener;
import cn.skcks.docking.gb28181.core.sip.message.processor.MessageProcessor;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.sip.RequestEvent;

@Slf4j
@RequiredArgsConstructor
@Component
public class RegisterRequestProcessor implements MessageProcessor {
    private final static String METHOD = "REGISTER";

    private final SipListener sipListener;

    @PostConstruct
    private void init(){
        sipListener.addProcessor(METHOD,this);
    }

    @Override
    public void process(RequestEvent requestEvent) {

    }
}
