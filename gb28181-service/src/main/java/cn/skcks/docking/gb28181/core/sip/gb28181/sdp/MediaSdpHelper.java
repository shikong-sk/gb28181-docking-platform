package cn.skcks.docking.gb28181.core.sip.gb28181.sdp;

import javax.sdp.SessionDescription;
import java.util.HashMap;
import java.util.Map;

public class MediaSdpHelper {
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

    public SessionDescription build(String deviceId, String channelId, String rtpIp, int rtpPort, String streamMode){
        return null;
    }
}
