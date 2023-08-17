package cn.skcks.docking.gb28181.media.dto.config;

import com.fasterxml.jackson.annotation.JsonAlias;
import lombok.Data;

@Data
public class GeneralConfig {
    @JsonAlias("check_nvidia_dev")
    private Integer checkNvidiaDev;
    private Integer enableVhost;
    private Integer flowThreshold;
    private Integer maxStreamWaitMS;
    private Integer streamNoneReaderDelayMS;
}
