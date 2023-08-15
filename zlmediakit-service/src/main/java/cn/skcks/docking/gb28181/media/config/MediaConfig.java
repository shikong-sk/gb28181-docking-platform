package cn.skcks.docking.gb28181.media.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Data
@Configuration
@ConfigurationProperties(prefix = "media")
public class MediaConfig {
    private String url;
}
