package cn.skcks.docking.gb28181.api.play;

import cn.skcks.docking.gb28181.annotation.web.JsonMapping;
import cn.skcks.docking.gb28181.annotation.web.methods.GetJson;
import cn.skcks.docking.gb28181.api.play.dto.RealTimePlayDTO;
import cn.skcks.docking.gb28181.api.play.dto.RealTimeStopDTO;
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

@Tag(name="播放")
@RestController
@JsonMapping("/device/play")
@RequiredArgsConstructor
public class PlayController {
    private final PlayService playService;

    @Bean
    public GroupedOpenApi playApi() {
        return SwaggerConfig.api("Play", "/device/play");
    }

    @GetJson("/realTimePlay")
    public DeferredResult<JsonResponse<String>> realTimePlay(@ParameterObject @Validated RealTimePlayDTO dto){
        return playService.realTimePlay(dto.getDeviceId(), dto.getChannelId(), dto.getTimeout());
    }

    @GetJson("/realtimeStop")
    public  JsonResponse<Void> realTimeStop(@ParameterObject @Validated RealTimeStopDTO dto){
        return playService.realTimeStop(dto.getDeviceId(), dto.getChannelId());
    }
}
