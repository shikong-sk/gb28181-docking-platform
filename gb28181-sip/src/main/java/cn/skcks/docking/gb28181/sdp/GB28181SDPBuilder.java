package cn.skcks.docking.gb28181.sdp;

import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;

import java.util.HashMap;
import java.util.Map;

public class GB28181SDPBuilder {
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
}
