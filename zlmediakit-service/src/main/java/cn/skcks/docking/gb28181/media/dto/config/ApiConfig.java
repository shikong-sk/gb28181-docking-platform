package cn.skcks.docking.gb28181.media.dto.config;

import feign.Param;
import lombok.Data;

@Data
public class ApiConfig {
    @Param("api.apiDebug")
    private Integer apiDebug;

    @Param("api.secret")
    private String secret;
    @Param("api.defaultSnap")
    private String defaultSnap;
    @Param("api.snapRoot")
    private String snapRoot;
}
