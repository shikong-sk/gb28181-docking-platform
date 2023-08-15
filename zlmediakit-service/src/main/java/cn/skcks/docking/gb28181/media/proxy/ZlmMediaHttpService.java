package cn.skcks.docking.gb28181.media.proxy;

import cn.skcks.docking.gb28181.common.json.JsonResponse;
import cn.skcks.docking.gb28181.media.dto.config.ServerConfig;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.service.annotation.GetExchange;
import org.springframework.web.service.annotation.HttpExchange;

import java.util.List;

@HttpExchange
public interface ZlmMediaHttpService {

    @GetExchange("/index/api/getServerConfig")
    JsonResponse<List<ServerConfig>> getServerConfig(@RequestParam String secret);
}
