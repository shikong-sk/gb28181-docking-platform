package cn.skcks.docking.gb28181.media.proxy;

import cn.skcks.docking.gb28181.common.json.JsonUtils;
import cn.skcks.docking.gb28181.media.dto.config.ServerConfig;
import cn.skcks.docking.gb28181.media.dto.response.ZlmResponse;
import cn.skcks.docking.gb28181.media.dto.rtp.CloseRtpServer;
import cn.skcks.docking.gb28181.media.dto.rtp.CloseRtpServerResp;
import cn.skcks.docking.gb28181.media.dto.rtp.OpenRtpServer;
import cn.skcks.docking.gb28181.media.dto.rtp.OpenRtpServerResp;
import com.fasterxml.jackson.core.type.TypeReference;
import lombok.Builder;
import org.springframework.http.ResponseEntity;

import java.util.List;
import java.util.Map;

@Builder
public class ZlmMediaService {
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
        return exchange.setServerConfig(secret, config);
    }

    public OpenRtpServerResp openRtpServer(OpenRtpServer params){
        return exchange.openRtpServer(secret, params);
    }

    public CloseRtpServerResp closeRtpServer(CloseRtpServer params){
        return exchange.closeRtpServer(secret, params);
    }
}
