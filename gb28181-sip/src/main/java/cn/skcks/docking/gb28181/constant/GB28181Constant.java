package cn.skcks.docking.gb28181.constant;

import javax.sip.ListeningPoint;

public class GB28181Constant {
    public static final String TIME_ZONE = "Asia/Shanghai";
    public static final String CHARSET = "GB2312";
    public static final String GEO_COORD_SYS = "WGS84";

    public static class TransPort {
        public static final String UDP = ListeningPoint.UDP;
        public static final String TCP = ListeningPoint.TCP;
    }
}
