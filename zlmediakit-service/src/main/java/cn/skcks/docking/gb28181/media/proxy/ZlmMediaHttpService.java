package cn.skcks.docking.gb28181.media.proxy;

import cn.skcks.docking.gb28181.media.dto.config.ServerConfig;
import cn.skcks.docking.gb28181.media.dto.response.ZlmResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.service.annotation.GetExchange;
import org.springframework.web.service.annotation.HttpExchange;

import java.util.List;

@HttpExchange
public interface ZlmMediaHttpService {

    @GetExchange("/index/api/getServerConfig")
    ZlmResponse<List<ServerConfig>> getServerConfig(@RequestParam String secret);

    @GetExchange("/index/api/getServerConfig")
    ResponseEntity<String> getServerConfigResponseEntity(@RequestParam String secret);
}
