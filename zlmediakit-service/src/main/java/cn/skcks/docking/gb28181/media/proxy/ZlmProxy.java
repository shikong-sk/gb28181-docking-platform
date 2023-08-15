package cn.skcks.docking.gb28181.media.proxy;

import cn.skcks.docking.gb28181.media.config.ZlmMediaConfig;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.support.WebClientAdapter;
import org.springframework.web.service.invoker.HttpServiceProxyFactory;

@RequiredArgsConstructor
@Component
public class ZlmProxy {
    private final ZlmMediaConfig mediaConfig;

    @Bean
    public WebClient zlmClient() {
        return WebClient.builder()
                .baseUrl(mediaConfig.getUrl())
                .build();
    }

    @Bean
    public ZlmMediaHttpService zlMediaHttpService(@Qualifier("zlmClient") WebClient zlmClient){
        HttpServiceProxyFactory proxyFactory =
                HttpServiceProxyFactory.builder(WebClientAdapter.forClient(zlmClient))
                        .build();
        return proxyFactory.createClient(ZlmMediaHttpService.class);
    }
}
