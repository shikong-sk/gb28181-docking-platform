package cn.skcks.docking.gb28181.sip.manscdp;

import cn.skcks.docking.gb28181.sip.manscdp.catalog.CatalogDeviceListDTO;
import cn.skcks.docking.gb28181.sip.manscdp.catalog.CatalogItemDTO;
import cn.skcks.docking.gb28181.sip.manscdp.catalog.CatalogQueryDTO;
import cn.skcks.docking.gb28181.sip.manscdp.catalog.CatalogResponseDTO;
import cn.skcks.docking.gb28181.sip.manscdp.keepalive.KeepaliveNotifyDTO;
import cn.skcks.docking.gb28181.sip.utils.MANSCDPUtils;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;

import java.nio.charset.StandardCharsets;
import java.util.Collections;

@Slf4j
public class MANSCDPTest {
    public static final String deviceId = "44050100001110000010";
    public static final String channelId = "44050100001310000010";
    public static final String deviceName = "模拟设备名称";
    public static String sn = String.valueOf(1);

    @Test
    public void keepalive(){
        KeepaliveNotifyDTO keepaliveNotifyDTO = KeepaliveNotifyDTO.builder()
                .deviceId(deviceId)
                .sn(sn)
                .build();

        MANSCDPUtils.XMLBuilder<KeepaliveNotifyDTO> xmlBuilder = MANSCDPUtils.XMLBuilder.<KeepaliveNotifyDTO>builder()
                .data(keepaliveNotifyDTO)
                .build();

        log.info("\n{}",MANSCDPUtils.toXml(xmlBuilder));
    }

    @Test
    public void catalog(){

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
        log.info("\n{}",queryDTO);

        // 创建 目录 查询响应
        CatalogItemDTO catalogItemDTO = CatalogItemDTO.builder()
                .deviceId(channelId)
                .name(deviceName)
                .manufacturer("gb28181-docking-platform")
                .build();
        CatalogResponseDTO catalogResponseDTO = CatalogResponseDTO.builder()
                .deviceId(deviceId)
                .sn(sn)
                .deviceList(new CatalogDeviceListDTO(1,Collections.singletonList(catalogItemDTO)))
                .sumNum(0L)
                .build();
        // 默认 GB2312 编码
        MANSCDPUtils.XMLBuilder<CatalogResponseDTO> response = MANSCDPUtils.XMLBuilder.<CatalogResponseDTO>builder()
                .data(catalogResponseDTO)
                .build();
        serialize = MANSCDPUtils.toXml(response);
        log.info("\n{}", serialize);
        CatalogResponseDTO responseDTO = MANSCDPUtils.parse(serialize, CatalogResponseDTO.class);
        log.info("{}",responseDTO);
        // UTF8 编码
        response = MANSCDPUtils.XMLBuilder.<CatalogResponseDTO>builder()
                .encoding(StandardCharsets.UTF_8.name())
                .data(catalogResponseDTO)
                .build();
        serialize = MANSCDPUtils.toXml(response);
        log.info("\n{}", serialize);
        responseDTO = MANSCDPUtils.parse(serialize, CatalogResponseDTO.class);
        log.info("{}",responseDTO);
    }
}
