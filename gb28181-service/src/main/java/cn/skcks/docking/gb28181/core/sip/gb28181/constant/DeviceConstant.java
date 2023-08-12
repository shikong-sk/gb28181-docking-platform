package cn.skcks.docking.gb28181.core.sip.gb28181.constant;

import lombok.experimental.UtilityClass;

import java.util.concurrent.TimeUnit;

@UtilityClass
public class DeviceConstant {
    public final static int KEEP_ALIVE_INTERVAL = 60;
    public final static TimeUnit UNIT = TimeUnit.SECONDS;

    @UtilityClass
    public class Cache {
        public final static String DEVICE = "DEVICE";
        public final static String ONLINE = "ONLINE";
        public final static String TRANSACTION = "TRANSACTION";

    }
}
