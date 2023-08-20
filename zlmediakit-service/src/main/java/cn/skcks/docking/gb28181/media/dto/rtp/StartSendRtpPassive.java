package cn.skcks.docking.gb28181.media.dto.rtp;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.Data;

@Data
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class StartSendRtpPassive {
    /**
     * 添加的流的虚拟主机，例如__defaultVhost__
     */
    private String vhost = "__defaultVhost__";
    /**
     * 应用名
     */
    private String app;
    /**
     * 流id
     */
    private String stream;
    /**
     * 推流的rtp的ssrc,指定不同的ssrc可以同时推流到多个服务器
     */
    private String ssrc;
    /**
     * 使用的本机端口，为0或不传时默认为随机端口
     */
    private Integer srcPort;
    /**
     * 发送时，rtp的pt（uint8_t）,不传时默认为96
     */
    private Integer pt;
    /**
     * 发送时，rtp的负载类型。为1时，负载为ps；为0时，为es；不传时默认为1
     */
    private Integer usePs;
    /**
     * 当use_ps 为0时，有效。为1时，发送音频；为0时，发送视频；不传时默认为0
     */
    private Integer onlyAudio;
}
