package cn.skcks.docking.gb28181.media.dto.config;

import lombok.Data;

@Data
public class SrtConfig {
    private Integer latencyMul;
    private Integer pktBufSize;
    private Integer port;
    private Integer timeoutSec;
}
