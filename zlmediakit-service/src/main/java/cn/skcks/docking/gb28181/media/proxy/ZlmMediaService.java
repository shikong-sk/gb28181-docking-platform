package cn.skcks.docking.gb28181.media.proxy;

import cn.skcks.docking.gb28181.media.dto.config.ServerConfig;
import cn.skcks.docking.gb28181.media.dto.proxy.*;
import cn.skcks.docking.gb28181.media.dto.response.ZlmResponse;
import cn.skcks.docking.gb28181.media.dto.rtp.*;
import cn.skcks.docking.gb28181.media.dto.snap.Snap;
import cn.skcks.docking.gb28181.media.dto.version.VersionResp;
import lombok.Builder;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Builder
@SuppressWarnings("unused")
public class ZlmMediaService {
    private String secret;
    private ZlmMediaHttpClient exchange;

    /**
     * 功能：获取API列表
     */
    public ZlmResponse<List<String>> getApiList() {
        return exchange.getApiList(secret);
    }

    /**
     * 功能：获取服务器配置
     */
    public ZlmResponse<List<ServerConfig>> getServerConfig() {
        return exchange.getServerConfig(secret);
    }


    public ResponseEntity<String> getServerConfigResponseEntity() {
        return exchange.getServerConfigResponseEntity(secret);
    }

    /**
     * 功能：设置服务器配置
     */
    public String setServerConfig(ServerConfig config){
        return exchange.setServerConfig(secret, config);
    }

    /**
     * 功能：创建GB28181 RTP接收端口，如果该端口接收数据超时，则会自动被回收(不用调用closeRtpServer接口)
     */
    public OpenRtpServerResp openRtpServer(OpenRtpServer params){
        return exchange.openRtpServer(secret, params);
    }

    /**
     * 关闭GB28181 RTP接收端口
     */
    public CloseRtpServerResp closeRtpServer(CloseRtpServer params){
        return exchange.closeRtpServer(secret, params);
    }

    /**
     * 获取openRtpServer接口创建的所有RTP服务器
     */
    public ZlmResponse<List<RtpServer>> listRtpServer(){
        return exchange.listRtpServer(secret);
    }

    /**
     * 功能：获取版本信息，如分支，commit id, 编译时间
     */
    public ZlmResponse<VersionResp> version(){
        return exchange.version(secret);
    }

    /**
     * 添加rtsp/rtmp主动推流(把本服务器的直播流推送到其他服务器去)
     */
    public ZlmResponse<AddStreamPusherProxyResp> addStreamPusherProxy(AddStreamPusherProxy params){
        return exchange.addStreamPusherProxy(secret, params);
    }

    /**
     *
     * 功能：关闭推流
     * <p>(可以使用close_streams接口关闭源直播流也可以停止推流)</p>
     * @param key addStreamPusherProxy 接口返回的key
     */
    public ZlmResponse<DelStreamPusherProxyResp> delStreamPusherProxy(@RequestParam String key) {
        return exchange.delStreamPusherProxy(secret, key);
    }


    /**
     * <p>功能：作为GB28181客户端，启动ps-rtp推流，支持rtp/udp方式；该接口支持rtsp/rtmp等协议转ps-rtp推流。</p>
     * 第一次推流失败会直接返回错误，成功一次后，后续失败也将无限重试。
     */
    public StartSendRtpResp startSendRtp(StartSendRtp params){
        return exchange.startSendRtp(secret, params);
    }


    /**
     * <p>功能：作为GB28181 Passive TCP服务器；该接口支持rtsp/rtmp等协议转ps-rtp被动推流。</p>
     * <p>调用该接口，zlm会启动tcp服务器等待连接请求，连接建立后，zlm会关闭tcp服务器，然后源源不断的往客户端推流。</p>
     * <p>第一次推流失败会直接返回错误，成功一次后，后续失败也将无限重试(不停地建立tcp监听，超时后再关闭)。</p>
     */
    public StartSendRtpResp startSendRtpPassive(StartSendRtpPassive params){
        return exchange.startSendRtpPassive(secret, params);
    }


    /**
     * 功能：停止GB28181 ps-rtp推流
     */
    public ZlmResponse<Void> stopSendRtp(StopSendRtp params){
        return exchange.stopSendRtp(secret, params);
    }

    /**
     * 功能：获取截图或生成实时截图并返回
     */
    public ResponseEntity<byte[]> getSnap(Snap params){
        return exchange.getSnap(secret, params);
    }

    /**
     * 功能：重启服务器,只有Daemon方式才能重启，否则是直接关闭！
     */
    public ZlmResponse<Void> restartServer(){
        return exchange.restartServer(secret);
    }

    /**
     * 功能：动态添加rtsp/rtmp/hls/http-ts/http-flv拉流代理(只支持H264/H265/aac/G711/opus负载)
     */
    public ZlmResponse<AddStreamProxyResp> addStreamProxy(AddStreamProxy params){
        return exchange.addStreamProxy(secret, params);
    }

    /**
     * 功能：关闭拉流代理
     */
    public ZlmResponse<DelStreamProxyResp> delStreamProxy(String key){
        return exchange.delStreamProxy(secret, key);
    }
}

