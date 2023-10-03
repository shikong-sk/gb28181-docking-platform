package cn.skcks.docking.gb28181.api.gb28181.record;

import cn.skcks.docking.gb28181.annotation.web.JsonMapping;
import cn.skcks.docking.gb28181.annotation.web.methods.GetJson;
import cn.skcks.docking.gb28181.api.gb28181.record.dto.GetRecordInfoDTO;
import cn.skcks.docking.gb28181.common.json.JsonResponse;
import cn.skcks.docking.gb28181.config.SwaggerConfig;
import cn.skcks.docking.gb28181.service.record.RecordService;
import cn.skcks.docking.gb28181.service.record.vo.RecordInfoItemVO;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springdoc.core.annotations.ParameterObject;
import org.springdoc.core.models.GroupedOpenApi;
import org.springframework.context.annotation.Bean;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.async.DeferredResult;

import java.util.List;

@Tag(name="历史录像")
@RestController
@JsonMapping("/api/gb28181/record")
@RequiredArgsConstructor
public class RecordController {
    private final RecordService recordService;

    @Bean
    public GroupedOpenApi recordApi() {
        return SwaggerConfig.api("Record", "/api/device/record");
    }

    @GetJson("/list")
    public DeferredResult<JsonResponse<List<RecordInfoItemVO>>> getInfo(@ParameterObject @Validated GetRecordInfoDTO dto){
        return recordService.requestRecordInfo(dto.getGbDeviceId(), dto.getGbDeviceChannelId(), dto.getTimeout(), dto.getDate());
    }
}
