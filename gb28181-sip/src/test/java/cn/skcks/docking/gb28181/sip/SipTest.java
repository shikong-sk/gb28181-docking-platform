package cn.skcks.docking.gb28181.sip;

import cn.skcks.docking.gb28181.sip.generic.SipBuilder;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;

import javax.sip.address.Address;
import javax.sip.address.SipURI;

@Slf4j
public class SipTest {
    @Test
    public void test(){
        String localIp = "127.0.0.1";
        int localPort = 5060;
        String localId = "12345678901234567890";
        String localHostAddress = SipBuilder.createHostAddress(localIp,localPort);
        SipURI localSipUri = SipBuilder.createSipURI(localId, localHostAddress);

        Address localAddress = SipBuilder.createAddress(localSipUri);
        log.info("{}", localAddress);
    }
}
