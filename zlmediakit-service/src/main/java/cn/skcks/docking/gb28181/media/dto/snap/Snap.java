package cn.skcks.docking.gb28181.media.dto.snap;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.Data;

@Data
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class Snap {
    /**
     * 需要截图的url，可以是本机的，也可以是远程主机的
     */
    private String url;
    /**
     * 截图失败超时时间，防止 ffmpeg 一直等待截图
     */
    private int timeoutSec;
    /**
     * 截图的过期时间，该时间内产生的截图都会作为缓存返回
     */
    private int expireSec;
}
