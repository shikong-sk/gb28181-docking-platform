package cn.skcks.docking.gb28181.api.play.dto;

import cn.hutool.core.date.DatePattern;
import cn.skcks.docking.gb28181.core.sip.gb28181.constant.GB28181Constant;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

@Schema(title = "关闭历史回放")
@Data
public class RecordStopDTO {
    @NotBlank
    @Schema(description = "设备id", example = "44050100001180000001")
    private String deviceId;

    @NotBlank
    @Schema(description = "通道id", example = "44050100001180000001")
    private String channelId;

    @DateTimeFormat(pattern= DatePattern.NORM_DATETIME_PATTERN)
    @JsonFormat(pattern = DatePattern.UTC_SIMPLE_PATTERN, timezone = GB28181Constant.TIME_ZONE)
    @Schema(description = "开始时间", example = "2023-08-31 00:00:00")
    private Date startTime;

    @DateTimeFormat(pattern= DatePattern.NORM_DATETIME_PATTERN)
    @JsonFormat(pattern = DatePattern.UTC_SIMPLE_PATTERN, timezone = GB28181Constant.TIME_ZONE)
    @Schema(description = "结束时间", example = "2023-08-31 00:15:00")
    private Date endTime;
}
