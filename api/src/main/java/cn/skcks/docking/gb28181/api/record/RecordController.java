package cn.skcks.docking.gb28181.api.record;

import cn.skcks.docking.gb28181.annotation.web.JsonMapping;
import cn.skcks.docking.gb28181.annotation.web.methods.GetJson;
import cn.skcks.docking.gb28181.api.record.dto.GetInfoDTO;
import cn.skcks.docking.gb28181.common.json.JsonResponse;
import cn.skcks.docking.gb28181.config.SwaggerConfig;
import cn.skcks.docking.gb28181.service.record.RecordService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springdoc.core.annotations.ParameterObject;
import org.springdoc.core.models.GroupedOpenApi;
import org.springframework.context.annotation.Bean;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RestController;

@Tag(name="历史录像")
@RestController
@JsonMapping("/record")
@RequiredArgsConstructor
public class RecordController {
    private final RecordService recordService;

    @Bean
    public GroupedOpenApi recordApi() {
        return SwaggerConfig.api("Record", "/record");
    }

    @GetJson("/getInfo")
    public JsonResponse<Void> getInfo(@ParameterObject @Validated GetInfoDTO dto){
        recordService.requestRecordInfo(dto.getDeviceId());
        return JsonResponse.success(null);
    }
}
