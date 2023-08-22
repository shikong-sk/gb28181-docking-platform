package cn.skcks.docking.gb28181.config;

import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import lombok.Data;
import org.springdoc.core.customizers.OpenApiCustomizer;
import org.springdoc.core.models.GroupedOpenApi;
import org.springframework.context.annotation.Configuration;

import static org.springdoc.core.utils.Constants.ALL_PATTERN;


@Configuration
@Data
public class SwaggerConfig {
    public static final Info INFO;
    private static final License LICENSE;

    static {
        INFO = new Info()
                .title("GB28181 Docking Platform API")
                .description("Matrix API description")
                .version("2.0");
        LICENSE = new License()
                .name("Apache 2.0")
                .url("http://www.apache.org/licenses/LICENSE-2.0.html");
        INFO.license(LICENSE);
    }

    public static GroupedOpenApi api(String group, String path) {
        return GroupedOpenApi.builder()
                .group(group)
                .pathsToMatch(path + ALL_PATTERN)
                .addOpenApiCustomizer(openApi -> openApi.info(SwaggerConfig.INFO))
                .addOpenApiCustomizer(authOpenApiDocs())
                .build();
    }

    private static OpenApiCustomizer authOpenApiDocs(){
        return openApi -> {
            // swagger 全局添加 token 认证选项
            openApi.addSecurityItem(new SecurityRequirement().addList("token"));
            openApi.getComponents()
                    .addSecuritySchemes("token",
                            new SecurityScheme()
                                    .type(SecurityScheme.Type.APIKEY)
                                    .in(SecurityScheme.In.HEADER)
                                    .name("token"));
        };
    }
}
