package cn.skcks.docking.gb28181.media.dto.rtp;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.Data;

@Data
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class RtpServer {
    /**
     * 绑定的端口号
     */
    private int port;
    /**
     * 绑定的流ID
     */
    private String streamId;
}
