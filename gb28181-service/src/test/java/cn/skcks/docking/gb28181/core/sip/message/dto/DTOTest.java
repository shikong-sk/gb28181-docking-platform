package cn.skcks.docking.gb28181.core.sip.message.dto;

import cn.hutool.core.date.DateUtil;
import cn.skcks.docking.gb28181.common.json.JsonUtils;
import cn.skcks.docking.gb28181.common.xml.XmlUtils;
import cn.skcks.docking.gb28181.core.sip.message.processor.record.dto.RecordInfoRequestDTO;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;

@Slf4j
public class DTOTest {
    @Test
    void test(){
        RecordInfoRequestDTO dto = new RecordInfoRequestDTO();
        dto.setCmdType("RecordInfo");
        dto.setDeviceId("44050100001310000006");
        dto.setSn("66666");
        dto.setStartTime(DateUtil.beginOfDay(DateUtil.date()));
        dto.setEndTime(DateUtil.date());
        String xml = XmlUtils.toXml(dto);
        log.info("\n{}", xml);
        log.info("{}", XmlUtils.parse(xml,RecordInfoRequestDTO.class));
        log.info("\n{}", JsonUtils.toJson(dto));
    }
}
