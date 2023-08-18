package cn.skcks.docking.gb28181.media.dto.config;


import com.fasterxml.jackson.annotation.JsonUnwrapped;
import lombok.Data;

@Data
public class ServerConfig {
    @JsonUnwrapped(prefix = "api.")
    private ApiConfig api;

    @JsonUnwrapped(prefix = "cluster.")
    private ClusterConfig cluster;

    @JsonUnwrapped(prefix = "ffmpeg.")
    private FFMpegConfig ffmpeg;

    @JsonUnwrapped(prefix = "general.")
    private GeneralConfig general;

    @JsonUnwrapped(prefix = "hls.")
    private HlsConfig hls;

    @JsonUnwrapped(prefix = "hook.")
    private HookConfig hook;

    @JsonUnwrapped(prefix = "http.")
    private HttpConfig http;
}
