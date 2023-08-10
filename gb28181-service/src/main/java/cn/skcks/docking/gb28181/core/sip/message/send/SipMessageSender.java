package cn.skcks.docking.gb28181.core.sip.message.send;

import cn.skcks.docking.gb28181.core.sip.service.SipService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@RequiredArgsConstructor
@Component
public class SipMessageSender {
    private final SipService sipService;
}
