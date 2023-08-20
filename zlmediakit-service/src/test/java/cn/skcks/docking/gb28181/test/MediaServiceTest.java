package cn.skcks.docking.gb28181.test;

import cn.hutool.core.codec.Base64;
import cn.hutool.core.date.LocalDateTimeUtil;
import cn.hutool.core.net.url.UrlBuilder;
import cn.hutool.core.net.url.UrlPath;
import cn.skcks.docking.gb28181.common.json.JsonResponse;
import cn.skcks.docking.gb28181.common.json.JsonUtils;
import cn.skcks.docking.gb28181.media.config.ZlmMediaConfig;
import cn.skcks.docking.gb28181.media.dto.config.FFMpegConfig;
import cn.skcks.docking.gb28181.media.dto.config.HookConfig;
import cn.skcks.docking.gb28181.media.dto.config.ServerConfig;
import cn.skcks.docking.gb28181.media.dto.proxy.*;
import cn.skcks.docking.gb28181.media.dto.response.ZlmResponse;
import cn.skcks.docking.gb28181.media.dto.response.ZlmResponseConvertor;
import cn.skcks.docking.gb28181.media.dto.rtp.CloseRtpServer;
import cn.skcks.docking.gb28181.media.dto.rtp.OpenRtpServer;
import cn.skcks.docking.gb28181.media.dto.rtp.RtpServer;
import cn.skcks.docking.gb28181.media.dto.rtp.StartSendRtp;
import cn.skcks.docking.gb28181.media.dto.snap.Snap;
import cn.skcks.docking.gb28181.media.dto.version.VersionResp;
import cn.skcks.docking.gb28181.media.proxy.ZlmMediaService;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.net.URL;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.*;

@Slf4j
@SpringBootTest
@EnableAutoConfiguration(exclude={DataSourceAutoConfiguration.class})
@ExtendWith(SpringExtension.class)
public class MediaServiceTest {
    @Autowired
    private ZlmMediaService zlmMediaService;
    @Autowired
    private ZlmMediaConfig config;

    @Test
    void test(){
        long unix = 1692346604L;
        Instant instant = Instant.ofEpochSecond(unix);

        log.info("{}", LocalDateTime.ofEpochSecond(unix, 0, TimeZone.getTimeZone("UTC").toZoneId().getRules().getOffset(instant)));
        log.info("{}", LocalDateTimeUtil.of(instant, TimeZone.getTimeZone("UTC")));
        log.info("{}", LocalDateTime.ofEpochSecond(unix, 0, ZoneOffset.ofHours(0)));

        log.info("{}", LocalDateTime.ofEpochSecond(unix, 0, TimeZone.getTimeZone("Asia/Shanghai").toZoneId().getRules().getOffset(instant)));
        log.info("{}", LocalDateTimeUtil.of(instant, TimeZone.getTimeZone("GMT+8")));
        log.info("{}", LocalDateTime.ofEpochSecond(unix, 0, ZoneOffset.ofHours(8)));
    }

    @Test
    void context(){
        ResponseEntity<String> entity = zlmMediaService.getServerConfigResponseEntity();
        log.info("{}", entity.getBody());

        ZlmResponse<List<ServerConfig>> test = zlmMediaService.getServerConfig();
        JsonResponse<List<ServerConfig>> jsonResponse = test.getJsonResponse();
        log.info("{}", jsonResponse);

        ZlmResponse<List<ServerConfig>> zlmResponse = ZlmResponseConvertor.INSTANCE.toZlmResponse(jsonResponse);
        log.info("{}", zlmResponse);
        log.info("{}", zlmResponse.getCode().getMsg());
    }

    @Test
    @SneakyThrows
    void testApi(){
        log.info("{}", zlmMediaService.getApiList());
        int port = 60000;
        String streamId = "testStream";
        OpenRtpServer openRtpServer = new OpenRtpServer(port,0,streamId);
        log.info("{}", zlmMediaService.openRtpServer(openRtpServer));

        ZlmResponse<List<RtpServer>> listRtpServer = zlmMediaService.listRtpServer();
        log.info("{}", listRtpServer.getData());

        CloseRtpServer closeRtpServer = new CloseRtpServer(streamId);
        log.info("{}", zlmMediaService.closeRtpServer(closeRtpServer));
    }

    @Test
    void version(){
        ZlmResponse<VersionResp> versionResp = zlmMediaService.version();
        log.info("{}", versionResp);
        Date date = versionResp.getData().getBuildTime();
        log.info("{}", date);
        log.info("{}", LocalDateTimeUtil.of(date.toInstant(), TimeZone.getTimeZone("GMT+8")));
        log.info("{}", LocalDateTimeUtil.of(date.toInstant(), TimeZone.getTimeZone("UTC")));
    }

    @Test
    void configTest(){
        ZlmResponse<List<ServerConfig>> resp = zlmMediaService.getServerConfig();
        log.info("{}", resp);
        ServerConfig config = resp.getData().get(0);
        config.getApi().setApiDebug(0);
        log.info("{}", zlmMediaService.setServerConfig(config));

        resp = zlmMediaService.getServerConfig();
        log.info("{}", resp);
        config.getApi().setApiDebug(1);
        log.info("{}", zlmMediaService.setServerConfig(config));

        resp = zlmMediaService.getServerConfig();
        log.info("{}", resp);

        log.info("{}", zlmMediaService.setServerConfig(config));
    }

    @Test
    void emptyHookConfigTest(){
        ZlmResponse<List<ServerConfig>> resp = zlmMediaService.getServerConfig();
        log.info("{}", resp);
        ServerConfig config = resp.getData().get(0);
        config.setHook(new HookConfig());
        log.info("{}", zlmMediaService.setServerConfig(config));
        log.info("{}", zlmMediaService.getServerConfig());
    }

    /**
     * <p>先清空 hook 后</p>
     * <p>再使用 ffmpeg 或其他推流工具</p>
     * <p>推流到 zlm 服务的 /live/test 再执行此测试</p>
     */
    @Test
    void streamPusherProxyTest(){
        AddStreamPusherProxy addStreamPusherProxy = new AddStreamPusherProxy();
        addStreamPusherProxy.setSchema("rtmp");
        addStreamPusherProxy.setApp("live");
        addStreamPusherProxy.setStream("test");
        addStreamPusherProxy.setDstUrl("rtmp://127.0.0.1/live/test2");

        ZlmResponse<AddStreamPusherProxyResp> addStreamPusherProxyRespZlmResponse = zlmMediaService.addStreamPusherProxy(addStreamPusherProxy);
        log.info("{}", addStreamPusherProxyRespZlmResponse);
        AddStreamPusherProxyResp data = addStreamPusherProxyRespZlmResponse.getData();
        String key = Optional.ofNullable(data).orElse(new AddStreamPusherProxyResp()).getKey();
        ZlmResponse<DelStreamPusherProxyResp> delStreamPusherProxyRespZlmResponse = zlmMediaService.delStreamPusherProxy(key);
        log.info("{}",delStreamPusherProxyRespZlmResponse);
    }

    /**
     * <p>使用 ffmpeg 或其他推流工具</p>
     * <p>推流到 zlm 服务的 /live/test 再执行此测试</p>
     */
    @Test
    @SneakyThrows
    void snapTest(){
        Snap snap = new Snap();
        // String url = UrlBuilder.of(config.getUrl())
        //         .setScheme("rtmp")
        //         .setPath(UrlPath.of("/live/test", null)).build();
        // log.info("url => {}", url);
        snap.setUrl("rtmp://127.0.0.1:1935/live/test");
        snap.setTimeoutSec(180);
        snap.setExpireSec(5);
        ResponseEntity<byte[]> response = zlmMediaService.getSnap(snap);
        log.info("{}", Objects.requireNonNull(response.getBody()).length);
        log.info("base64 snap => \n{}",Base64.encode(response.getBody()));
    }

    @Test
    void restartServer(){
        ZlmResponse<Void> response = zlmMediaService.restartServer();
        log.info("{}", response);
    }

    @Test
    void simpleTest(){
        StartSendRtp startSendRtp = new StartSendRtp();
        startSendRtp.setUdp(true);
        log.info("{}",JsonUtils.toJson(startSendRtp));
    }

    @Test
    void streamProxyTest(){
        AddStreamProxy addStreamProxy = AddStreamProxy.builder()
                .app("proxy")
                .stream("test")
                .url("rtmp://127.0.0.1:1935/live/test")
                .build();

        ZlmResponse<AddStreamProxyResp> addedStreamProxy = zlmMediaService.addStreamProxy(addStreamProxy);
        log.info("{}", addedStreamProxy);
        String key = Optional.ofNullable(addedStreamProxy.getData()).orElse(new AddStreamProxyResp()).getKey();
        log.info("{}", key);

        ZlmResponse<DelStreamProxyResp> delStreamProxyRespZlmResponse = zlmMediaService.delStreamProxy(key);
        log.info("{}", delStreamProxyRespZlmResponse);
    }
}
