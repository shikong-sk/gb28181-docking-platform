package cn.skcks.docking.gb28181.media.dto.proxy;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.Data;

@Data
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class AddStreamPusherProxy {
    /**
     * 添加的流的虚拟主机，例如__defaultVhost__
     */
    private String vhost = "__defaultVhost__";
    /**
     * 协议，例如 rtsp或rtmp
     */
    private String schema;
    /**
     * 添加的流的应用名，例如live
     */
    private String app;
    /**
     * 需要转推的流id
     */
    private String stream;
    /**
     * 目标转推url，带参数需要自行url转义
     */
    private String dstUrl;
    /**
     * 转推失败重试次数，默认无限重试
     */
    private Integer retryCount;
    /**
     * rtsp推流时，推流方式，0：tcp，1：udp
     */
    private Integer rtpType;
    /**
     * 推流超时时间，单位秒，float类型
     */
    private Double timeoutSec;
}
