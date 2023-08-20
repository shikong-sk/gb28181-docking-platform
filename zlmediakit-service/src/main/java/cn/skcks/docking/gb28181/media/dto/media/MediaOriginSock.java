package cn.skcks.docking.gb28181.media.dto.media;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.Data;

/**
 * 客户端和服务器网络信息
 */
@Data
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class MediaOriginSock {
    private String identifier;
    private String localIp;
    private Integer localPort;
    private String peerIp;
    private Integer peerPort;
}
