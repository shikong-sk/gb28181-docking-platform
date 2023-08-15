package cn.skcks.docking.gb28181.media.dto.config;


import com.fasterxml.jackson.annotation.JsonUnwrapped;
import lombok.Data;

@Data
public class ServerConfig {
    @JsonUnwrapped(prefix = "api.")
    private ApiConfig api;

    @JsonUnwrapped(prefix = "ffmpeg.")
    private FFMpegConfig ffmpeg;
}