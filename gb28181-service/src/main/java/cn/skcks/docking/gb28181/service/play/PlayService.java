package cn.skcks.docking.gb28181.service.play;

import cn.skcks.docking.gb28181.common.json.JsonResponse;
import cn.skcks.docking.gb28181.common.json.JsonUtils;
import cn.skcks.docking.gb28181.common.redis.RedisUtil;
import cn.skcks.docking.gb28181.core.sip.dto.SipTransactionInfo;
import cn.skcks.docking.gb28181.core.sip.gb28181.cache.CacheUtil;
import cn.skcks.docking.gb28181.core.sip.gb28181.sdp.GB28181Description;
import cn.skcks.docking.gb28181.core.sip.gb28181.sdp.MediaSdpHelper;
import cn.skcks.docking.gb28181.core.sip.gb28181.sdp.StreamMode;
import cn.skcks.docking.gb28181.core.sip.message.processor.MessageProcessor;
import cn.skcks.docking.gb28181.core.sip.message.request.SipRequestBuilder;
import cn.skcks.docking.gb28181.core.sip.message.sender.SipMessageSender;
import cn.skcks.docking.gb28181.core.sip.message.subscribe.GenericSubscribe;
import cn.skcks.docking.gb28181.core.sip.message.subscribe.SipSubscribe;
import cn.skcks.docking.gb28181.core.sip.service.SipService;
import cn.skcks.docking.gb28181.core.sip.utils.SipUtil;
import cn.skcks.docking.gb28181.media.config.ZlmMediaConfig;
import cn.skcks.docking.gb28181.media.dto.rtp.CloseRtpServer;
import cn.skcks.docking.gb28181.media.dto.rtp.GetRtpInfoResp;
import cn.skcks.docking.gb28181.media.dto.rtp.OpenRtpServer;
import cn.skcks.docking.gb28181.media.dto.rtp.OpenRtpServerResp;
import cn.skcks.docking.gb28181.media.dto.status.ResponseStatus;
import cn.skcks.docking.gb28181.media.proxy.ZlmMediaService;
import cn.skcks.docking.gb28181.orm.mybatis.dynamic.model.DockingDevice;
import cn.skcks.docking.gb28181.service.docking.device.DockingDeviceService;
import cn.skcks.docking.gb28181.service.ssrc.SsrcService;
import gov.nist.javax.sip.message.SIPResponse;
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
import javax.sip.message.Response;
import java.text.MessageFormat;
import java.util.Date;
import java.util.concurrent.Flow;
import java.util.concurrent.TimeUnit;

@Slf4j
@Service
@RequiredArgsConstructor
public class PlayService {
    private final ZlmMediaConfig zlmMediaConfig;
    private final DockingDeviceService deviceService;
    private final ZlmMediaService zlmMediaService;
    private final SsrcService ssrcService;
    private final SipService sipService;
    private final SipMessageSender sender;
    private final SipSubscribe subscribe;

    private String videoUrl(String streamId){
        return StringUtils.joinWith("/", zlmMediaConfig.getUrl(),"rtp", streamId + ".live.flv");
    }

    private DeferredResult<JsonResponse<String>> makeResult(String deviceId, String channelId, long timeout, DockingDevice device) {
        DeferredResult<JsonResponse<String>> result = new DeferredResult<>(TimeUnit.SECONDS.toMillis(timeout));
        if (device == null) {
            log.info("未能找到 编码为 => {} 的设备", deviceId);
            result.setResult(JsonResponse.error(null, "未找到设备"));
            return result;
        }

        return result;
    }

    /**
     * 实时视频点播
     * @param deviceId 设备id
     * @param channelId 通道id
     */
    @SneakyThrows
    public DeferredResult<JsonResponse<String>> realTimePlay(String deviceId, String channelId, long timeout){
        DockingDevice device = deviceService.getDevice(deviceId);
        DeferredResult<JsonResponse<String>> result = makeResult(deviceId,channelId,timeout, device);
        if(result.hasResult()){
            return result;
        }

        String streamId = MediaSdpHelper.getStreamId(deviceId,channelId);
        String key = CacheUtil.getKey(MediaSdpHelper.Action.PLAY.getAction(), deviceId, channelId);
        if (RedisUtil.KeyOps.hasKey(key)) {
            result.setResult(JsonResponse.success(videoUrl(streamId)));
            return result;
        }

        GetRtpInfoResp rtpInfo = zlmMediaService.getRtpInfo(streamId);
        if(rtpInfo.getExist()){
            result.setResult(JsonResponse.error(MessageFormat.format("流 {0} 已存在", streamId)));
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
        String subscribeKey = GenericSubscribe.Helper.getKey(MessageProcessor.Method.INVITE, callId.getCallId());
        subscribe.getInviteSubscribe().addPublisher(subscribeKey);
        Flow.Subscriber<SIPResponse> subscriber = new Flow.Subscriber<>() {
            private Flow.Subscription subscription;
            @Override
            public void onSubscribe(Flow.Subscription subscription) {
                this.subscription = subscription;
                log.info("订阅 {} {}",MessageProcessor.Method.INVITE,subscribeKey);
                subscription.request(1);
            }

            @Override
            public void onNext(SIPResponse item) {
                int statusCode = item.getStatusCode();
                log.debug("{} 收到订阅消息 {}", subscribeKey, item);
                if(statusCode == Response.TRYING){
                    log.info("订阅 {} {} 尝试连接流媒体服务", MessageProcessor.Method.INVITE,subscribeKey);
                    subscription.request(1);
                } else if(statusCode>=Response.OK && statusCode < Response.MULTIPLE_CHOICES){
                    log.info("订阅 {} {} 流媒体服务连接成功, 开始推送视频流", MessageProcessor.Method.INVITE,subscribeKey);
                    RedisUtil.StringOps.set(key, JsonUtils.toCompressJson(new SipTransactionInfo(item)));
                    RedisUtil.StringOps.set(CacheUtil.getKey(key,"ssrc"), ssrc);
                    result.setResult(JsonResponse.success(videoUrl(streamId)));
                    onComplete();
                } else {
                    log.info("订阅 {} {} 连接流媒体服务时出现异常, 终止订阅", MessageProcessor.Method.INVITE,subscribeKey);
                    RedisUtil.KeyOps.delete(key);
                    RedisUtil.KeyOps.delete(CacheUtil.getKey(key,"ssrc"));
                    result.setResult(JsonResponse.error("连接流媒体服务失败"));
                    ssrcService.releaseSsrc(zlmMediaConfig.getId(), ssrc);
                    onComplete();
                }
            }

            @Override
            public void onError(Throwable throwable) {

            }

            @Override
            public void onComplete() {
                subscribe.getRecordInfoSubscribe().delPublisher(subscribeKey);
            }
        };
        subscribe.getInviteSubscribe().addSubscribe(subscribeKey, subscriber);
        sender.send(senderIp, request);
        result.onTimeout(()->{
            subscribe.getInviteSubscribe().delPublisher(subscribeKey);
            result.setResult(JsonResponse.error("点播超时"));
        });
        return result;
    }

    @SneakyThrows
    public JsonResponse<Void> realTimeStop(String deviceId, String channelId){
        DockingDevice device = deviceService.getDevice(deviceId);
        if (device == null) {
            log.info("未能找到 编码为 => {} 的设备", deviceId);
            return JsonResponse.error(null, "未找到设备");
        }

        String streamId = MediaSdpHelper.getStreamId(deviceId,channelId);
        String key = CacheUtil.getKey(MediaSdpHelper.Action.PLAY.getAction(), deviceId, channelId);
        String ssrcKey = CacheUtil.getKey(key,"ssrc");
        zlmMediaService.closeRtpServer(new CloseRtpServer(streamId));
        SipTransactionInfo transactionInfo = JsonUtils.parse(RedisUtil.StringOps.get(key), SipTransactionInfo.class);
        if(transactionInfo == null){
            return JsonResponse.error("未找到连接信息");
        }
        Request request = SipRequestBuilder.createByeRequest(device, channelId, transactionInfo);
        String senderIp = device.getLocalIp();
        sender.send(senderIp, request);

        String ssrc = RedisUtil.StringOps.get(ssrcKey);
        ssrcService.releaseSsrc(zlmMediaConfig.getId(),ssrc);
        RedisUtil.KeyOps.delete(ssrcKey);
        RedisUtil.KeyOps.delete(key);
        return JsonResponse.success(null);
    }

    @SneakyThrows
    public DeferredResult<JsonResponse<String>> recordPlay(String deviceId, String channelId, Date startTime, Date endTime, long timeout){
        DockingDevice device = deviceService.getDevice(deviceId);
        long start = startTime.toInstant().getEpochSecond();
        long end = endTime.toInstant().getEpochSecond();
        String streamId = MediaSdpHelper.getStreamId(deviceId,channelId,String.valueOf(start), String.valueOf(end));
        DeferredResult<JsonResponse<String>> result = makeResult(deviceId,channelId,timeout,device);
        if(result.hasResult()){
            return result;
        }

        String key = CacheUtil.getKey(MediaSdpHelper.Action.PLAY_BACK.getAction(), deviceId, channelId);
        if(RedisUtil.KeyOps.hasKey(key)){
            result.setResult(JsonResponse.success(videoUrl(streamId)));
            return result;
        }

        GetRtpInfoResp rtpInfo = zlmMediaService.getRtpInfo(streamId);
        if(rtpInfo.getExist()){
            result.setResult(JsonResponse.error(MessageFormat.format("流 {0} 已存在", streamId)));
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
        GB28181Description description = MediaSdpHelper.playback(deviceId, channelId, Connection.IP4, ip, port, ssrc, StreamMode.of(device.getStreamMode()),startTime,endTime);

        String transport = device.getTransport();
        String senderIp = device.getLocalIp();
        SipProvider provider = sipService.getProvider(transport, senderIp);
        CallIdHeader callId = provider.getNewCallId();

        Request request = SipRequestBuilder.createInviteRequest(device, channelId, description.toString(), SipUtil.generateViaTag(), SipUtil.generateFromTag(), null, ssrc, callId);
        String subscribeKey = GenericSubscribe.Helper.getKey(MessageProcessor.Method.INVITE, callId.getCallId());
        subscribe.getInviteSubscribe().addPublisher(subscribeKey);
        Flow.Subscriber<SIPResponse> subscriber = new Flow.Subscriber<>() {
            private Flow.Subscription subscription;
            @Override
            public void onSubscribe(Flow.Subscription subscription) {
                this.subscription = subscription;
                log.info("订阅 {} {}",MessageProcessor.Method.INVITE,subscribeKey);
                subscription.request(1);
            }

            @Override
            public void onNext(SIPResponse item) {
                int statusCode = item.getStatusCode();
                log.debug("{} 收到订阅消息 {}", subscribeKey, item);
                if(statusCode == Response.TRYING){
                    log.info("订阅 {} {} 尝试连接流媒体服务", MessageProcessor.Method.INVITE,subscribeKey);
                    subscription.request(1);
                } else if(statusCode>=Response.OK && statusCode < Response.MULTIPLE_CHOICES){
                    log.info("订阅 {} {} 流媒体服务连接成功, 开始推送视频流", MessageProcessor.Method.INVITE,subscribeKey);
                    RedisUtil.StringOps.set(key, JsonUtils.toCompressJson(new SipTransactionInfo(item)));
                    RedisUtil.StringOps.set(CacheUtil.getKey(key,"ssrc"), ssrc);
                    result.setResult(JsonResponse.success(videoUrl(streamId)));
                    onComplete();
                } else {
                    log.info("订阅 {} {} 连接流媒体服务时出现异常, 终止订阅", MessageProcessor.Method.INVITE,subscribeKey);
                    RedisUtil.KeyOps.delete(key);
                    RedisUtil.KeyOps.delete(CacheUtil.getKey(key,"ssrc"));
                    result.setResult(JsonResponse.error("连接流媒体服务失败"));
                    ssrcService.releaseSsrc(zlmMediaConfig.getId(), ssrc);
                    onComplete();
                }
            }

            @Override
            public void onError(Throwable throwable) {

            }

            @Override
            public void onComplete() {
                subscribe.getRecordInfoSubscribe().delPublisher(subscribeKey);
            }
        };
        subscribe.getInviteSubscribe().addSubscribe(subscribeKey, subscriber);
        sender.send(senderIp, request);
        result.onTimeout(()->{
            subscribe.getInviteSubscribe().delPublisher(subscribeKey);
            result.setResult(JsonResponse.error("点播超时"));
        });
        return result;
    }

    @SneakyThrows
    public JsonResponse<Void> recordStop(String deviceId, String channelId, Date startTime, Date endTime){
        DockingDevice device = deviceService.getDevice(deviceId);
        if (device == null) {
            log.info("未能找到 编码为 => {} 的设备", deviceId);
            return JsonResponse.error(null, "未找到设备");
        }

        long start = startTime.toInstant().getEpochSecond();
        long end = endTime.toInstant().getEpochSecond();
        String streamId = MediaSdpHelper.getStreamId(deviceId,channelId,String.valueOf(start), String.valueOf(end));
        String key = CacheUtil.getKey(MediaSdpHelper.Action.PLAY_BACK.getAction(), deviceId, channelId);
        String ssrcKey = CacheUtil.getKey(key,"ssrc");
        zlmMediaService.closeRtpServer(new CloseRtpServer(streamId));
        SipTransactionInfo transactionInfo = JsonUtils.parse(RedisUtil.StringOps.get(key), SipTransactionInfo.class);
        if(transactionInfo == null){
            return JsonResponse.error("未找到连接信息");
        }
        Request request = SipRequestBuilder.createByeRequest(device, channelId, transactionInfo);
        String senderIp = device.getLocalIp();
        sender.send(senderIp, request);

        String ssrc = RedisUtil.StringOps.get(ssrcKey);
        ssrcService.releaseSsrc(zlmMediaConfig.getId(),ssrc);
        RedisUtil.KeyOps.delete(ssrcKey);
        RedisUtil.KeyOps.delete(key);
        return JsonResponse.success(null);
    }

}
