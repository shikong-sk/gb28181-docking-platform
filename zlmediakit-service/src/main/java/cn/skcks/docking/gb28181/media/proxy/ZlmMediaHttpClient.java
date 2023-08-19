package cn.skcks.docking.gb28181.media.proxy;

import cn.skcks.docking.gb28181.media.dto.config.ServerConfig;
import cn.skcks.docking.gb28181.media.dto.response.ZlmResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.cloud.openfeign.SpringQueryMap;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.service.annotation.HttpExchange;

import java.util.List;
import java.util.Map;

@FeignClient(name="zlmMediaServerProxy",url = "${media.url}")
@HttpExchange
public interface ZlmMediaHttpClient {
    @GetMapping("/index/api/getApiList")
    ZlmResponse<List<String>> getApiList(@RequestParam String secret);

    @GetMapping("/index/api/getServerConfig")
    ZlmResponse<List<ServerConfig>> getServerConfig(@RequestParam String secret);

    @GetMapping("/index/api/getServerConfig")
    ResponseEntity<String> getServerConfigResponseEntity(@RequestParam String secret);

    @GetMapping("/index/api/setServerConfig")
    String setServerConfig(@RequestParam String secret, @SpringQueryMap Map<String,Object> config);
}
