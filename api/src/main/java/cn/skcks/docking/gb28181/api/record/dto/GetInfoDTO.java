package cn.skcks.docking.gb28181.api.record.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Schema(title = "查询历史录像")
@Data
public class GetInfoDTO {
    @Schema(description = "设备id")
    private String deviceId;
}
