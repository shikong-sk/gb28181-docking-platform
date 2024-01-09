package cn.skcks.docking.gb28181.sip.manscdp.control;

import cn.skcks.docking.gb28181.constant.CmdType;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@JacksonXmlRootElement(localName = "Control")
@JsonInclude(JsonInclude.Include.NON_EMPTY)
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Data
public class DeviceControlRequestDTO {
    @Builder.Default
    private String cmdType = CmdType.DEVICE_CONTROL;

    @JacksonXmlProperty(localName = "SN")
    private String sn;

    /**
     * 目标设备的设备编码(必选)
     */
    @JacksonXmlProperty(localName = "DeviceID")
    private String deviceId;

    /**
     * 录像控制命令
     */
    private String recordCmd;

    /**
     * 云台控制命令
     */
    @JacksonXmlProperty(localName = "PTZCmd")
    private String ptzCmd;

    /**
     * 远程启动
     */
    private String teleBoot;

    /**
     * 布防撤防
     */
    private String guardCmd;

    /**
     * 告警控制
     */
    private String alarmCmd;

    /**
     * 强制关键帧
     */
    @JacksonXmlProperty(localName = "IFameCmd")
    private String iFameCmd;

    /**
     * 拉框放大
     */
    private String dragZoomIn;

    /**
     * 拉框缩小
     */
    private String dragZoomOut;

    /**
     * 看守位
     */
    private String homePosition;
}
