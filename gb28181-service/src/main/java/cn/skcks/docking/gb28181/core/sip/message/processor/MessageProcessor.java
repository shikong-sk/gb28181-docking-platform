package cn.skcks.docking.gb28181.core.sip.message.processor;

import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.sip.PeerUnavailableException;
import javax.sip.RequestEvent;
import javax.sip.SipFactory;
import javax.sip.header.HeaderFactory;
import javax.sip.message.MessageFactory;

public interface MessageProcessor {
    Logger log = LoggerFactory.getLogger(MessageProcessor.class);

    class Method {
        public static final String REGISTER = "REGISTER";
    }

    void process(RequestEvent requestEvent);

    default MessageFactory getMessageFactory() {
        try {
            return SipFactory.getInstance().createMessageFactory();
        } catch (PeerUnavailableException e) {
            log.error("未处理的异常 ", e);
        }
        return null;
    }

    default HeaderFactory getHeaderFactory() {
        try {
            return SipFactory.getInstance().createHeaderFactory();
        } catch (PeerUnavailableException e) {
            log.error("未处理的异常 ", e);
        }
        return null;
    }
}
