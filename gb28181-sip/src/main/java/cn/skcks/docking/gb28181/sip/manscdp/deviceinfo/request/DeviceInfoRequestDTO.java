package cn.skcks.docking.gb28181.sip.manscdp.deviceinfo.request;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@JacksonXmlRootElement(localName = "Query")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DeviceInfoRequestDTO {
    @Builder.Default
    private String cmdType = "DeviceInfo";

    @JacksonXmlProperty(localName = "SN")
    private String sn;

    @JacksonXmlProperty(localName = "DeviceID")
    private String deviceId;
}
