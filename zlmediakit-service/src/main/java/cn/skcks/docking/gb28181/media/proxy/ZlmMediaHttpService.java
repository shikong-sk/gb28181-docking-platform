package cn.skcks.docking.gb28181.media.proxy;

import cn.skcks.docking.gb28181.common.json.JsonUtils;
import cn.skcks.docking.gb28181.media.dto.config.ServerConfig;
import cn.skcks.docking.gb28181.media.dto.response.ZlmResponse;
import com.fasterxml.jackson.core.type.TypeReference;
import lombok.Builder;
import org.springframework.http.ResponseEntity;

import java.util.List;
import java.util.Map;

@Builder
public class ZlmMediaHttpService {
    private String secret;
    private ZlmMediaHttpClient exchange;

    public ZlmResponse<List<String>> getApiList() {
        return exchange.getApiList(secret);
    }


    public ZlmResponse<List<ServerConfig>> getServerConfig() {
        return exchange.getServerConfig(secret);
    }


    public ResponseEntity<String> getServerConfigResponseEntity() {
        return exchange.getServerConfigResponseEntity(secret);
    }

    public String setServerConfig(ServerConfig config){
        Map<String,Object> map = JsonUtils.mapper.convertValue(config, new TypeReference<>() {});
        return exchange.setServerConfig(secret, map);
    }
}
