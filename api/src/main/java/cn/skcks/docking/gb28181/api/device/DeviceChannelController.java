package cn.skcks.docking.gb28181.api.device;

import cn.skcks.docking.gb28181.annotation.web.JsonMapping;
import cn.skcks.docking.gb28181.annotation.web.methods.GetJson;
import cn.skcks.docking.gb28181.api.device.dto.DeviceChannelPageDTO;
import cn.skcks.docking.gb28181.common.json.JsonResponse;
import cn.skcks.docking.gb28181.common.page.PageWrapper;
import cn.skcks.docking.gb28181.config.SwaggerConfig;
import cn.skcks.docking.gb28181.orm.mybatis.dynamic.model.DockingDeviceChannel;
import cn.skcks.docking.gb28181.service.device.DeviceChannelService;
import com.github.pagehelper.PageInfo;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springdoc.core.annotations.ParameterObject;
import org.springdoc.core.models.GroupedOpenApi;
import org.springframework.context.annotation.Bean;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "设备通道")
@JsonMapping("/api/device/channel")
@RestController
@RequiredArgsConstructor
public class DeviceChannelController {
    private final DeviceChannelService deviceChannelService;

    @Bean
    public GroupedOpenApi deviceChannelApi() {
        return SwaggerConfig.api("DeviceChannel", "/api/device/channel");
    }

    @Operation(summary = "分页查询 设备通道信息")
    @GetJson("/page")
    public JsonResponse<PageWrapper<DockingDeviceChannel>> getWithPage(@ParameterObject @Validated DeviceChannelPageDTO dto){
        PageInfo<DockingDeviceChannel> withPage = deviceChannelService.getWithPage(dto.getPage(), dto.getSize());
        return JsonResponse.success(PageWrapper.of(withPage));
    }
}
