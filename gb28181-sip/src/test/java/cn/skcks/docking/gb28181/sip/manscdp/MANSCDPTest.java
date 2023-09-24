package cn.skcks.docking.gb28181.sip.manscdp;

import cn.skcks.docking.gb28181.sip.manscdp.catalog.response.CatalogDeviceListDTO;
import cn.skcks.docking.gb28181.sip.manscdp.catalog.response.CatalogItemDTO;
import cn.skcks.docking.gb28181.sip.manscdp.catalog.query.CatalogQueryDTO;
import cn.skcks.docking.gb28181.sip.manscdp.catalog.response.CatalogResponseDTO;
import cn.skcks.docking.gb28181.sip.manscdp.deviceinfo.request.DeviceInfoRequestDTO;
import cn.skcks.docking.gb28181.sip.manscdp.deviceinfo.response.DeviceInfoResponseDTO;
import cn.skcks.docking.gb28181.sip.manscdp.keepalive.notify.KeepaliveNotifyDTO;
import cn.skcks.docking.gb28181.sip.manscdp.recordinfo.request.RecordInfoRequestDTO;
import cn.skcks.docking.gb28181.sip.manscdp.recordinfo.response.RecordInfoItemDTO;
import cn.skcks.docking.gb28181.sip.manscdp.recordinfo.response.RecordInfoResponseDTO;
import cn.skcks.docking.gb28181.sip.manscdp.recordinfo.response.RecordListDTO;
import cn.skcks.docking.gb28181.sip.utils.MANSCDPUtils;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Slf4j
public class MANSCDPTest {
    public static final String deviceId = "44050100001110000010";
    public static final String channelId = "44050100001310000010";
    public static final String deviceName = "模拟设备名称";

    public static final Integer channel = 1;
    public static final String manufacturer = "gb28181-docking-platform";
    public static String sn = String.valueOf(1);

    @Test
    public void keepalive() {
        KeepaliveNotifyDTO keepaliveNotifyDTO = KeepaliveNotifyDTO.builder()
                .deviceId(deviceId)
                .sn(sn)
                .build();

        MANSCDPUtils.XMLBuilder<KeepaliveNotifyDTO> xmlBuilder = MANSCDPUtils.XMLBuilder.<KeepaliveNotifyDTO>builder()
                .data(keepaliveNotifyDTO)
                .build();

        log.info("\n{}", MANSCDPUtils.toXml(xmlBuilder));
    }

    @Test
    public void catalog() {

        // 创建 目录查询 请求
        CatalogQueryDTO catalogQueryDTO = CatalogQueryDTO.builder()
                .deviceId(deviceId)
                .sn(sn)
                .build();
        log.info("\n{}", MANSCDPUtils.toXml(catalogQueryDTO));

        MANSCDPUtils.XMLBuilder<CatalogQueryDTO> query = new MANSCDPUtils.XMLBuilder<>(catalogQueryDTO);
        String serialize = MANSCDPUtils.toXml(query);
        log.info("\n{}", serialize);

        CatalogQueryDTO queryDTO = MANSCDPUtils.parse(serialize, CatalogQueryDTO.class);
        log.info("\n{}", queryDTO);

        // 创建 目录 查询响应
        CatalogItemDTO catalogItemDTO = CatalogItemDTO.builder()
                .deviceId(channelId)
                .name(deviceName)
                .manufacturer(manufacturer)
                .build();

        List<CatalogItemDTO> itemDTOList = new ArrayList<>(2);
        itemDTOList.add(catalogItemDTO);
        itemDTOList.add(catalogItemDTO);
        CatalogResponseDTO catalogResponseDTO = CatalogResponseDTO.builder()
                .deviceId(deviceId)
                .sn(sn)
                .deviceList(new CatalogDeviceListDTO(itemDTOList.size(), itemDTOList))
                .sumNum(2L)
                .build();
        // 默认 GB2312 编码
        MANSCDPUtils.XMLBuilder<CatalogResponseDTO> response = MANSCDPUtils.XMLBuilder.<CatalogResponseDTO>builder()
                .data(catalogResponseDTO)
                .build();
        serialize = MANSCDPUtils.toXml(response);
        log.info("\n{}", serialize);
        CatalogResponseDTO responseDTO = MANSCDPUtils.parse(serialize, CatalogResponseDTO.class);
        log.info("{}", responseDTO);
        // UTF8 编码
        response = MANSCDPUtils.XMLBuilder.<CatalogResponseDTO>builder()
                .encoding(StandardCharsets.UTF_8.name())
                .data(catalogResponseDTO)
                .build();
        serialize = MANSCDPUtils.toXml(response);
        log.info("\n{}", serialize);
        responseDTO = MANSCDPUtils.parse(serialize, CatalogResponseDTO.class);
        log.info("{}", responseDTO);
    }

    @Test
    public void deviceInfo() {
        DeviceInfoRequestDTO deviceInfoRequestDTO = DeviceInfoRequestDTO.builder()
                .deviceId(deviceId)
                .sn(sn)
                .build();
        MANSCDPUtils.XMLBuilder<DeviceInfoRequestDTO> deviceInfoRequestDTOXMLBuilder = MANSCDPUtils.build(deviceInfoRequestDTO);
        log.info("\n{}", MANSCDPUtils.toXml(deviceInfoRequestDTOXMLBuilder));


        DeviceInfoResponseDTO deviceInfoResponseDTO = DeviceInfoResponseDTO.builder()
                .deviceId(deviceId)
                .channel(channel)
                .deviceName(deviceName)
                .manufacturer(manufacturer)
                .build();

        MANSCDPUtils.XMLBuilder<DeviceInfoResponseDTO> deviceInfoResponseDTOXMLBuilder = MANSCDPUtils.build(deviceInfoResponseDTO);
        String xml = MANSCDPUtils.toXml(deviceInfoResponseDTOXMLBuilder);
        log.info("\n{}", xml);
        DeviceInfoResponseDTO parse = MANSCDPUtils.parse(xml, DeviceInfoResponseDTO.class);
        if (parse != null) {
            log.info("getDeviceName {}", parse.getDeviceName());
        }
    }

    @Test
    public void recordInfo() {
        RecordInfoRequestDTO recordInfoRequestDTO = RecordInfoRequestDTO.builder()
                .type("all")
                .startTime(new Date())
                .endTime(new Date())
                .deviceId(deviceId)
                .sn(sn)
                .build();
        MANSCDPUtils.XMLBuilder<RecordInfoRequestDTO> recordInfoRequestDTOXMLBuilder = MANSCDPUtils.build(recordInfoRequestDTO);
        log.info("\n{}", MANSCDPUtils.toXml(recordInfoRequestDTOXMLBuilder));


        RecordInfoItemDTO recordInfoItemDTO = RecordInfoItemDTO.builder()
                .deviceId(deviceId)
                .startTime(new Date())
                .endTime(new Date())
                .name("record.mp4")
                .fileSize(0L)
                .build();
        List<RecordInfoItemDTO> recordInfoItemDTOList = new ArrayList<>(2);
        recordInfoItemDTOList.add(recordInfoItemDTO);
        recordInfoItemDTOList.add(recordInfoItemDTO);
        RecordInfoResponseDTO recordInfoResponseDTO = RecordInfoResponseDTO.builder()
                .name(deviceName)
                .deviceId(deviceId)
                .sn(sn)
                .sumNum(2L)
                .recordList(RecordListDTO.builder()
                        .num(2)
                        .recordList(recordInfoItemDTOList)
                        .build())
                .build();
        MANSCDPUtils.XMLBuilder<RecordInfoResponseDTO> recordInfoResponseDTOXMLBuilder = MANSCDPUtils.build(recordInfoResponseDTO, "UTF8");
        String xml = MANSCDPUtils.toXml(recordInfoResponseDTOXMLBuilder);
        log.info("\n{}", xml);

        MessageDTO messageDTO = MANSCDPUtils.parse(xml.getBytes(), MessageDTO.class);
        log.info("{}", messageDTO);
    }
}
