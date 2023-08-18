package cn.skcks.docking.gb28181.media.dto.config;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.Data;

@Data
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class HookConfig {
    private Double aliveInterval;
    private Integer enable;
    private String onFlowReport;
    private String onHttpAccess;
    private String onPlay;
    private String onPublish;
    private String onRecordMp4;
    private String onRecordTs;
    private String onRtpServerTimeout;
    private String onRtspAuth;
    private String onRtspRealm;
    private String onSendRtpStopped;
    private String onServerExited;
    private String onServerKeepalive;
    private String onServerStarted;
    private String onShellLogin;
    private String onStreamChanged;
    private String onStreamNoneReader;
    private String onStreamNotFound;
    private Long retry;
    private Double retryDelay;
    @JsonProperty("timeoutSec")
    private Integer timeoutSec;
}
