package cn.skcks.docking.gb28181.core.sip.message.processor.record.dto;

import cn.hutool.core.date.DatePattern;
import cn.skcks.docking.gb28181.core.sip.gb28181.constant.GB28181Constant;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;
import lombok.Data;

import java.util.Date;

@JacksonXmlRootElement(localName = "Query")
@Data
public class RecordInfoRequestDTO {
    private String cmdType;

    @JacksonXmlProperty(localName = "SN")
    private String sn;

    @JacksonXmlProperty(localName = "DeviceID")
    private String deviceId;

    @JsonFormat(pattern = DatePattern.UTC_SIMPLE_PATTERN, timezone = GB28181Constant.TIME_ZONE)
    private Date startTime;

    @JsonFormat(pattern = DatePattern.UTC_SIMPLE_PATTERN, timezone = GB28181Constant.TIME_ZONE)
    private Date endTime;
}
