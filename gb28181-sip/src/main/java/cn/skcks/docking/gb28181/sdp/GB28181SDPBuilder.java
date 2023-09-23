package cn.skcks.docking.gb28181.sdp;

import cn.skcks.docking.gb28181.sdp.field.ssrc.SsrcField;
import cn.skcks.docking.gb28181.sdp.media.MediaStreamMode;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import gov.nist.core.Separators;
import gov.nist.javax.sdp.MediaDescriptionImpl;
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

@SuppressWarnings("unused")
@Slf4j
public class GB28181SDPBuilder {
    public final static String SEPARATOR = "_";

    public static String getStreamId(String prefix, String... ids) {
        return StringUtils.joinWith(SEPARATOR, (Object[]) ArrayUtils.addFirst(ids, prefix));
    }

    public static Map<String, String> RTPMAP = new HashMap<>() {{
        put("96", "PS/90000");
        put("126", "H264/90000");
        put("125", "H264S/90000");
        put("99", "H265/90000");
        put("98", "H264/90000");
        put("97", "MPEG4/90000");
    }};
    public static Map<String, String> FMTP = new HashMap<>() {{
        put("126", "profile-level-id=42e01e");
        put("125", "profile-level-id=42e01e");
    }};

    @AllArgsConstructor
    @Getter
    public enum Protocol {
        RTP_AVP(SdpConstants.RTP_AVP),
        RTP_AVP_TCP(SdpConstants.RTP_AVP + "/" + "TCP"),
        // 个别会使用这种格式
        TCP_RTP_AVP("TCP" + "/" + SdpConstants.RTP_AVP);

        @JsonValue
        private final String protocol;

        @JsonCreator
        public static Protocol fromProtocol(String protocol) {
            for (Protocol a : values()) {
                if (a.getProtocol().equalsIgnoreCase(protocol)) {
                    return a;
                }
            }
            return null;
        }

        @SneakyThrows
        public static Protocol fromProtocol(MediaDescription mediaDescription) {
            String protocol = mediaDescription.getMedia().getProtocol();
            for (Protocol a : values()) {
                if (a.getProtocol().equalsIgnoreCase(protocol)) {
                    return a;
                }
            }
            return null;
        }
    }

    @AllArgsConstructor
    @Getter
    public enum Action {
        PLAY("Play"), PLAY_BACK("Playback"), DOWNLOAD("Download");

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


    @SuppressWarnings("Duplicates")
    @SneakyThrows
    public static GB28181Description build(Action action, String deviceId, String channelId,
                                           String netType, String rtpIp, int rtpPort, String ssrc,
                                           MediaStreamMode streamMode, TimeDescription timeDescription,
                                           boolean isRecv, Map<String,String> rtpMap, Map<String,String> fmtpMap) {
        log.debug("{} {} {} {} {} {} {} {} {}", action, deviceId, channelId, netType, rtpIp, rtpPort, ssrc, streamMode, timeDescription);
        GB28181Description description = new GB28181Description();
        description.setSessionName(SdpFactory.getInstance().createSessionName(action.getAction()));

        Version version = SdpFactory.getInstance().createVersion(0);
        description.setVersion(version);

        Connection connectionField = SdpFactory.getInstance().createConnection(ConnectionField.IN, netType, rtpIp);
        description.setConnection(connectionField);

        MediaDescription mediaDescription;
        if(MediaStreamMode.UDP.equals(streamMode)){
            mediaDescription = SdpFactory.getInstance().createMediaDescription("video", rtpPort, 0, Protocol.RTP_AVP.getProtocol(), rtpMap.keySet().toArray(new String[0]));
        } else {
            mediaDescription = SdpFactory.getInstance().createMediaDescription("video", rtpPort, 0, Protocol.RTP_AVP_TCP.getProtocol(), rtpMap.keySet().toArray(new String[0]));
        }

        if (isRecv) {
            mediaDescription.addAttribute((AttributeField) SdpFactory.getInstance().createAttribute("recvonly", null));
        } else {
            mediaDescription.addAttribute((AttributeField) SdpFactory.getInstance().createAttribute("sendonly", null));
        }

        rtpMap.forEach((k, v) -> {
            if(fmtpMap != null){
                Optional.ofNullable(fmtpMap.get(k)).ifPresent((f) -> {
                    mediaDescription.addAttribute((AttributeField) SdpFactory.getInstance().createAttribute(SdpConstants.FMTP.toLowerCase(), StringUtils.joinWith(Separators.SP, k, f)));
                });
            }
            mediaDescription.addAttribute((AttributeField) SdpFactory.getInstance().createAttribute(SdpConstants.RTPMAP, StringUtils.joinWith(Separators.SP, k, v)));
        });

        if (streamMode == MediaStreamMode.TCP_PASSIVE) {
            // TCP-PASSIVE
            mediaDescription.addAttribute((AttributeField) SdpFactory.getInstance().createAttribute("setup", "passive"));
            mediaDescription.addAttribute((AttributeField) SdpFactory.getInstance().createAttribute("connection", "new"));
        } else if (streamMode == MediaStreamMode.TCP_ACTIVE) {
            // TCP-ACTIVE
            mediaDescription.addAttribute((AttributeField) SdpFactory.getInstance().createAttribute("setup", "active"));
            mediaDescription.addAttribute((AttributeField) SdpFactory.getInstance().createAttribute("connection", "new"));
        }

        description.setMediaDescriptions(new Vector<>() {{
            add(mediaDescription);
        }});

        description.setTimeDescriptions(new Vector<>() {{
            add(timeDescription);
        }});

        Origin origin = SdpFactory.getInstance().createOrigin(channelId, 0, 0, ConnectionField.IN, netType, rtpIp);
        description.setOrigin(origin);

        description.setSsrcField(new SsrcField(ssrc));
        return description;
    }

    public static class Receiver {
        @SneakyThrows
        public static GB28181Description build(Action action, String deviceId, String channelId, String netType, String rtpIp, int rtpPort, String ssrc, MediaStreamMode streamMode, TimeDescription timeDescription) {
            return GB28181SDPBuilder.build(action, deviceId, channelId, netType, rtpIp, rtpPort, ssrc, streamMode, timeDescription, true, GB28181SDPBuilder.RTPMAP, GB28181SDPBuilder.FMTP);
        }

        @SneakyThrows
        public static GB28181Description play(String deviceId, String channelId, String netType, String rtpIp, int rtpPort, String ssrc, MediaStreamMode streamMode) {
            TimeDescription timeDescription = SdpFactory.getInstance().createTimeDescription();
            return build(Action.PLAY, deviceId, channelId, netType, rtpIp, rtpPort, ssrc, streamMode, timeDescription);
        }

        @SneakyThrows
        public static GB28181Description playback(String deviceId, String channelId, String netType, String rtpIp, int rtpPort, String ssrc, MediaStreamMode streamMode, Date start, Date end) {
            TimeField timeField = new TimeField();
            timeField.setStartTime(start.toInstant().getEpochSecond());
            timeField.setStopTime(end.toInstant().getEpochSecond());
            TimeDescription timeDescription = SdpFactory.getInstance().createTimeDescription(timeField);

            GB28181Description description = build(Action.PLAY_BACK, deviceId, channelId, netType, rtpIp, rtpPort, ssrc, streamMode, timeDescription);

            URIField uriField = new URIField();
            uriField.setURI(StringUtils.joinWith(":", channelId, "0"));
            description.setURI(uriField);
            return description;
        }

        @SneakyThrows
        public static GB28181Description download(String deviceId, String channelId, String netType, String rtpIp, int rtpPort, String ssrc, MediaStreamMode streamMode, Date start, Date end, Double downloadSpeed) {
            TimeField timeField = new TimeField();
            timeField.setStartTime(start.toInstant().getEpochSecond());
            timeField.setStopTime(end.toInstant().getEpochSecond());
            TimeDescription timeDescription = SdpFactory.getInstance().createTimeDescription(timeField);

            GB28181Description description = build(Action.DOWNLOAD, deviceId, channelId, netType, rtpIp, rtpPort, ssrc, streamMode, timeDescription);
            MediaDescriptionImpl media = (MediaDescriptionImpl) description.getMediaDescriptions(false).get(0);
            media.setAttribute("downloadspeed", String.valueOf(downloadSpeed));

            URIField uriField = new URIField();
            uriField.setURI(StringUtils.joinWith(":", channelId, "0"));
            description.setURI(uriField);
            return description;
        }
    }

    public static class Sender {
        @SneakyThrows
        public static GB28181Description build(Action action, String deviceId, String channelId, String netType, String rtpIp, int rtpPort, String ssrc, MediaStreamMode streamMode, TimeDescription timeDescription, Map<String,String> rtpMap, Map<String,String> fmtpMap) {
            return GB28181SDPBuilder.build(action, deviceId, channelId, netType, rtpIp, rtpPort, ssrc, streamMode, timeDescription, false, GB28181SDPBuilder.RTPMAP, GB28181SDPBuilder.FMTP);
        }

        @SneakyThrows
        public static GB28181Description build(Action action, String deviceId, String channelId, String netType, String rtpIp, int rtpPort, String ssrc, MediaStreamMode streamMode, TimeDescription timeDescription) {
            return build(action, deviceId, channelId, netType, rtpIp, rtpPort, ssrc, streamMode, timeDescription, GB28181SDPBuilder.RTPMAP, GB28181SDPBuilder.FMTP);
        }

        @SuppressWarnings({"unchecked","Duplicates"})
        @SneakyThrows
        public static GB28181Description build(GB28181Description receive, String rtpIp, int rtpPort, Map<String,String> rtpMap, Map<String,String> fmtpMap){
            GB28181Description description = new GB28181Description(receive);
            MediaDescriptionImpl media = (MediaDescriptionImpl) description.getMediaDescriptions(true).get(0);
            Vector<String> formats = media.getMedia().getMediaFormats(true);
            formats.clear();
            formats.addAll(rtpMap.keySet());

            Vector<String> attributes = media.getAttributes(true);
            attributes.clear();

            rtpMap.forEach((k, v) -> {
                if (fmtpMap != null) {
                    Optional.ofNullable(fmtpMap.get(k)).ifPresent((f) -> {
                        media.addAttribute((AttributeField) SdpFactory.getInstance().createAttribute(SdpConstants.FMTP.toLowerCase(), StringUtils.joinWith(Separators.SP, k, f)));
                    });
                }

                media.addAttribute((AttributeField) SdpFactory.getInstance().createAttribute(SdpConstants.RTPMAP, StringUtils.joinWith(Separators.SP, k, v)));
            });

            media.setAttribute("sendonly",null);
            Connection connection = description.getConnection();
            connection.setAddress(rtpIp);
            media.getMedia().setMediaPort(rtpPort);
            return description;
        }


        @SneakyThrows
        public static GB28181Description build(GB28181Description receive, String rtpIp, int rtpPort){
            GB28181Description description = new GB28181Description(receive);
            MediaDescriptionImpl media = (MediaDescriptionImpl) description.getMediaDescriptions(true).get(0);
            return build(description, rtpIp, rtpPort, GB28181SDPBuilder.RTPMAP, GB28181SDPBuilder.FMTP);
        }
    }
}
