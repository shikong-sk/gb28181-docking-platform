package cn.skcks.docking.gb28181.service.play;

import cn.skcks.docking.gb28181.common.json.JsonResponse;
import cn.skcks.docking.gb28181.common.redis.RedisUtil;
import cn.skcks.docking.gb28181.core.sip.gb28181.cache.CacheUtil;
import cn.skcks.docking.gb28181.media.config.ZlmMediaConfig;
import cn.skcks.docking.gb28181.media.dto.rtp.GetRtpInfoResp;
import cn.skcks.docking.gb28181.media.dto.rtp.OpenRtpServer;
import cn.skcks.docking.gb28181.media.dto.rtp.OpenRtpServerResp;
import cn.skcks.docking.gb28181.media.dto.status.ResponseStatus;
import cn.skcks.docking.gb28181.media.proxy.ZlmMediaService;
import cn.skcks.docking.gb28181.orm.mybatis.dynamic.model.DockingDevice;
import cn.skcks.docking.gb28181.service.docking.device.DockingDeviceService;
import cn.skcks.docking.gb28181.service.ssrc.SsrcService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.context.request.async.DeferredResult;

import javax.sip.ListeningPoint;
import java.text.MessageFormat;
import java.util.concurrent.TimeUnit;

@Slf4j
@Service
@RequiredArgsConstructor
public class PlayService {
    private static final String PREFIX = "RealTimePlay";
    private final ZlmMediaConfig mediaConfig;
    private final DockingDeviceService deviceService;
    private final ZlmMediaService zlmMediaService;
    private final SsrcService ssrcService;

    /**
     *
     * @param deviceId 设备id
     * @param channelId 通道id
     */
    public DeferredResult<JsonResponse<String>> realTimePlay(String deviceId, String channelId, long timeout){
        DeferredResult<JsonResponse<String>> result = new DeferredResult<>(TimeUnit.SECONDS.toMillis(timeout));
        DockingDevice device = deviceService.getDevice(deviceId);
        if (device == null) {
            log.info("未能找到 编码为 => {} 的设备", deviceId);
            result.setResult(JsonResponse.error(null, "未找到设备"));
            return result;
        }

        String streamId = CacheUtil.getKey(deviceId,channelId);
        String key = CacheUtil.getKey(PREFIX, streamId);
        if(RedisUtil.KeyOps.hasKey(key)){
            String url = RedisUtil.StringOps.get(key);
            result.setResult(JsonResponse.success(url));
            return result;
        }

        GetRtpInfoResp rtpInfo = zlmMediaService.getRtpInfo(streamId);
        if(rtpInfo.getExist()){
            result.setResult(JsonResponse.error(MessageFormat.format("实时流 {0} 已存在", streamId)));
            return result;
        }

        int streamMode = device.getStreamMode() == null || device.getStreamMode().equalsIgnoreCase(ListeningPoint.UDP) ? 0 : 1;
        OpenRtpServer openRtpServer = new OpenRtpServer();
        openRtpServer.setPort(0);
        openRtpServer.setStreamId(streamId);
        openRtpServer.setTcpMode(streamMode);
        OpenRtpServerResp openRtpServerResp = zlmMediaService.openRtpServer(openRtpServer);
        if(!openRtpServerResp.getCode().equals(ResponseStatus.Success)){
            result.setResult(JsonResponse.error(openRtpServerResp.getCode().getMsg()));
            return result;
        }

        String ip = mediaConfig.getIp();
        StringBuilder sb = new StringBuilder();
        sb.append("v=0\r\n");
        sb.append("o=").append(channelId).append(" 0 0 IN IP4 ").append(ip).append("\r\n");

        return result;
//        zlmMediaService.getRtpInfo();
//        GetMediaList getMediaList = new GetMediaList();
//        getMediaList.set
//        zlmMediaService.getMediaList()
    }
}
