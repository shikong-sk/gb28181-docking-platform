package cn.skcks.docking.gb28181.media.dto.config;

import lombok.Data;

@Data
public class RecordConfig {
    private String appName;
    private Integer fastStart;
    private Integer fileBufSize;
    private Integer fileRepeat;
    private Integer sampleMS;
}
