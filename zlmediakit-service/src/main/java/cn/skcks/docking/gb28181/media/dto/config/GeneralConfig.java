package cn.skcks.docking.gb28181.media.dto.config;

import lombok.Data;

@Data
public class GeneralConfig {
    private Integer enableVhost;
    private Integer flowThreshold;
    private Integer maxStreamWaitMS;
    private Integer streamNoneReaderDelayMS;
}
