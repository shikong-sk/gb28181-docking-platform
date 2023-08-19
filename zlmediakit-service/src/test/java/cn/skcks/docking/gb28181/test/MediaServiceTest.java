package cn.skcks.docking.gb28181.test;

import cn.hutool.core.date.LocalDateTimeUtil;
import cn.skcks.docking.gb28181.common.json.JsonResponse;
import cn.skcks.docking.gb28181.media.dto.config.ServerConfig;
import cn.skcks.docking.gb28181.media.dto.response.ZlmResponse;
import cn.skcks.docking.gb28181.media.dto.response.ZlmResponseConvertor;
import cn.skcks.docking.gb28181.media.dto.rtp.CloseRtpServer;
import cn.skcks.docking.gb28181.media.dto.rtp.OpenRtpServer;
import cn.skcks.docking.gb28181.media.proxy.ZlmMediaService;
import lombok.SneakyThrows;
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
import java.time.ZoneOffset;
import java.util.*;

@Slf4j
@SpringBootTest
@EnableAutoConfiguration(exclude={DataSourceAutoConfiguration.class})
@ExtendWith(SpringExtension.class)
public class MediaServiceTest {
    @Autowired
    private ZlmMediaService zlMediaHttpService;

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
    @SneakyThrows
    void testApi(){
        log.info("{}", zlMediaHttpService.getApiList());
        int port = 60000;
        String streamId = "testStream";
        OpenRtpServer openRtpServer = new OpenRtpServer(port,0,streamId);
        log.info("{}", zlMediaHttpService.openRtpServer(openRtpServer));
        Thread.sleep(500);
        CloseRtpServer closeRtpServer = new CloseRtpServer(streamId);
        log.info("{}", zlMediaHttpService.closeRtpServer(closeRtpServer));
    }

    @Test
    void configTest(){
        ZlmResponse<List<ServerConfig>> resp = zlMediaHttpService.getServerConfig();
        log.info("{}", resp);
        ServerConfig config = resp.getData().get(0);
        config.getApi().setApiDebug(0);
        log.info("{}", zlMediaHttpService.setServerConfig(config));

        resp = zlMediaHttpService.getServerConfig();
        log.info("{}", resp);
        config.getApi().setApiDebug(1);
        log.info("{}", zlMediaHttpService.setServerConfig(config));

        resp = zlMediaHttpService.getServerConfig();
        log.info("{}", resp);

        log.info("{}", zlMediaHttpService.setServerConfig(config));
    }
}
