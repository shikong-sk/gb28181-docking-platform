package cn.skcks.docking.gb28181.core.sip.gb28181.cache;

import lombok.experimental.UtilityClass;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;

@UtilityClass
public class CacheUtil {
    public final static String SEPARATOR = ":";
    public static String getKey(String prefix,String... ids){
        return StringUtils.joinWith(SEPARATOR, (Object[]) ArrayUtils.addFirst(ids,prefix));
    }

    public final static String SIP_C_SEQ_PREFIX = "SIP_C_SEQ";
}
