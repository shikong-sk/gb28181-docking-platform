package cn.skcks.docking.gb28181.api.play.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Schema(title = "关闭点播")
@Data
public class RealTimeStopDTO {
    @NotBlank
    @Schema(description = "设备id", example = "44050100001180000001")
    private String deviceId;

    @NotBlank
    @Schema(description = "通道id", example = "44050100001180000001")
    private String channelId;
}
