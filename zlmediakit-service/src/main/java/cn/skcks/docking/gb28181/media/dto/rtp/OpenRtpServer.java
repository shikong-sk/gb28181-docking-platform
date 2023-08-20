package cn.skcks.docking.gb28181.media.dto.rtp;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import feign.Param;
import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class OpenRtpServer {
    /**
     * 接收端口，0则为随机端口
     */
    private int port;

    /**
     * 0 udp 模式，1 tcp 被动模式, 2 tcp 主动模式。 (兼容enable_tcp 为0/1)
     */
    private int tcpMode;

    /**
     * 该端口绑定的流ID，该端口只能创建这一个流(而不是根据ssrc创建多个)
     */
    private String streamId;
}
