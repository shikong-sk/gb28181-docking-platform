package cn.skcks.docking.gb28181.core.sip.message.processor;

import javax.sip.RequestEvent;

public interface MessageProcessor {
    void process(RequestEvent requestEvent);
}
