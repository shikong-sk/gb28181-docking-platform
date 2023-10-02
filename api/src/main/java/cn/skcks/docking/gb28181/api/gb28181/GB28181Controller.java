package cn.skcks.docking.gb28181.api.gb28181;

import cn.skcks.docking.gb28181.annotation.web.JsonMapping;
import cn.skcks.docking.gb28181.config.SwaggerConfig;
import org.springdoc.core.models.GroupedOpenApi;
import org.springframework.context.annotation.Bean;
import org.springframework.web.bind.annotation.RestController;

@JsonMapping("/api/gb28181")
@RestController
public class GB28181Controller {
    @Bean
    public GroupedOpenApi gb28181Api() {
        return SwaggerConfig.api("GB28181", "/api/gb28181");
    }
}
