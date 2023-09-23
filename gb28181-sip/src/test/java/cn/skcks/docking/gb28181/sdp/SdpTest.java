package cn.skcks.docking.gb28181.sdp;

import gov.nist.javax.sdp.SessionDescriptionImpl;
import gov.nist.javax.sdp.fields.TimeField;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;

import javax.sdp.SdpFactory;
import javax.sdp.TimeDescription;
import java.util.Vector;

@Slf4j
public class SdpTest {
    @Test
    @SneakyThrows
    public void test(){
        String domain = "4405010000";
        TimeField timeField = new TimeField();
        timeField.setZero();
        TimeDescription timeDescription = SdpFactory.getInstance().createTimeDescription(timeField);
        GB28181Description gb28181Description = new GB28181Description();
        gb28181Description.setTimeDescriptions(new Vector<>() {{
            add(timeDescription);
        }});
        gb28181Description.setSsrcField(new SsrcField(String.format("%s%04d", domain.substring(3, 8), 1)));
        log.info("gb28181 sdp \n{}", gb28181Description);

        GB28181DescriptionParser gb28181DescriptionParser = new GB28181DescriptionParser(gb28181Description.toString());
        GB28181Description parse = gb28181DescriptionParser.parse();
        log.info("gb28181 sdp 解析\n{}",parse);

        SessionDescriptionImpl sessionDescription = gb28181Description;
        sessionDescription.setTimeDescriptions(new Vector<>() {{
            add(timeDescription);
        }});
        gb28181Description = new GB28181Description(sessionDescription);
        gb28181Description.setTimeDescriptions(new Vector<>() {{
            add(timeDescription);
        }});
        log.info("从 SessionDescriptionImpl 转换为 gb28181 sdp \n{}", gb28181Description);
        gb28181DescriptionParser = new GB28181DescriptionParser(gb28181Description.toString());
        parse = gb28181DescriptionParser.parse();
        log.info("从 SessionDescriptionImpl 转换为 gb28181 sdp 解析\n{}",parse);
    }
}
