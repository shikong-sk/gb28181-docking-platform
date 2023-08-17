package cn.skcks.docking.gb28181.media.dto.config;

import lombok.Data;

@Data
public class ApiConfig {
    private Integer apiDebug;
    private String secret;
    private String defaultSnap;
    private String snapRoot;
}
