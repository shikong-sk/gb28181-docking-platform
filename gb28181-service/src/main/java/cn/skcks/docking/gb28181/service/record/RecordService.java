package cn.skcks.docking.gb28181.service.record;

import cn.hutool.core.date.DateUtil;
import cn.skcks.docking.gb28181.common.json.JsonResponse;
import cn.skcks.docking.gb28181.common.json.ResponseStatus;
import cn.skcks.docking.gb28181.common.xml.XmlUtils;
import cn.skcks.docking.gb28181.core.sip.gb28181.constant.CmdType;
import cn.skcks.docking.gb28181.core.sip.message.processor.message.types.recordinfo.query.dto.RecordInfoRequestDTO;
import cn.skcks.docking.gb28181.core.sip.message.processor.message.types.recordinfo.reponse.dto.RecordInfoItemDTO;
import cn.skcks.docking.gb28181.core.sip.message.processor.message.types.recordinfo.reponse.dto.RecordInfoResponseDTO;
import cn.skcks.docking.gb28181.core.sip.message.request.SipRequestBuilder;
import cn.skcks.docking.gb28181.core.sip.message.sender.SipMessageSender;
import cn.skcks.docking.gb28181.core.sip.message.subscribe.GenericSubscribe;
import cn.skcks.docking.gb28181.core.sip.message.subscribe.SipSubscribe;
import cn.skcks.docking.gb28181.core.sip.service.SipService;
import cn.skcks.docking.gb28181.core.sip.utils.SipUtil;
import cn.skcks.docking.gb28181.orm.mybatis.dynamic.model.DockingDevice;
import cn.skcks.docking.gb28181.service.docking.device.DockingDeviceService;
import cn.skcks.docking.gb28181.service.record.convertor.RecordConvertor;
import cn.skcks.docking.gb28181.service.record.vo.RecordInfoItemVO;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.context.request.async.DeferredResult;

import javax.sip.SipProvider;
import javax.sip.header.CallIdHeader;
import javax.sip.message.Request;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicLong;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class RecordService {
    private final DockingDeviceService deviceService;
    private final SipService sipService;
    private final SipMessageSender sender;
    private final SipSubscribe subscribe;

    /**
     *
     * @param deviceId 设备id
     * @param timeout 超时时间
     * @param date 查询日期
     */
    @SneakyThrows
    public DeferredResult<JsonResponse<List<RecordInfoItemVO>>> requestRecordInfo(String deviceId, long timeout, Date date) {
        log.info("查询 设备 => {} {} 的历史媒体记录, 超时时间 {} 秒", deviceId, DateUtil.formatDate(date), timeout);
        DeferredResult<JsonResponse<List<RecordInfoItemVO>>> result = new DeferredResult<>(TimeUnit.SECONDS.toMillis(timeout));

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
                .startTime(DateUtil.beginOfDay(date))
                .endTime(DateUtil.endOfDay(date))
                .sn(sn)
                .build();
        Request request = SipRequestBuilder.createMessageRequest(device,
                XmlUtils.toXml(dto),
                SipUtil.generateViaTag(),
                SipUtil.generateFromTag(),
                null,
                callId);

        String key = GenericSubscribe.Helper.getKey(CmdType.RECORD_INFO, deviceId, sn);
        subscribe.getRecordInfoSubscribe().addPublisher(key);
        sender.send(senderIp, request);
        List<RecordInfoItemDTO> list = new ArrayList<>();
        AtomicLong atomicSum = new AtomicLong(0);
        AtomicLong atomicNum = new AtomicLong(0);
        ScheduledExecutorService scheduledExecutorService = Executors.newSingleThreadScheduledExecutor();
        final ScheduledFuture<?>[] schedule = new ScheduledFuture<?>[1];
        Flow.Subscriber<RecordInfoResponseDTO> subscriber = new Flow.Subscriber<>() {
            Flow.Subscription subscription;

            @Override
            public void onSubscribe(Flow.Subscription subscription) {
                this.subscription = subscription;
                log.debug("建立订阅 => {}", key);
                subscription.request(1);
            }

            @Override
            public void onNext(RecordInfoResponseDTO item) {
                atomicSum.set(item.getSumNum());
                atomicNum.getAndAdd(item.getRecordList().size());
                list.addAll(item.getRecordList());
                long num = atomicNum.get();
                long sum = atomicSum.get();
                if(num > sum){
                    log.warn("检测到 设备 => {}, 未按规范实现, 订阅 => {}, 期望总数为 => {}, 已接收数量 => {}", deviceId, key, atomicSum.get(), atomicNum.get());
                } else {
                    log.info("获取订阅 => {}, {}/{}", key, atomicNum.get(), atomicSum.get());
                }

                if (num >= sum) {
                    // 针对某些不按规范的设备
                    // 如果已获取数量 >= 约定的总数
                    // 就执行定时任务, 若 500ms 内未收到新的数据视为已结束
                    if(schedule[0] != null){
                        schedule[0].cancel(true);
                    }
                    schedule[0] = scheduledExecutorService.schedule(this::onComplete, 500, TimeUnit.MILLISECONDS);
                }
                subscription.request(1);
            }

            @Override
            public void onError(Throwable throwable) {

            }

            @Override
            public void onComplete() {
                schedule[0].cancel(true);
                subscribe.getRecordInfoSubscribe().delPublisher(key);
                result.setResult(JsonResponse.success(RecordConvertor.INSTANCE.dto2Vo(sortedRecordList(list))));
                log.debug("订阅结束 => {}", key);
            }
        };

        subscribe.getRecordInfoSubscribe().addSubscribe(key, subscriber);

        result.onTimeout(() -> {
            result.setResult(JsonResponse.build(ResponseStatus.PARTIAL_CONTENT,
                    RecordConvertor.INSTANCE.dto2Vo(sortedRecordList(list)),
                    "查询超时, 结果可能不完整"));
            subscribe.getRecordInfoSubscribe().delPublisher(key);
        });

        return result;
    }

    private List<RecordInfoItemDTO> sortedRecordList(List<RecordInfoItemDTO> list){
        return list.stream().sorted((a,b)-> DateUtil.compare(a.getStartTime(),b.getStartTime())).collect(Collectors.toList());
    }
}