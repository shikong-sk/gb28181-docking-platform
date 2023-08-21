package cn.skcks.docking.gb28181;

import cn.skcks.docking.gb28181.core.sip.service.SipService;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import javax.sip.SipProvider;

@Slf4j
@SpringBootTest(classes = Gb28181DockingPlatformApplicationTest.class)
public class SipTest {
    @Autowired
    private SipService service;

    @Test
    void context(){
        SipProvider provider = service.getProvider("TCP", "10.10.10.20");
        log.info("{}", provider);
    }
}
