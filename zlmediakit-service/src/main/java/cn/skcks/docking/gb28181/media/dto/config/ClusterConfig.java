package cn.skcks.docking.gb28181.media.dto.config;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.Data;

@Data
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class ClusterConfig {
    private String originUrl;

    private String retryCount;
    private String timeoutSec;
}
