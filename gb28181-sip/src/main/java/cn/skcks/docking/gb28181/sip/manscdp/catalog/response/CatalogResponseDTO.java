package cn.skcks.docking.gb28181.sip.manscdp.catalog.response;

import cn.skcks.docking.gb28181.constant.CmdType;
import cn.skcks.docking.gb28181.sip.manscdp.catalog.query.CatalogDeviceListDTO;
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
public class CatalogResponseDTO {
    @Builder.Default
    private String cmdType = CmdType.CATALOG;
    @JacksonXmlProperty(localName = "SN")
    private String sn;

    /**
     * 目标设备的设备编码(必选)
     */
    @JacksonXmlProperty(localName = "DeviceID")
    private String deviceId;

    private Long sumNum;

    private CatalogDeviceListDTO deviceList;
}
