package cn.skcks.docking.gb28181.sip.manscdp.catalog;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlElementWrapper;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@JacksonXmlRootElement(localName = "DeviceList")
@AllArgsConstructor
@NoArgsConstructor
@Data
public class CatalogDeviceListDTO {
    @JacksonXmlProperty(isAttribute = true)
    private Integer num;
    @JacksonXmlProperty(localName = "Item")
    @JacksonXmlElementWrapper(useWrapping = false)
    private List<CatalogItemDTO> deviceList;
}
