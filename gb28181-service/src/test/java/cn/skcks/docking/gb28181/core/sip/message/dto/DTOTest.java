package cn.skcks.docking.gb28181.core.sip.message.dto;

import cn.hutool.core.date.DateUtil;
import cn.skcks.docking.gb28181.common.json.JsonUtils;
import cn.skcks.docking.gb28181.common.xml.XmlUtils;
import cn.skcks.docking.gb28181.core.sip.message.processor.message.types.recordinfo.query.dto.RecordInfoRequestDTO;
import cn.skcks.docking.gb28181.core.sip.message.processor.message.types.recordinfo.reponse.dto.RecordInfoResponseDTO;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;

@Slf4j
public class DTOTest {
    @Test
    void recordInfoTest(){
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

        String response = "<?xml version=\"1.0\" encoding=\"GB2312\"?>\n" +
                "<Response>\n" +
                "<CmdType>RecordInfo</CmdType>\n" +
                "<SN>100000</SN>\n" +
                "<DeviceID>44050100001110000009</DeviceID>\n" +
                "<Name>Happytimesoft</Name>\n" +
                "<SumNum>10</SumNum>\n" +
                "<RecordList Num=\"2\">\n" +
                "<Item>\n" +
                "<DeviceID>44050100001110000009</DeviceID>\n" +
                "<Name>Happytimesoft</Name>\n" +
                "<Address></Address>\n" +
                "<StartTime>2023-08-22T00:00:00</StartTime>\n" +
                "<EndTime>2023-08-22T00:10:00</EndTime>\n" +
                "<Secrecy>0</Secrecy>\n" +
                "<Type>time</Type>\n" +
                "<FileSize>10485760</FileSize>\n" +
                "</Item>\n" +
                "<Item>\n" +
                "<DeviceID>44050100001110000009</DeviceID>\n" +
                "<Name>Happytimesoft</Name>\n" +
                "<Address></Address>\n" +
                "<StartTime>2023-08-22T00:10:00</StartTime>\n" +
                "<EndTime>2023-08-22T00:20:00</EndTime>\n" +
                "<Secrecy>0</Secrecy>\n" +
                "<Type>time</Type>\n" +
                "<FileSize>10485760</FileSize>\n" +
                "</Item>\n" +
                "</RecordList>\n" +
                "</Response>";
        RecordInfoResponseDTO responseDTO = XmlUtils.parse(response, RecordInfoResponseDTO.class);
        log.info("{}", responseDTO);
        log.info("\n{}", JsonUtils.toJson(responseDTO));
    }
}
