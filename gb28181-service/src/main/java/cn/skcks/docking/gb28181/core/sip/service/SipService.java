package cn.skcks.docking.gb28181.core.sip.service;

import javax.sip.SipProvider;

public interface SipService {
    void run();
    void stop();

    SipProvider getProvider(String transport, String ip);
}
