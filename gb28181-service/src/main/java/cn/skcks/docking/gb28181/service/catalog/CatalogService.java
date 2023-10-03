package cn.skcks.docking.gb28181.service.catalog;

import cn.skcks.docking.gb28181.config.sip.SipConfig;
import cn.skcks.docking.gb28181.constant.CmdType;
import cn.skcks.docking.gb28181.core.sip.message.request.SipRequestBuilder;
import cn.skcks.docking.gb28181.core.sip.message.subscribe.GenericSubscribe;
import cn.skcks.docking.gb28181.core.sip.message.subscribe.SipSubscribe;
import cn.skcks.docking.gb28181.core.sip.service.SipService;
import cn.skcks.docking.gb28181.orm.mybatis.dynamic.model.DockingDevice;
import cn.skcks.docking.gb28181.orm.mybatis.dynamic.model.DockingDeviceChannel;
import cn.skcks.docking.gb28181.service.device.DeviceChannelService;
import cn.skcks.docking.gb28181.service.docking.device.cache.DockingDeviceCacheService;
import cn.skcks.docking.gb28181.sip.manscdp.catalog.query.CatalogQueryDTO;
import cn.skcks.docking.gb28181.sip.manscdp.catalog.response.CatalogItemDTO;
import cn.skcks.docking.gb28181.sip.manscdp.catalog.response.CatalogResponseDTO;
import cn.skcks.docking.gb28181.sip.method.message.request.MessageRequestBuilder;
import cn.skcks.docking.gb28181.sip.utils.MANSCDPUtils;
import cn.skcks.docking.gb28181.sip.utils.SipUtil;
import gov.nist.javax.sip.message.SIPRequest;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.sip.SipProvider;
import javax.sip.message.Request;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Flow;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicLong;

@Slf4j
@Service
@RequiredArgsConstructor
public class CatalogService {
    private final SipService sipService;
    private final DockingDeviceCacheService deviceCacheService;
    private final SipConfig sipConfig;
    private final SipSubscribe subscribe;
    private final DeviceChannelService deviceChannelService;

    @SneakyThrows
    public CompletableFuture<List<CatalogItemDTO>> catalog(String gbDeviceId){
        CompletableFuture<List<CatalogItemDTO>> result = new CompletableFuture<>();
        result.completeOnTimeout(Collections.emptyList(), 60, TimeUnit.SECONDS);
        DockingDevice device = deviceCacheService.getDevice(gbDeviceId);
        SipProvider provider = sipService.getProvider(device.getTransport(), device.getLocalIp());
        MessageRequestBuilder requestBuilder = MessageRequestBuilder.builder()
                .localIp(device.getLocalIp())
                .localId(sipConfig.getId())
                .localPort(sipConfig.getPort())
                .transport(device.getTransport())
                .targetId(device.getDeviceId())
                .targetIp(device.getIp())
                .targetPort(device.getPort())
                .build();
        String callId = provider.getNewCallId().getCallId();
        long cSeq = SipRequestBuilder.getCSeq();
        String sn = SipUtil.generateSn();
        CatalogQueryDTO catalogQueryDTO = CatalogQueryDTO.builder()
                .deviceId(gbDeviceId)
                .sn(sn)
                .build();
        Request request = requestBuilder.createMessageRequest(callId, cSeq, MANSCDPUtils.toByteXml(catalogQueryDTO, device.getCharset()));
        String key = GenericSubscribe.Helper.getKey(CmdType.CATALOG, gbDeviceId, sn);
        subscribe.getSipRequestSubscribe().addPublisher(key, 60, TimeUnit.SECONDS);
        subscribe.getSipRequestSubscribe().addSubscribe(key, new Flow.Subscriber<>() {
            private Flow.Subscription subscription;
            private final AtomicLong num = new AtomicLong(0);
            private long sumNum = 0;

            private final List<CatalogItemDTO> data = new ArrayList<>();

            @Override
            public void onSubscribe(Flow.Subscription subscription) {
                this.subscription = subscription;
                subscription.request(1);
            }

            @Override
            public void onNext(SIPRequest item) {
                CatalogResponseDTO catalogResponseDTO = MANSCDPUtils.parse(item.getRawContent(), CatalogResponseDTO.class);
                sumNum = Math.max(sumNum,catalogResponseDTO.getSumNum());
                long curNum = num.addAndGet(catalogResponseDTO.getDeviceList().getNum());
                log.debug("当前获取数量: {}/{}", curNum, sumNum);
                data.addAll(catalogResponseDTO.getDeviceList().getDeviceList());
                if(curNum >= sumNum){
                    log.info("获取完成 {}", key);
                    subscribe.getSipRequestSubscribe().complete(key);
                } else {
                    subscription.request(1);
                }
            }

            @Override
            public void onError(Throwable throwable) {
                throwable.printStackTrace();
                onComplete();
            }

            @Override
            public void onComplete() {
                log.info("{} 返回结果 {}", key, result.complete(data));

                data.stream().map(item->{
                    DockingDeviceChannel model = new DockingDeviceChannel();
                    model.setGbDeviceId(device.getDeviceId());
                    model.setGbDeviceChannelId(item.getDeviceId());
                    model.setName(item.getName());
                    model.setAddress(item.getAddress());
                    return model;
                }).forEach(deviceChannelService::add);

                subscribe.getSipRequestSubscribe().delPublisher(key);
            }
        });
        provider.sendRequest(request);
        return result;
    }
}


