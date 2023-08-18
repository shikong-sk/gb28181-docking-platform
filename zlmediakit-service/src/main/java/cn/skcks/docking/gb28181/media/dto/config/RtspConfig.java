package cn.skcks.docking.gb28181.media.dto.config;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class RtspConfig {
    private Integer authBasic;
    private Integer directProxy;
    private Integer handshakeSecond;
    private Integer keepAliveSecond;
    private Integer lowLatency;
    private Integer port;
    private Integer rtpTransportType;

    @JsonProperty("sslport")
    private Integer sslPort;
}
