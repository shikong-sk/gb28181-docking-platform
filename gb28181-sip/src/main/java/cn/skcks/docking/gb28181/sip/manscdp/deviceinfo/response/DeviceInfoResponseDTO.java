package cn.skcks.docking.gb28181.sip.manscdp.deviceinfo.response;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@JacksonXmlRootElement(localName = "Response")
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
public class DeviceInfoResponseDTO {
    @Builder.Default
    private String cmdType = "DeviceInfo";
    @JacksonXmlProperty(localName = "SN")
    private String sn;

    @JacksonXmlProperty(localName = "DeviceID")
    private String deviceId;

    private String deviceName;

    @Builder.Default
    private String Result = "OK";

    /**
     * 设备生产商
     */
    private String manufacturer;

    /**
     * 设备型号(可选)
     */
    private String model;

    /**
     * 设备固件版本(可选)
     */
    private String firmware;

    /**
     * 视频输入通道数(可选)
     */
    private Integer channel;
}
