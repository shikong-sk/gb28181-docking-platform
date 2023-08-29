package cn.skcks.docking.gb28181.api.info;

import cn.skcks.docking.gb28181.annotation.web.JsonMapping;
import cn.skcks.docking.gb28181.annotation.web.methods.GetJson;
import cn.skcks.docking.gb28181.common.json.JsonResponse;
import cn.skcks.docking.gb28181.config.SwaggerConfig;
import cn.skcks.docking.gb28181.orm.mybatis.dynamic.model.DockingDevice;
import cn.skcks.docking.gb28181.service.info.InfoService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springdoc.core.models.GroupedOpenApi;
import org.springframework.context.annotation.Bean;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Tag(name="详情/信息")
@RestController
@JsonMapping("/api/device/info")
@RequiredArgsConstructor
public class InfoController {
    private final InfoService infoService;
    @Bean
    public GroupedOpenApi infoApi() {
        return SwaggerConfig.api("Info", "/api/device/info");
    }

    @GetJson("/list")
    public JsonResponse<List<DockingDevice>> list(){
        return JsonResponse.success(infoService.getDeviceList());
    }
}
