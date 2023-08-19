package cn.skcks.docking.gb28181.test;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.date.LocalDateTimeUtil;
import cn.hutool.core.date.ZoneUtil;
import cn.skcks.docking.gb28181.common.json.JsonResponse;
import cn.skcks.docking.gb28181.common.json.JsonUtils;
import cn.skcks.docking.gb28181.media.config.ZlmMediaConfig;
import cn.skcks.docking.gb28181.media.dto.config.ApiConfig;
import cn.skcks.docking.gb28181.media.dto.config.ServerConfig;
import cn.skcks.docking.gb28181.media.dto.response.ZlmResponse;
import cn.skcks.docking.gb28181.media.dto.response.ZlmResponseConvertor;
import cn.skcks.docking.gb28181.media.proxy.ZlmMediaHttpService;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.util.*;

@Slf4j
@SpringBootTest
@EnableAutoConfiguration(exclude={DataSourceAutoConfiguration.class})
@ExtendWith(SpringExtension.class)
public class MediaServiceTest {
    @Autowired
    private ZlmMediaHttpService zlMediaHttpService;

    @Test
    void test(){
        long unix = 1692346604L;
        Instant instant = Instant.ofEpochSecond(unix);

        log.info("{}", LocalDateTime.ofEpochSecond(unix, 0, TimeZone.getTimeZone("UTC").toZoneId().getRules().getOffset(instant)));
        log.info("{}", LocalDateTimeUtil.of(instant, TimeZone.getTimeZone("UTC")));
        log.info("{}", LocalDateTime.ofEpochSecond(unix, 0, ZoneOffset.ofHours(0)));

        log.info("{}", LocalDateTime.ofEpochSecond(unix, 0, TimeZone.getTimeZone("Asia/Shanghai").toZoneId().getRules().getOffset(instant)));
        log.info("{}", LocalDateTimeUtil.of(instant, TimeZone.getTimeZone("GMT+8")));
        log.info("{}", LocalDateTime.ofEpochSecond(unix, 0, ZoneOffset.ofHours(8)));
    }

    @Test
    void context(){
        ResponseEntity<String> entity = zlMediaHttpService.getServerConfigResponseEntity();
        log.info("{}", entity.getBody());

        ZlmResponse<List<ServerConfig>> test = zlMediaHttpService.getServerConfig();
        JsonResponse<List<ServerConfig>> jsonResponse = test.getJsonResponse();
        log.info("{}", jsonResponse);

        ZlmResponse<List<ServerConfig>> zlmResponse = ZlmResponseConvertor.INSTANCE.toZlmResponse(jsonResponse);
        log.info("{}", zlmResponse);
        log.info("{}", zlmResponse.getCode().getMsg());
    }

    @Test
    void testApi(){
        log.info("{}", zlMediaHttpService.getApiList());
    }

    @Test
    void httpBinTest(){
        ZlmResponse<List<ServerConfig>> resp = zlMediaHttpService.getServerConfig();
        ServerConfig config = resp.getData().get(0);
        log.info("{}", zlMediaHttpService.setServerConfig(config));
    }
}
