package cn.skcks.docking.gb28181.sip.manscdp.mediastatus.notify;

import cn.skcks.docking.gb28181.constant.CmdType;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@JacksonXmlRootElement(localName = "Notify")
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
public class MediaStatusRequestDTO {
    @Builder.Default
    private String cmdType = CmdType.MEDIA_STATUS;
    @JacksonXmlProperty(localName = "SN")
    private String sn;

    /**
     * 目标设备的设备编码(必选)
     */
    @JacksonXmlProperty(localName = "DeviceID")
    private String deviceId;

    @JacksonXmlProperty(localName = "NotifyType")
    private String notifyType = "121";
}
