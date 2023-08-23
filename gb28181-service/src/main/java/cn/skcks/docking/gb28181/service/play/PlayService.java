package cn.skcks.docking.gb28181.service.play;

import cn.skcks.docking.gb28181.common.json.JsonResponse;
import cn.skcks.docking.gb28181.common.redis.RedisUtil;
import cn.skcks.docking.gb28181.core.sip.gb28181.cache.CacheUtil;
import cn.skcks.docking.gb28181.core.sip.gb28181.sdp.GB28181Description;
import cn.skcks.docking.gb28181.core.sip.gb28181.sdp.MediaSdpHelper;
import cn.skcks.docking.gb28181.core.sip.gb28181.sdp.StreamMode;
import cn.skcks.docking.gb28181.core.sip.message.request.SipRequestBuilder;
import cn.skcks.docking.gb28181.core.sip.message.sender.SipMessageSender;
import cn.skcks.docking.gb28181.core.sip.service.SipService;
import cn.skcks.docking.gb28181.core.sip.utils.SipUtil;
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
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.web.context.request.async.DeferredResult;

import javax.sdp.Connection;
import javax.sip.ListeningPoint;
import javax.sip.SipProvider;
import javax.sip.header.CallIdHeader;
import javax.sip.message.Request;
import java.text.MessageFormat;
import java.util.concurrent.TimeUnit;

@Slf4j
@Service
@RequiredArgsConstructor
public class PlayService {
    private static final String PREFIX = "RealTimePlay";
    private final ZlmMediaConfig zlmMediaConfig;
    private final DockingDeviceService deviceService;
    private final ZlmMediaService zlmMediaService;
    private final SsrcService ssrcService;
    private final SipService sipService;
    private final SipMessageSender sender;

    /**
     *
     * @param deviceId 设备id
     * @param channelId 通道id
     */
    @SneakyThrows
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
        log.info("openRtpServerResp => {}", openRtpServerResp);
        if(!openRtpServerResp.getCode().equals(ResponseStatus.Success)){
            result.setResult(JsonResponse.error(openRtpServerResp.getCode().getMsg()));
            return result;
        }

        String ip = zlmMediaConfig.getIp();
        int port = openRtpServerResp.getPort();
        String ssrc = ssrcService.getPlaySsrc();
        GB28181Description description = MediaSdpHelper.play(deviceId, channelId, Connection.IP4, ip, port, ssrc, StreamMode.of(device.getStreamMode()));

        String transport = device.getTransport();
        String senderIp = device.getLocalIp();
        SipProvider provider = sipService.getProvider(transport, senderIp);
        CallIdHeader callId = provider.getNewCallId();
        Request request = SipRequestBuilder.createInviteRequest(device, channelId, description.toString(), SipUtil.generateViaTag(), SipUtil.generateFromTag(), null, ssrc, callId);
        sender.send(senderIp, request);

        result.setResult(JsonResponse.success(StringUtils.joinWith("/", zlmMediaConfig.getUrl(),"rtp", streamId + ".live.flv")));
        return result;
//        zlmMediaService.getRtpInfo();
//        GetMediaList getMediaList = new GetMediaList();
//        getMediaList.set
//        zlmMediaService.getMediaList()
    }
}
