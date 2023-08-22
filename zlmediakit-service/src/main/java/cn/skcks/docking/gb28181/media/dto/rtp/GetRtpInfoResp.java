package cn.skcks.docking.gb28181.media.dto.rtp;

import cn.skcks.docking.gb28181.media.dto.status.ResponseStatus;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.Data;

@Data
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class GetRtpInfoResp {
    private ResponseStatus code;
    /**
     * 是否存在
     */
    private Boolean exist;
    /**
     * 推流客户端ip
     */
    private String peerIp;
    /**
     * 客户端端口号
     */
    private Integer peerPort;
    /**
     * 本地监听的网卡ip
     */
    private String localIp;
    /**
     * 本地监听端口号
     */
    private Integer localPort;
}
