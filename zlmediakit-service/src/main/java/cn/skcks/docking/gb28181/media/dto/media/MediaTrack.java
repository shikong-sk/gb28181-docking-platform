package cn.skcks.docking.gb28181.media.dto.media;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.Data;

@Data
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class MediaTrack {
    /**
     * 音频通道数
     */
    private Integer channels;
    /**
     * H264 = 0, H265 = 1, AAC = 2, G711A = 3, G711U = 4
     */
    private Integer codecId;
    /**
     * 编码类型名称
     */
    private String codecIdName;
    /**
     * Video = 0, Audio = 1
     */
    private Integer codecType;
    private Integer fps;
    /**
     * 累计接收帧数，不包含sei/aud/sps/pps等不能解码的帧
     */
    private Long frames;
    /**
     * gop间隔时间，单位毫秒
     */
    private Long gopIntervalMs;
    /**
     * gop大小，单位帧数
     */
    private Long gopSize;
    /**
     * 累计接收关键帧数
     */
    private Long keyFrames;
    /**
     * 轨道是否准备就绪
     */
    private Boolean ready;
    /**
     * 视频高
     */
    private Integer height;
    /**
     * 视频宽
     */
    private Integer width;
    /**
     * 音频采样位数
     */
    private Integer sampleBit;
    /**
     * 音频采样率
     */
    private Integer sampleRate;
}
