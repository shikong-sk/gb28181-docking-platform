package cn.skcks.docking.gb28181.media.dto.media;

import lombok.Data;

import java.util.List;

@Data
public class MediaResp {
    /**
     * 应用名
     */
    private String app;
    /**
     * 本协议观看人数
     */
    private Long readerCount;
    /**
     * 观看总人数，包括hls/rtsp/rtmp/http-flv/ws-flv
     */
    private Long totalReaderCount;
    /**
     * 协议
     */
    private String schema;
    /**
     * 流id
     */
    private String stream;
    /**
     * 客户端和服务器网络信息
     */
    private MediaOriginSock originSock;
    /**
     * <p>产生源类型</p>
     * unknown = 0,rtmp_push=1,rtsp_push=2,rtp_push=3,pull=4,ffmpeg_pull=5,mp4_vod=6,device_chn=7
     */
    private Integer originType;
    private String originTypeStr;
    /**
     * 产生源的url
     */
    private String originUrl;
    /**
     * GMT unix系统时间戳，单位秒
     */
    private Long createStamp;
    /**
     * 存活时间，单位秒
     */
    private Long aliveSecond;
    /**
     * 数据产生速度，单位byte/s
     */
    private Long bytesSpeed;
    /**
     * 音视频轨道
     */
    private List<MediaTrack> tracks;
    /**
     * 虚拟主机名
     */
    private String vhost;
}
