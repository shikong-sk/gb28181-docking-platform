package cn.skcks.docking.gb28181.media.proxy;

import cn.skcks.docking.gb28181.media.dto.config.ServerConfig;
import cn.skcks.docking.gb28181.media.dto.proxy.AddStreamPusherProxy;
import cn.skcks.docking.gb28181.media.dto.proxy.AddStreamPusherProxyResp;
import cn.skcks.docking.gb28181.media.dto.proxy.DelStreamPusherProxyResp;
import cn.skcks.docking.gb28181.media.dto.response.ZlmResponse;
import cn.skcks.docking.gb28181.media.dto.rtp.CloseRtpServer;
import cn.skcks.docking.gb28181.media.dto.rtp.CloseRtpServerResp;
import cn.skcks.docking.gb28181.media.dto.rtp.OpenRtpServer;
import cn.skcks.docking.gb28181.media.dto.rtp.OpenRtpServerResp;
import cn.skcks.docking.gb28181.media.dto.version.VersionResp;
import lombok.Builder;
import org.springframework.http.ResponseEntity;
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
}

