package cn.skcks.docking.gb28181.media.proxy;

import cn.skcks.docking.gb28181.media.dto.config.ServerConfig;
import cn.skcks.docking.gb28181.media.dto.media.GetMediaList;
import cn.skcks.docking.gb28181.media.dto.media.MediaResp;
import cn.skcks.docking.gb28181.media.dto.proxy.*;
import cn.skcks.docking.gb28181.media.dto.record.*;
import cn.skcks.docking.gb28181.media.dto.response.ZlmResponse;
import cn.skcks.docking.gb28181.media.dto.rtp.*;
import cn.skcks.docking.gb28181.media.dto.snap.Snap;
import cn.skcks.docking.gb28181.media.dto.version.VersionResp;
import cn.skcks.docking.gb28181.media.feign.IgnoreSSLFeignClientConfig;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;
import java.util.Map;

@FeignClient(name="zlmMediaServerProxy", url = "${media.url}", configuration = IgnoreSSLFeignClientConfig.class)
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

    @PostMapping("/index/api/addStreamProxy")
    ZlmResponse<AddStreamProxyResp> addStreamProxy(@RequestParam String secret, @RequestBody AddStreamProxy params);

    @GetMapping("/index/api/delStreamProxy")
    ZlmResponse<DelStreamProxyResp> delStreamProxy(@RequestParam String secret, @RequestParam String key);

    @PostMapping("/index/api/getMediaList")
    ZlmResponse<List<MediaResp>> getMediaList(@RequestParam String secret,@RequestBody GetMediaList params);

    @GetMapping("/index/api/getRtpInfo")
    GetRtpInfoResp getRtpInfo(@RequestParam String secret,@RequestParam("stream_id") String streamId);

    @PostMapping("/index/api/addFFmpegSource")
    ZlmResponse<AddFFmpegSourceResp> addFFmpegSource(@RequestParam String secret,@RequestBody AddFFmpegSource params);

    @GetMapping("/index/api/delFFmpegSource")
    ZlmResponse<DelFFmpegSourceResp> delFFmpegSource(@RequestParam String secret, @RequestParam String key);

    @PostMapping("/index/api/startRecord")
    StartRecordResp startRecord(@RequestParam String secret, @RequestBody StartRecord params);

    @PostMapping("/index/api/stopRecord")
    StopRecordResp stopRecord(@RequestParam String secret, @RequestBody StopRecord params);

    @PostMapping("/index/api/isRecording")
    IsRecordingResp isRecording(@RequestParam String secret, @RequestBody IsRecording params);

    @PostMapping("/index/api/getMp4RecordFile")
    GetMp4RecordFileResp getMp4RecordFile(@RequestParam String secret, @RequestBody GetMp4RecordFile params);

    @PostMapping("/index/api/deleteRecordDirectory")
    DeleteRecordDirectoryResp deleteRecordDirectory(@RequestParam String secret, @RequestBody DeleteRecordDirectory params);
}
