package cn.skcks.docking.gb28181.media.dto.config;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class RtmpConfig {
    private Integer handshakeSecond;
    private Integer keepAliveSecond;
    private Integer modifyStamp;
    private Integer port;
    @JsonProperty("sslport")
    private Integer sslPort;
}
