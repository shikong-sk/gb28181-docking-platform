package cn.skcks.docking.gb28181.media.dto.config;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.Data;

@Data
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class RtpProxyConfig {
    @JsonProperty("dumpDir")
    private String dumpDir;
    private Integer gopCache;
    private Integer h264Pt;
    private Integer h265Pt;
    private Integer opusPt;
    private Integer port;
    private String portRange;
    private Integer psPt;
    @JsonProperty("timeoutSec")
    private Integer timeoutSec;
}
