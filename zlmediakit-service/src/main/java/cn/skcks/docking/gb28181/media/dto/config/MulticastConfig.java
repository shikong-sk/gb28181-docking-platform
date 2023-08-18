package cn.skcks.docking.gb28181.media.dto.config;

import lombok.Data;

@Data
public class MulticastConfig {
    private String addrMax;
    private String addrMin;
    private Integer udpTTL;
}
