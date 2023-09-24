package cn.skcks.docking.gb28181.sip.manscdp.recordinfo.response;

import cn.skcks.docking.gb28181.sip.manscdp.catalog.response.CatalogItemDTO;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlElementWrapper;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@JacksonXmlRootElement(localName = "RecordList")
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Data
public class RecordListDTO {
    @Builder.Default
    @JacksonXmlProperty(isAttribute = true)
    private Integer num = 0;

    @Builder.Default
    @JacksonXmlProperty(localName = "Item")
    @JacksonXmlElementWrapper(useWrapping = false)
    private List<RecordInfoItemDTO> recordList = new ArrayList<>();
}
