package cn.skcks.docking.gb28181.sip.manscdp.catalog;


import cn.skcks.docking.gb28181.constant.CmdType;
import com.fasterxml.jackson.annotation.JsonRootName;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@JacksonXmlRootElement(localName = "Query")
@JsonRootName("Query")
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Data
public class CatalogQueryDTO {
    @Builder.Default
    private String cmdType = CmdType.CATALOG;
    @JacksonXmlProperty(localName = "SN")
    private String sn;

    /**
     * 目标设备的设备编码(必选)
     */
    @JacksonXmlProperty(localName = "DeviceID")
    private String deviceId;
}
