package cn.skcks.docking.gb28181.media.dto.proxy;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class AddStreamProxy {
    /**
     * 添加的流的虚拟主机，例如__defaultVhost__
     */
    @Builder.Default
    private String vhost = "__defaultVhost__";
    /**
     * 添加的流的应用名，例如live
     */
    private String app;
    /**
     * 添加的流的id名
     */
    private String stream;
    /**
     * 拉流地址
     */
    private String url;
    /**
     * 转推失败重试次数，-1 无限重试
     */
    private Integer retryCount;
    /**
     * rtsp拉流时，拉流方式，0：tcp，1：udp，2：组播
     */
    private Integer rtpType;
    /**
     * 拉流超时时间，单位秒，float类型
     */
    private Double timeoutSec;
    /**
     * 是否转换成hls-mpegts协议
     */
    private Boolean enableHls;
    /**
     * 是否转换成hls-fmp4协议
     */
    private Boolean enableHlsFmp4;
    /**
     * 是否允许mp4录制
     */
    private Boolean enableMp4;
    /**
     * 是否转rtsp协议
     */
    private Boolean enableRtsp;
    /**
     * 是否转rtmp/flv协议
     */
    private Boolean enableRtmp;
    /**
     * 是否转http-ts/ws-ts协议
     */
    private Boolean enableTs;
    /**
     * 是否转http-fmp4/ws-fmp4协议
     */
    private Boolean enableFmp4;
    /**
     * 该协议是否有人观看才生成
     */
    private Boolean hlsDemand;
    /**
     * 该协议是否有人观看才生成
     */
    private Boolean rtspDemand;
    /**
     * 该协议是否有人观看才生成
     */
    private Boolean rtmpDemand;
    /**
     * 该协议是否有人观看才生成
     */
    private Boolean tsDemand;
    /**
     * 该协议是否有人观看才生成
     */
    private Boolean fmp4Demand;
    /**
     * 转协议时是否开启音频
     */
    private Boolean enableAudio;
    /**
     * 转协议时，无音频是否添加静音aac音频
     */
    private Boolean addMuteAudio;
    /**
     * mp4录制文件保存根目录，置空使用默认
     */
    private String mp4SavePath;
    /**
     * mp4录制切片大小，单位秒
     */
    private Integer mp4MaxSecond;
    /**
     * MP4录制是否当作观看者参与播放人数计数
     */
    private Boolean mp4AsPlayer;
    /**
     * hls文件保存保存根目录，置空使用默认
     */
    private Boolean hlsSavePath;
    /**
     * 该流是否开启时间戳覆盖(0:绝对时间戳/1:系统时间戳/2:相对时间戳)
     */
    private Integer modifyStamp;
    /**
     * 无人观看是否自动关闭流(不触发无人观看hook)
     */
    private Boolean autoClose;
}
