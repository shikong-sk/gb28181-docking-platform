package cn.skcks.docking.gb28181;

import cn.skcks.docking.gb28181.core.sip.service.SipService;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import javax.sip.SipProvider;

@Slf4j
@ExtendWith(SpringExtension.class)
@AutoConfigureMockMvc
@SpringBootTest(classes = Gb28181DockingPlatformApplicationTest.class)
public class SipTest {
    @Autowired
    private SipService service;

    @Test
    void context(){
        SipProvider provider = service.getProvider("TCP", "192.168.10.195");
        log.info("{}", provider);
    }
}
