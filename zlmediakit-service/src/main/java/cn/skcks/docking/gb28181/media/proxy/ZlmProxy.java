package cn.skcks.docking.gb28181.media.proxy;

import cn.skcks.docking.gb28181.media.config.ZlmMediaConfig;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
public class ZlmProxy {
    private final ZlmMediaConfig mediaConfig;
    private final ZlmMediaHttpClient exchange;

    @Bean
    public ZlmMediaHttpService zlMediaHttpService(){
        return ZlmMediaHttpService.builder()
                .secret(mediaConfig.getSecret())
                .exchange(exchange)
                .build();
    }
}
