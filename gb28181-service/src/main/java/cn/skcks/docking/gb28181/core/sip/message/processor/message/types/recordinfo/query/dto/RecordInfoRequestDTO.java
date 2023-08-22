package cn.skcks.docking.gb28181.core.sip.message.processor.message.types.recordinfo.query.dto;

import cn.hutool.core.date.DatePattern;
import cn.skcks.docking.gb28181.core.sip.gb28181.constant.CmdType;
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
    /**
     * 命令类型:设备信息查询(必选)
     */
    @Builder.Default
    private String cmdType = CmdType.RECORD_INFO;

    /**
     * 命令序列号(必选)
     */
    @JacksonXmlProperty(localName = "SN")
    private String sn;

    /**
     * 目标设备的设备编码(必选)
     */
    @JacksonXmlProperty(localName = "DeviceID")
    private String deviceId;

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
