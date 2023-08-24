package cn.skcks.docking.gb28181.core.sip.gb28181.sdp;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import gov.nist.core.Separators;
import gov.nist.javax.sdp.fields.AttributeField;
import gov.nist.javax.sdp.fields.ConnectionField;
import gov.nist.javax.sdp.fields.TimeField;
import gov.nist.javax.sdp.fields.URIField;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;

import javax.sdp.*;
import java.util.*;

@Slf4j
public class MediaSdpHelper {
    public final static String SEPARATOR = "_";
    public static String getStreamId(String prefix,String... ids){
        return StringUtils.joinWith(SEPARATOR, (Object[]) ArrayUtils.addFirst(ids,prefix));
    }

    public static final Map<String, String> RTPMAP = new HashMap<>() {{
        put("96", "PS/90000");
        put("126", "H264/90000");
        put("125", "H264S/90000");
        put("99", "H265/90000");
        put("98", "H264/90000");
        put("97", "MPEG4/90000");
    }};
    public static final Map<String, String> FMTP = new HashMap<>() {{
        put("126", "profile-level-id=42e01e");
        put("125", "profile-level-id=42e01e");
    }};

    @AllArgsConstructor
    @Getter
    public enum Action {
        PLAY("Play"),
        PLAY_BACK("Playback"),
        DOWNLOAD("Download");

        @JsonValue
        private final String action;

        @JsonCreator
        public static Action fromCode(String action) {
            for (Action a : values()) {
                if (a.getAction().equalsIgnoreCase(action)) {
                    return a;
                }
            }
            return null;
        }
    }

    @SneakyThrows
    public static GB28181Description build(Action action, String deviceId, String channelId, String netType, String rtpIp, int rtpPort, String ssrc, StreamMode streamMode, TimeDescription timeDescription){
        log.debug("{} {} {} {} {} {} {} {} {}",action, deviceId, channelId, netType, rtpIp, rtpPort, ssrc, streamMode, timeDescription);
        GB28181Description description = new GB28181Description();
        description.setSessionName(SdpFactory.getInstance().createSessionName(action.getAction()));

        Version version = SdpFactory.getInstance().createVersion(0);
        description.setVersion(version);

        Connection connectionField = SdpFactory.getInstance().createConnection(ConnectionField.IN, netType, rtpIp);
        description.setConnection(connectionField);

        MediaDescription mediaDescription = SdpFactory.getInstance().createMediaDescription("video", rtpPort, 0, SdpConstants.RTP_AVP, MediaSdpHelper.RTPMAP.keySet().toArray(new String[0]));
        mediaDescription.addAttribute((AttributeField)SdpFactory.getInstance().createAttribute("recvonly",null));
        MediaSdpHelper.RTPMAP.forEach((k, v)->{
            Optional.ofNullable(MediaSdpHelper.FMTP.get(k)).ifPresent((f)->{
                mediaDescription.addAttribute((AttributeField) SdpFactory.getInstance().createAttribute(SdpConstants.FMTP.toLowerCase(), StringUtils.joinWith(Separators.SP,k,f)));
            });
            mediaDescription.addAttribute((AttributeField) SdpFactory.getInstance().createAttribute(SdpConstants.RTPMAP, StringUtils.joinWith(Separators.SP,k,v)));
        });

        if(streamMode == StreamMode.TCP_PASSIVE){
            // TCP-PASSIVE
            mediaDescription.addAttribute((AttributeField)SdpFactory.getInstance().createAttribute("setup","passive"));
            mediaDescription.addAttribute((AttributeField)SdpFactory.getInstance().createAttribute("connection","new"));
        } else if(streamMode == StreamMode.TCP_ACTIVE){
            // TCP-ACTIVE
            mediaDescription.addAttribute((AttributeField)SdpFactory.getInstance().createAttribute("setup","active"));
            mediaDescription.addAttribute((AttributeField)SdpFactory.getInstance().createAttribute("connection","new"));
        }

        description.setMediaDescriptions(new Vector<>() {{
            add(mediaDescription);
        }});

        description.setTimeDescriptions(new Vector<>(){{
            add(timeDescription);
        }});

        Origin origin = SdpFactory.getInstance().createOrigin(channelId, 0, 0, ConnectionField.IN, netType, rtpIp);
        description.setOrigin(origin);

        description.setSsrcField(new SsrcField(ssrc));
        return description;
    }

    @SneakyThrows
    public static GB28181Description play(String deviceId, String channelId, String netType, String rtpIp, int rtpPort, String ssrc, StreamMode streamMode){
        TimeDescription timeDescription = SdpFactory.getInstance().createTimeDescription();
        return build(Action.PLAY, deviceId, channelId, netType, rtpIp, rtpPort, ssrc, streamMode, timeDescription);
    }

    @SneakyThrows
    public static GB28181Description playback(String deviceId, String channelId, String netType, String rtpIp, int rtpPort, String ssrc, StreamMode streamMode, Date start, Date end) {
        TimeField timeField = new TimeField();
        timeField.setStart(start);
        timeField.setStop(end);
        TimeDescription timeDescription = SdpFactory.getInstance().createTimeDescription(timeField);

        GB28181Description description = build(Action.PLAY_BACK, deviceId, channelId, netType, rtpIp, rtpPort, ssrc, streamMode, timeDescription);

        URIField uriField = new URIField();
        uriField.setURI(StringUtils.joinWith(":", channelId, "0"));
        description.setURI(uriField);
        return description;
    }
}
