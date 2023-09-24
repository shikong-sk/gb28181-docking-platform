package cn.skcks.docking.gb28181.sip.manscdp.recordinfo.response;

import cn.skcks.docking.gb28181.constant.GB28181Constant;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Builder
@NoArgsConstructor
@AllArgsConstructor
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

    @JsonFormat(pattern = GB28181Constant.DATETIME_FORMAT, timezone = GB28181Constant.TIME_ZONE)
    private Date startTime;

    @JsonFormat(pattern = GB28181Constant.DATETIME_FORMAT, timezone = GB28181Constant.TIME_ZONE)
    private Date endTime;

    @Builder.Default
    private Integer secrecy = 0;

    @Builder.Default
    private String type = "all";

    private Long fileSize;
}
