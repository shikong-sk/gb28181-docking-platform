package cn.skcks.docking.gb28181.common.config;

import jakarta.annotation.PostConstruct;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;

@Slf4j
@Data
@Order(0)
@Configuration
@ConfigurationProperties(prefix = "project")
public class ProjectConfig {
    private String version;

    @PostConstruct
    private void init(){
        log.info("项目版本号 {}", version);
    }
}
