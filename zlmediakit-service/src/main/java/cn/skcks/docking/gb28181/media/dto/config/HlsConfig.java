package cn.skcks.docking.gb28181.media.dto.config;

import lombok.Data;

@Data
public class HlsConfig {
    private Integer broadcastRecordTs;
    private Integer deleteDelaySec;
    private Integer fileBufSize;
    private String filePath;
    private Integer segDur;
    private Integer segNum;
    private Integer segRetain;
}
