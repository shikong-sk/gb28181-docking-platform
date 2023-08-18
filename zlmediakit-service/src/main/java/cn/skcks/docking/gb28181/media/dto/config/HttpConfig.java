package cn.skcks.docking.gb28181.media.dto.config;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class HttpConfig {
    @JsonProperty("allow_cross_domains")
    private Integer allowCrossDomains;
    @JsonProperty("allow_ip_range")
    private String allowIpRange;
    private String charSet;
    private Integer dirMenu;
    private String forbidCacheSuffix;
    @JsonProperty("forwarded_ip_header")
    private String forwardedIpHeader;
    private String keepAliveSecond;
    private Integer maxReqSize;
    private String notFound;
    private Integer port;
    private String rootPath;
    private Integer sendBufSize;
    @JsonProperty("sslport")
    private Integer sslPort;
    private String virtualPath;
}
