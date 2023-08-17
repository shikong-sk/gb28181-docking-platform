package cn.skcks.docking.gb28181.test;

import cn.skcks.docking.gb28181.common.json.JsonResponse;
import cn.skcks.docking.gb28181.media.config.ZlmMediaConfig;
import cn.skcks.docking.gb28181.media.dto.config.ServerConfig;
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

import java.util.List;

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

        JsonResponse<List<ServerConfig>> test = zlMediaHttpService.getServerConfig(secret);
        log.info("{}", test);
    }
}
