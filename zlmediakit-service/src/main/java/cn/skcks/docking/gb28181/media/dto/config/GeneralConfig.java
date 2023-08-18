package cn.skcks.docking.gb28181.media.dto.config;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class GeneralConfig {
    @JsonProperty("check_nvidia_dev")
    private Integer checkNvidiaDev;

    private Integer enableVhost;

    @JsonProperty("enable_ffmpeg_log")
    private Integer enableFfmpegLog;

    private Integer flowThreshold;
    private Integer maxStreamWaitMS;
    private String mediaServerId;
    private Integer mergeWriteMS;
    private Integer resetWhenRePlay;
    private Integer streamNoneReaderDelayMS;

    @JsonProperty("unready_frame_cache")
    private Integer unreadyFrameCache;

    @JsonProperty("wait_add_track_ms")
    private Integer waitAddTrackMs;

    @JsonProperty("wait_track_ready_ms")
    private Integer waitTrackReadyMs;
}
