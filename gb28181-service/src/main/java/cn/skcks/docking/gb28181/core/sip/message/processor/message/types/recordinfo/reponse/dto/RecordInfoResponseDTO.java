package cn.skcks.docking.gb28181.core.sip.message.processor.message.types.recordinfo.reponse.dto;

import cn.skcks.docking.gb28181.core.sip.gb28181.constant.CmdType;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlElementWrapper;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;
import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@JacksonXmlRootElement(localName = "Response")
public class RecordInfoResponseDTO {
    /**
     * 命令类型:设备信息查询(必选)
     */
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

    private String name;

    private Long sumNum;

    @JacksonXmlElementWrapper
    private List<RecordInfoItemDTO> recordList;
}
