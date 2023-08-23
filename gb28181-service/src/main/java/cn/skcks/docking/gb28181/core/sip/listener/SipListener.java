package cn.skcks.docking.gb28181.core.sip.listener;

import cn.skcks.docking.gb28181.core.sip.message.processor.MessageProcessor;

public interface SipListener extends javax.sip.SipListener {
    void addRequestProcessor(String method, MessageProcessor messageProcessor);
    void addResponseProcessor(String method, MessageProcessor messageProcessor);
}
