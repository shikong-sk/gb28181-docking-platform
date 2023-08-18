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

    @JsonUnwrapped(prefix = "multicast.")
    private MulticastConfig multicast;

    @JsonUnwrapped(prefix = "protocol.")
    private ProtocolConfig protocol;

    @JsonUnwrapped(prefix = "record.")
    private RecordConfig record;

    @JsonUnwrapped(prefix = "rtc.")
    private RtcConfig rtc;

    @JsonUnwrapped(prefix = "rtmp.")
    private RtmpConfig rtmp;

    @JsonUnwrapped(prefix = "rtp.")
    private RtpConfig rtp;

    @JsonUnwrapped(prefix = "rtp_proxy.")
    private RtpProxyConfig rtpProxy;

    @JsonUnwrapped(prefix = "rtsp.")
    private RtspConfig rtsp;

    @JsonUnwrapped(prefix = "shell.")
    private ShellConfig shell;

    @JsonUnwrapped(prefix = "srt.")
    private SrtConfig srtConfig;
}
