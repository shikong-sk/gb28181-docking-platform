package cn.skcks.docking.gb28181.media.dto.rtp;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.Data;

@Data
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class StopSendRtp {
    /**
     * 添加的流的虚拟主机，例如__defaultVhost__
     */
    private String vhost = "__defaultVhost__";
    /**
     * 应用名
     */
    private String app;
    /**
     * 流id
     */
    private String stream;
    /**
     * 推流的rtp的ssrc,指定不同的ssrc可以同时推流到多个服务器
     */
    private String ssrc;
}
