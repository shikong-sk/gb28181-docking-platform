package cn.skcks.docking.gb28181.service.record;

import cn.hutool.core.date.DateUtil;
import cn.skcks.docking.gb28181.common.json.JsonResponse;
import cn.skcks.docking.gb28181.core.sip.message.subscribe.SipSubscribe;
import cn.skcks.docking.gb28181.service.record.convertor.RecordConvertor;
import cn.skcks.docking.gb28181.service.record.vo.RecordInfoItemVO;
import cn.skcks.docking.gb28181.sip.manscdp.recordinfo.response.RecordInfoItemDTO;
import cn.skcks.docking.gb28181.sip.manscdp.recordinfo.response.RecordInfoResponseDTO;
import cn.skcks.docking.gb28181.sip.utils.MANSCDPUtils;
import gov.nist.javax.sip.message.SIPRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.context.request.async.DeferredResult;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Flow;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicLong;
import java.util.stream.Collectors;

@Slf4j
@RequiredArgsConstructor
public class RecordSubscriber implements Flow.Subscriber<SIPRequest>{
    private final SipSubscribe subscribe;
    private final String key;
    private final DeferredResult<JsonResponse<List<RecordInfoItemVO>>> result;
    private final String deviceId;

    private final List<RecordInfoItemDTO> list = new ArrayList<>();
    private final AtomicLong atomicSum = new AtomicLong(0);
    private final AtomicLong atomicNum = new AtomicLong(0);
    private Flow.Subscription subscription;

    @Override
    public void onSubscribe(Flow.Subscription subscription) {
        this.subscription = subscription;
        log.debug("建立订阅 => {}", key);
        subscription.request(1);
    }

    @Override
    public void onNext(SIPRequest item) {
        RecordInfoResponseDTO data = MANSCDPUtils.parse(item.getRawContent(), RecordInfoResponseDTO.class);
        atomicSum.set(Math.max(data.getSumNum(), atomicNum.get()));
        atomicNum.addAndGet(data.getRecordList().getNum());
        list.addAll(data.getRecordList().getRecordList());
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
            subscribe.getSipRequestSubscribe().refreshPublisher(key,500, TimeUnit.MILLISECONDS);
        }
        subscription.request(1);
    }

    @Override
    public void onError(Throwable throwable) {

    }

    @Override
    public void onComplete() {
        result.setResult(JsonResponse.success(RecordConvertor.INSTANCE.dto2Vo(sortedRecordList(list))));
        log.debug("订阅结束 => {}", key);
        subscribe.getSipRequestSubscribe().delPublisher(key);
    }

    private List<RecordInfoItemDTO> sortedRecordList(List<RecordInfoItemDTO> list){
        return list.stream().sorted((a,b)-> DateUtil.compare(a.getStartTime(),b.getStartTime())).collect(Collectors.toList());
    }
}
