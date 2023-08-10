package cn.skcks.docking.gb28181.core.sip.listener;

import cn.skcks.docking.gb28181.core.sip.message.processor.MessageProcessor;

public interface SipListener extends javax.sip.SipListener {
    void addProcessor(String method, MessageProcessor messageProcessor);
}
