package cn.skcks.docking.gb28181.sip.manscdp.keepalive.notify;

import cn.skcks.docking.gb28181.constant.CmdType;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@JacksonXmlRootElement(localName = "Notify")
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Data
public class KeepaliveNotifyDTO {
    @Builder.Default
    private String cmdType = CmdType.KEEPALIVE;

    @JacksonXmlProperty(localName = "SN")
    private String sn;

    @JacksonXmlProperty(localName = "DeviceID")
    private String deviceId;

    @Builder.Default
    private String status = "OK";
}
