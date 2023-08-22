package cn.skcks.docking.gb28181.core.sip.message.processor.message.types.recordinfo.reponse.dto;

import cn.hutool.core.date.DatePattern;
import cn.skcks.docking.gb28181.core.sip.gb28181.constant.GB28181Constant;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;
import jakarta.validation.constraints.Min;
import lombok.Builder;
import lombok.Data;

import java.util.Date;

@Data
@JacksonXmlRootElement(localName = "Item")
public class RecordInfoItemDTO {
    /**
     * 目标设备的设备编码(必选)
     */
    @JacksonXmlProperty(localName = "DeviceID")
    private String deviceId;

    private String name;

    private String address;

    @JsonFormat(pattern = DatePattern.UTC_SIMPLE_PATTERN, timezone = GB28181Constant.TIME_ZONE)
    private Date startTime;

    @JsonFormat(pattern = DatePattern.UTC_SIMPLE_PATTERN, timezone = GB28181Constant.TIME_ZONE)
    private Date endTime;

    @Min(value = 0)
    private Integer Secrecy = 0;

    private String type = "all";

    private Long fileSize;
}
