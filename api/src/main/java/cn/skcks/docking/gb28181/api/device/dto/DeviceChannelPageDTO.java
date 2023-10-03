package cn.skcks.docking.gb28181.api.device.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class DeviceChannelPageDTO {
    @Schema(description = "页数", example = "1")
    @Min(value = 1, message = "page 必须为正整数")
    Integer page;

    @Schema(description = "每页条数", example = "10")
    @Min(value = 1, message = "size 必须为正整数")
    Integer size;
}
