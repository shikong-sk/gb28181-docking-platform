package cn.skcks.docking.gb28181.media.dto.config;

import lombok.Data;

@Data
public class RtcConfig {
    private String externIP;
    private Integer port;
    private String preferredCodecA;
    private String preferredCodecV;
    private Integer rembBitRate;
    private Integer tcpPort;
    private Integer timeoutSec;
}
