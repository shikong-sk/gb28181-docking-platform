package cn.skcks.docking.gb28181.sdp;

import cn.skcks.docking.gb28181.sdp.field.ssrc.SsrcField;
import cn.skcks.docking.gb28181.sdp.media.MediaStreamMode;
import cn.skcks.docking.gb28181.sdp.parser.GB28181DescriptionParser;
import gov.nist.javax.sdp.SessionDescriptionImpl;
import gov.nist.javax.sdp.fields.TimeField;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;

import javax.sdp.Connection;
import javax.sdp.SdpFactory;
import javax.sdp.TimeDescription;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
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
        String ssrc = String.format("%s%04d", domain.substring(3, 8), 1);
        gb28181Description.setSsrcField(new SsrcField(ssrc));
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

        String deviceId = "00000000000000000002";
        String channelId = "00000000000000000002";
        String rtpNetType = Connection.IP4;
        String rtpIp = "10.10.10.200";
        int rtpPort = 5080;
        String sendRtpIp = "127.0.0.1";
        int sendRtpPort = 5080;

        GB28181Description play = GB28181SDPBuilder.Receiver.play(deviceId, channelId, rtpNetType, rtpIp, rtpPort, ssrc, MediaStreamMode.TCP_ACTIVE);
        log.info("sdp play 请求\n{}",play);

        play = GB28181SDPBuilder.Receiver.play(deviceId, channelId, rtpNetType, rtpIp, rtpPort, ssrc, MediaStreamMode.TCP_ACTIVE, GB28181SDPBuilder.StreamType.GB2022.MAIN);
        log.info("sdp play 请求\n{}",play);

        final String psType = "96";
        Map<String,String> respRtpMap = new HashMap<>(){{
            put(psType, GB28181SDPBuilder.RTPMAP.get(psType));
        }};

        GB28181Description resp = GB28181SDPBuilder.Sender.build(play, sendRtpIp, sendRtpPort,respRtpMap,null);
        log.info("sdp 响应\n{}", resp);
    }

    @Test
    public void streamType() {
        log.info("{}", GB28181SDPBuilder.StreamType.getAttribute(GB28181SDPBuilder.StreamType.TPLink.MAIN));
        log.info("{}", GB28181SDPBuilder.StreamType.getAttribute(GB28181SDPBuilder.StreamType.TPLink.SUB));

        log.info("{}", GB28181SDPBuilder.StreamType.getAttribute(GB28181SDPBuilder.StreamType.GB2022.MAIN));
        log.info("{}", GB28181SDPBuilder.StreamType.getAttribute(GB28181SDPBuilder.StreamType.GB2022.SUB));
        log.info("{}", GB28181SDPBuilder.StreamType.getAttribute(new GB28181SDPBuilder.StreamType.GB2022(5)));
    }
}
