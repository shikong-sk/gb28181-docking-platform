package cn.skcks.docking.gb28181.service.record;

import cn.hutool.core.date.DateUtil;
import cn.skcks.docking.gb28181.common.json.JsonResponse;
import cn.skcks.docking.gb28181.common.xml.XmlUtils;
import cn.skcks.docking.gb28181.core.sip.gb28181.cache.CacheUtil;
import cn.skcks.docking.gb28181.core.sip.gb28181.constant.CmdType;
import cn.skcks.docking.gb28181.core.sip.message.processor.message.types.recordinfo.query.dto.RecordInfoRequestDTO;
import cn.skcks.docking.gb28181.core.sip.message.processor.message.types.recordinfo.reponse.dto.RecordInfoItemDTO;
import cn.skcks.docking.gb28181.core.sip.message.processor.message.types.recordinfo.reponse.dto.RecordInfoResponseDTO;
import cn.skcks.docking.gb28181.core.sip.message.request.SipRequestBuilder;
import cn.skcks.docking.gb28181.core.sip.message.sender.SipMessageSender;
import cn.skcks.docking.gb28181.core.sip.message.subscribe.SipSubscribe;
import cn.skcks.docking.gb28181.core.sip.service.SipService;
import cn.skcks.docking.gb28181.core.sip.utils.SipUtil;
import cn.skcks.docking.gb28181.orm.mybatis.dynamic.model.DockingDevice;
import cn.skcks.docking.gb28181.service.docking.device.DockingDeviceService;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.context.request.async.DeferredResult;

import javax.sip.SipProvider;
import javax.sip.header.CallIdHeader;
import javax.sip.message.Request;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Flow;
import java.util.concurrent.atomic.AtomicLong;

@Slf4j
@Service
@RequiredArgsConstructor
public class RecordService {
    private final DockingDeviceService deviceService;
    private final SipService sipService;
    private final SipMessageSender sender;
    private final SipSubscribe subscribe;

    @SneakyThrows
    public DeferredResult<JsonResponse<List<RecordInfoItemDTO>>> requestRecordInfo(String deviceId) {
        DeferredResult<JsonResponse<List<RecordInfoItemDTO>>> result = new DeferredResult<>(30 * 1000L);

        DockingDevice device = deviceService.getDevice(deviceId);
        if (device == null) {
            log.info("未能找到 编码为 => {} 的设备", deviceId);
            result.setResult(JsonResponse.error(null, "未找到设备"));
            return result;
        }

        String transport = device.getTransport();
        String senderIp = device.getLocalIp();
        SipProvider provider = sipService.getProvider(transport, senderIp);
        CallIdHeader callId = provider.getNewCallId();
        String sn = String.valueOf((int) (Math.random() * 9 + 1) * 100000);
        RecordInfoRequestDTO dto = RecordInfoRequestDTO.builder()
                .deviceId(deviceId)
                .startTime(DateUtil.beginOfDay(DateUtil.date()))
                .endTime(DateUtil.endOfDay(DateUtil.date()))
                .sn(sn)
                .build();
        Request request = SipRequestBuilder.createMessageRequest(device,
                XmlUtils.toXml(dto),
                SipUtil.generateViaTag(),
                SipUtil.generateFromTag(),
                null,
                callId);

        String key = CacheUtil.getKey(CmdType.RECORD_INFO, deviceId, sn);
        subscribe.getRecordInfoSubscribe().addPublisher(key);
        sender.send(senderIp, request);
        List<RecordInfoItemDTO> list = new ArrayList<>();
        AtomicLong sum = new AtomicLong(0);
        AtomicLong getNum = new AtomicLong(0);
        subscribe.getRecordInfoSubscribe().addSubscribe(key, new Flow.Subscriber<>() {
            Flow.Subscription subscription;

            @Override
            public void onSubscribe(Flow.Subscription subscription) {
                this.subscription = subscription;
                log.debug("建立订阅 => {}", key);
                subscription.request(1);
            }

            @Override
            public void onNext(RecordInfoResponseDTO item) {
                sum.set(item.getSumNum());
                getNum.getAndAdd(item.getRecordList().size());
                list.addAll(item.getRecordList());
                log.info("获取订阅 => {}, {}/{}", key, getNum.get(), sum.get());
                if (getNum.get() >= sum.get()) {
                    onComplete();
                } else {
                    subscription.request(1);
                }
            }

            @Override
            public void onError(Throwable throwable) {

            }

            @Override
            public void onComplete() {
                subscribe.getRecordInfoSubscribe().delPublisher(key);
                result.setResult(JsonResponse.success(list));
                log.debug("订阅结束 => {}", key);
            }
        });

        result.onTimeout(()->{
            result.setResult(JsonResponse.success(list,"查询超时, 结果可能不完整"));
            subscribe.getRecordInfoSubscribe().delPublisher(key);
        });

        return result;
    }
}
