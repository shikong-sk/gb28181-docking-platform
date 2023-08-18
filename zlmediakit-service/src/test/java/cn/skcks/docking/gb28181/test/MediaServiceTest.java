package cn.skcks.docking.gb28181.test;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.date.LocalDateTimeUtil;
import cn.hutool.core.date.ZoneUtil;
import cn.skcks.docking.gb28181.common.json.JsonResponse;
import cn.skcks.docking.gb28181.media.config.ZlmMediaConfig;
import cn.skcks.docking.gb28181.media.dto.config.ServerConfig;
import cn.skcks.docking.gb28181.media.dto.response.ZlmResponse;
import cn.skcks.docking.gb28181.media.proxy.ZlmMediaHttpService;
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
    private ZlmMediaConfig config;

    @Autowired
    private ZlmMediaHttpService zlMediaHttpService;

    @Test
    void context(){
        String secret = config.getSecret();
        ResponseEntity<String> entity = zlMediaHttpService.getServerConfigResponseEntity(secret);
        log.info("{}", entity.getBody());

        ZlmResponse<List<ServerConfig>> test = zlMediaHttpService.getServerConfig(secret);
        log.info("{}", test);
        log.info("{}",test.getCode().getMsg());
    }

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
}
