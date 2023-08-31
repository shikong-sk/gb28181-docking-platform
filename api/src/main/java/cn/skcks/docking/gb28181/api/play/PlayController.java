package cn.skcks.docking.gb28181.api.play;

import cn.skcks.docking.gb28181.annotation.web.JsonMapping;
import cn.skcks.docking.gb28181.annotation.web.methods.GetJson;
import cn.skcks.docking.gb28181.api.play.dto.RealTimePlayDTO;
import cn.skcks.docking.gb28181.api.play.dto.RealTimeStopDTO;
import cn.skcks.docking.gb28181.api.play.dto.RecordPlayDTO;
import cn.skcks.docking.gb28181.api.play.dto.RecordStopDTO;
import cn.skcks.docking.gb28181.common.json.JsonResponse;
import cn.skcks.docking.gb28181.config.SwaggerConfig;
import cn.skcks.docking.gb28181.service.play.PlayService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springdoc.core.annotations.ParameterObject;
import org.springdoc.core.models.GroupedOpenApi;
import org.springframework.context.annotation.Bean;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.async.DeferredResult;

@Tag(name = "播放")
@RestController
@JsonMapping("/api/device/play")
@RequiredArgsConstructor
public class PlayController {
    private final PlayService playService;

    @Bean
    public GroupedOpenApi playApi() {
        return SwaggerConfig.api("Play", "/api/device/play");
    }

    @GetJson("/realTimePlay")
    public DeferredResult<JsonResponse<String>> realTimePlay(@ParameterObject @Validated RealTimePlayDTO dto) {
        return playService.realTimePlay(dto.getDeviceId(), dto.getChannelId(), dto.getTimeout());
    }

    @GetJson("/realtimeStop")
    public JsonResponse<Void> realTimeStop(@ParameterObject @Validated RealTimeStopDTO dto) {
        return playService.realTimeStop(dto.getDeviceId(), dto.getChannelId());
    }

    @GetJson("/recordPlay")
    public DeferredResult<JsonResponse<String>> recordPlay(@ParameterObject @Validated RecordPlayDTO dto) {
        return playService.recordPlay(dto.getDeviceId(), dto.getChannelId(), dto.getStartTime(), dto.getEndTime(), dto.getTimeout());
    }

    @GetJson("/recordStop")
    public JsonResponse<Void> recordStop(@ParameterObject @Validated RecordStopDTO dto) {
        return playService.recordStop(dto.getDeviceId(), dto.getChannelId(), dto.getStartTime(), dto.getEndTime());
    }
}
