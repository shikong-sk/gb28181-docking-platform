package cn.skcks.docking.gb28181.core.sip.message.processor.record.dto;

import cn.hutool.core.date.DatePattern;
import cn.skcks.docking.gb28181.core.sip.gb28181.constant.GB28181Constant;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@JacksonXmlRootElement(localName = "Query")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class RecordInfoRequestDTO {
    @Builder.Default
    private String cmdType = "RecordInfo";

    @JacksonXmlProperty(localName = "SN")
    private String sn;

    @JacksonXmlProperty(localName = "DeviceID")
    private String deviceId;

    @NotNull
    @JsonFormat(pattern = DatePattern.UTC_SIMPLE_PATTERN, timezone = GB28181Constant.TIME_ZONE)
    private Date startTime;

    @JsonFormat(pattern = DatePattern.UTC_SIMPLE_PATTERN, timezone = GB28181Constant.TIME_ZONE)
    private Date endTime;

    @Builder.Default
    @Min(value = 0)
    private Integer Secrecy = 0;

    @Builder.Default
    private String type = "all";
}
