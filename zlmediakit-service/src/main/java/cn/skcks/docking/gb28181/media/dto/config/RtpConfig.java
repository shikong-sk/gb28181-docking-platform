package cn.skcks.docking.gb28181.media.dto.config;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class RtpConfig {
    private Integer audioMtuSize;
    @JsonProperty("h264_stap_a")
    private Integer h264StapA;
    private Integer lowLatency;
    private Integer rtpMaxSize;
    private Integer videoMtuSize;
}
