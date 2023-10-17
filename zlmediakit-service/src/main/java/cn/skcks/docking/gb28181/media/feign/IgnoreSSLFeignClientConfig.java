package cn.skcks.docking.gb28181.media.feign;

import feign.Client;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.context.annotation.Bean;

public class IgnoreSSLFeignClientConfig {
	
	// 加载自定义Client
	@Bean
    @ConditionalOnBean(IgnoreHttpsSSLClient.class)
    public Client generateClient(IgnoreHttpsSSLClient ignoreHttpsSSLClient) {
        return ignoreHttpsSSLClient.feignClient();
    }
}