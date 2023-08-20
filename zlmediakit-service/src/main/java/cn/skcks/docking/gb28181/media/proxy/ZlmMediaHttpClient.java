package cn.skcks.docking.gb28181.media.proxy;

import cn.skcks.docking.gb28181.media.dto.config.ServerConfig;
import cn.skcks.docking.gb28181.media.dto.proxy.AddStreamPusherProxy;
import cn.skcks.docking.gb28181.media.dto.proxy.AddStreamPusherProxyResp;
import cn.skcks.docking.gb28181.media.dto.proxy.DelStreamPusherProxyResp;
import cn.skcks.docking.gb28181.media.dto.response.ZlmResponse;
import cn.skcks.docking.gb28181.media.dto.rtp.*;
import cn.skcks.docking.gb28181.media.dto.snap.Snap;
import cn.skcks.docking.gb28181.media.dto.version.VersionResp;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.service.annotation.HttpExchange;

import java.util.List;

@FeignClient(name="zlmMediaServerProxy",url = "${media.url}")
@HttpExchange
public interface ZlmMediaHttpClient {
    @GetMapping("/index/api/getApiList")
    ZlmResponse<List<String>> getApiList(@RequestParam String secret);

    @GetMapping("/index/api/getServerConfig")
    ZlmResponse<List<ServerConfig>> getServerConfig(@RequestParam String secret);

    @GetMapping("/index/api/getServerConfig")
    ResponseEntity<String> getServerConfigResponseEntity(@RequestParam String secret);

    @PostMapping("/index/api/setServerConfig")
    String setServerConfig(@RequestParam String secret, @RequestBody ServerConfig config);

    @PostMapping("/index/api/openRtpServer")
    OpenRtpServerResp openRtpServer(@RequestParam String secret, @RequestBody OpenRtpServer params);

    @PostMapping("/index/api/closeRtpServer")
    CloseRtpServerResp closeRtpServer(@RequestParam String secret, @RequestBody CloseRtpServer params);

    @GetMapping("/index/api/listRtpServer")
    ZlmResponse<List<RtpServer>> listRtpServer(@RequestParam String secret);

    @GetMapping("/index/api/version")
    ZlmResponse<VersionResp> version(@RequestParam String secret);

    @PostMapping("/index/api/addStreamPusherProxy")
    ZlmResponse<AddStreamPusherProxyResp> addStreamPusherProxy(@RequestParam String secret, @RequestBody AddStreamPusherProxy params);

    @PostMapping("/index/api/delStreamPusherProxy")
    ZlmResponse<DelStreamPusherProxyResp> delStreamPusherProxy(@RequestParam String secret, @RequestParam String key);

    @PostMapping("/index/api/startSendRtp")
    StartSendRtpResp startSendRtp(@RequestParam String secret, @RequestBody StartSendRtp params);

    @PostMapping("/index/api/startSendRtpPassive")
    StartSendRtpResp startSendRtpPassive(@RequestParam String secret, @RequestBody StartSendRtpPassive params);

    @PostMapping("/index/api/stopSendRtp")
    ZlmResponse<Void> stopSendRtp(@RequestParam String secret, @RequestBody StopSendRtp params);

    @PostMapping("/index/api/getSnap")
    ResponseEntity<byte[]> getSnap(@RequestParam String secret, @RequestBody Snap params);

    @GetMapping("/index/api/restartServer")
    ZlmResponse<Void> restartServer(@RequestParam String secret);
}