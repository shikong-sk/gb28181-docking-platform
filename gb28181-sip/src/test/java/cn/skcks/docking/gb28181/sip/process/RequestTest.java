package cn.skcks.docking.gb28181.sip.process;

import cn.skcks.docking.gb28181.sip.generic.SipBuilder;
import cn.skcks.docking.gb28181.sip.utils.SipUtil;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;

import javax.sip.header.UserAgentHeader;

@Slf4j
public class RequestTest {
    @Test
    void test(){
        SipUtil.setUserAgentVersion("0.1.0");
        UserAgentHeader userAgentHeader = SipBuilder.userAgentHeader;
        log.info("\n{}",userAgentHeader);
    }
}
