package cn.skcks.docking.gb28181.api.play.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Schema(title = "查询历史录像")
@Data
public class RealTimePlayDTO {
    @NotBlank
    @Schema(description = "设备id", example = "44050100001180000001")
    private String deviceId;

    @NotBlank
    @Schema(description = "通道id", example = "44050100001180000001")
    private String channelId;

    @Min(30)
    @Schema(description = "超时时间(秒)", example = "30")
    private long timeout = 30;
}
