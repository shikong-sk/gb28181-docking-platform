package cn.skcks.docking.gb28181.sip.manscdp;

import cn.skcks.docking.gb28181.sip.manscdp.catalog.CatalogDeviceListDTO;
import cn.skcks.docking.gb28181.sip.manscdp.catalog.CatalogItemDTO;
import cn.skcks.docking.gb28181.sip.manscdp.catalog.CatalogQueryDTO;
import cn.skcks.docking.gb28181.sip.manscdp.catalog.CatalogResponseDTO;
import cn.skcks.docking.gb28181.sip.utils.MANSCDPUtils;
import com.fasterxml.jackson.core.type.TypeReference;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;

import java.util.Collections;

@Slf4j
public class MANSCDPTest {
    public static final String deviceId = "44050100001110000010";
    public static final String channelId = "44050100001310000010";
    public static final String deviceName = "模拟设备名称";

    @Test
    public void catalog(){
        String sn = String.valueOf(1);
        // 创建 目录查询 请求
        CatalogQueryDTO catalogQueryDTO = CatalogQueryDTO.builder()
                .deviceId(deviceId)
                .sn(sn)
                .build();
        log.info("\n{}", MANSCDPUtils.toXml(catalogQueryDTO));

        MANSCDPUtils.XMLWrapper<CatalogQueryDTO> query = MANSCDPUtils.XMLWrapper.<CatalogQueryDTO>builder()
                .query(catalogQueryDTO)
                .build();
        String serialize = MANSCDPUtils.toXml(query);
        log.info("\n{}", serialize);

        MANSCDPUtils.XMLWrapper<CatalogQueryDTO> deserializeCatalogQueryDTO = MANSCDPUtils.parse(serialize, new TypeReference<>(){});
        log.info("\n{}",deserializeCatalogQueryDTO);
        if (deserializeCatalogQueryDTO == null) {
            return;
        }
        CatalogQueryDTO queryDTO = deserializeCatalogQueryDTO.getQuery();
        log.info("{}",queryDTO);

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
        MANSCDPUtils.XMLWrapper<CatalogResponseDTO> response = MANSCDPUtils.XMLWrapper.<CatalogResponseDTO>builder()
                .response(catalogResponseDTO)
                .build();
        serialize = MANSCDPUtils.toXml(response);
        log.info("\n{}", serialize);
        MANSCDPUtils.XMLWrapper<CatalogResponseDTO> deserializeCatalogResponseDTO = MANSCDPUtils.parse(serialize, new TypeReference<>(){});
        if (deserializeCatalogResponseDTO == null) {
            return;
        }
        CatalogResponseDTO responseDTO = deserializeCatalogResponseDTO.getResponse();
        log.info("{}",responseDTO);
    }
}
