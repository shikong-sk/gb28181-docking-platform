package cn.skcks.docking.gb28181.service.catalog;

import cn.skcks.docking.gb28181.core.sip.message.subscribe.SipSubscribe;
import cn.skcks.docking.gb28181.orm.mybatis.dynamic.model.DockingDeviceChannel;
import cn.skcks.docking.gb28181.sip.manscdp.catalog.response.CatalogItemDTO;
import cn.skcks.docking.gb28181.sip.manscdp.catalog.response.CatalogResponseDTO;
import cn.skcks.docking.gb28181.sip.utils.MANSCDPUtils;
import gov.nist.javax.sip.message.SIPRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Flow;
import java.util.concurrent.atomic.AtomicLong;
import java.util.function.Consumer;

@Slf4j
@RequiredArgsConstructor
public class CatalogSubscriber implements Flow.Subscriber<SIPRequest>{
    private final SipSubscribe subscribe;
    private final String key;
    private final CompletableFuture<List<CatalogItemDTO>> result;
    private final String deviceId;
    private final Consumer<? super DockingDeviceChannel> addDeviceChannelFunc;

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
            model.setGbDeviceId(deviceId);
            model.setGbDeviceChannelId(item.getDeviceId());
            model.setName(item.getName());
            model.setAddress(item.getAddress());
            return model;
        }).forEach(addDeviceChannelFunc);

        subscribe.getSipRequestSubscribe().delPublisher(key);
    }
}
