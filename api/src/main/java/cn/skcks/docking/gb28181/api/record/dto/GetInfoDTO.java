package cn.skcks.docking.gb28181.api.record.dto;

import cn.hutool.core.date.DatePattern;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

@Schema(title = "查询历史录像")
@Data
public class GetInfoDTO {
    @NotBlank
    @Schema(description = "设备id", example = "44050100001180000001")
    private String deviceId;

    @Min(30)
    @Schema(description = "超时时间(秒)", example = "30")
    private long timeout = 30;

    @DateTimeFormat(pattern= DatePattern.NORM_DATE_PATTERN)
    @Schema(description = "查询日期", example = "2023-08-23")
    private Date date;
}
