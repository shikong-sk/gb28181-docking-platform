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
public class AddFFmpegSource {
    /**
     * FFmpeg 拉流地址,支持任意协议或格式(只要 FFmpeg 支持即可)
     */
    private String srcUrl;
    /**
     * FFmpeg rtmp 推流地址，一般都是推给自己
     * <p>例如 rtmp://127.0.0.1/live/stream_form_ffmpeg</p>
     */
    private String dstUrl;
    /**
     * FFmpeg 推流成功超时时间
     */
    private long timeoutMs;
    /**
     * 是否开启 hls 录制
     */
    private Boolean enableHls;
    /**
     * 是否开启 mp4 录制
     */
    private Boolean enableMp4;
    /**
     * 配置文件中 FFmpeg 命令参数模板 key(非内容)，置空则采用默认模板:ffmpeg.cmd
     */
    private String ffmpegCmdKey;
}
