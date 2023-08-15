package cn.skcks.docking.gb28181.test;

import cn.skcks.docking.gb28181.media.config.MediaConfig;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Bean;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.web.reactive.function.client.WebClient;

@Slf4j
@SpringBootTest
@ExtendWith(SpringExtension.class)
public class MediaServiceTest {
    @Autowired
    private MediaConfig mediaConfig;

    @Bean
    WebClient webClient(ObjectMapper objectMapper) {
        return WebClient.builder()
                .baseUrl(mediaConfig.getUrl())
                .build();
    }

    @Test
    void context(WebClient webClient){
//        log.info("mediaConfig {}", mediaConfig);
//        HttpServiceProxyFactory httpServiceProxyFactory =
//                HttpServiceProxyFactory.builder(WebClientAdapter.forClient(webClient))
//                        .build();
    }
}
