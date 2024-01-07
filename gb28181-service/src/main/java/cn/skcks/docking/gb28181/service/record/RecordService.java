package cn.skcks.docking.gb28181.service.record;

import cn.hutool.core.date.DateUtil;
import cn.skcks.docking.gb28181.common.json.JsonResponse;
import cn.skcks.docking.gb28181.common.json.ResponseStatus;
import cn.skcks.docking.gb28181.config.sip.SipConfig;
import cn.skcks.docking.gb28181.core.sip.gb28181.constant.CmdType;


import cn.skcks.docking.gb28181.core.sip.message.request.SipRequestBuilder;
import cn.skcks.docking.gb28181.core.sip.message.subscribe.GenericSubscribe;
import cn.skcks.docking.gb28181.core.sip.message.subscribe.SipSubscribe;
import cn.skcks.docking.gb28181.core.sip.service.SipService;
import cn.skcks.docking.gb28181.orm.mybatis.dynamic.model.DockingDevice;
import cn.skcks.docking.gb28181.orm.mybatis.dynamic.model.DockingDeviceChannel;
import cn.skcks.docking.gb28181.service.device.DeviceChannelService;
import cn.skcks.docking.gb28181.service.docking.device.DockingDeviceService;
import cn.skcks.docking.gb28181.service.record.convertor.RecordConvertor;
import cn.skcks.docking.gb28181.service.record.vo.RecordInfoItemVO;
import cn.skcks.docking.gb28181.sip.manscdp.recordinfo.request.RecordInfoRequestDTO;
import cn.skcks.docking.gb28181.sip.manscdp.recordinfo.response.RecordInfoItemDTO;
import cn.skcks.docking.gb28181.sip.manscdp.recordinfo.response.RecordInfoResponseDTO;
import cn.skcks.docking.gb28181.sip.method.message.request.MessageRequestBuilder;
import cn.skcks.docking.gb28181.sip.utils.MANSCDPUtils;
import gov.nist.javax.sip.message.SIPRequest;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.context.request.async.DeferredResult;

import javax.sip.SipProvider;
import javax.sip.message.Request;
import java.util.*;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicLong;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class RecordService {
    private final SipConfig sipConfig;
    private final DockingDeviceService deviceService;
    private final DeviceChannelService deviceChannelService;
    private final SipService sipService;
    private final SipSubscribe subscribe;
    /**
     *
     * @param deviceId 设备id
     * @param timeout 超时时间
     * @param date 查询日期
     */
    @SneakyThrows
    public DeferredResult<JsonResponse<List<RecordInfoItemVO>>> requestRecordInfo(String deviceId, String channelId, long timeout, Date date) {
        log.info("查询 设备 => {} {} 的历史媒体记录, 超时时间 {} 秒", deviceId, DateUtil.formatDate(date), timeout);
        DeferredResult<JsonResponse<List<RecordInfoItemVO>>> result = new DeferredResult<>(TimeUnit.SECONDS.toMillis(timeout));
        DockingDevice device = deviceService.getDevice(deviceId);
        if (device == null) {
            log.info("未能找到 编码为 => {} 的设备", deviceId);
            result.setResult(JsonResponse.error("未找到设备"));
            return result;
        }
        Optional<DockingDeviceChannel> deviceChannel = deviceChannelService.getDeviceChannel(deviceId, channelId);
        if(deviceChannel.isEmpty()){
            log.info("未能找到 设备编码为 => {}, 通道 => {} 的信息", deviceId, channelId);
            result.setResult(JsonResponse.error("未找到通道信息"));
            return result;
        }

        String transport = device.getTransport();
        String localIp = device.getLocalIp();
        SipProvider provider = sipService.getProvider(transport, localIp);
        String callId = provider.getNewCallId().getCallId();
        String sn = String.valueOf((int) (Math.random() * 9 + 1) * 100000);
        MessageRequestBuilder requestBuilder = MessageRequestBuilder.builder()
                .targetIp(device.getIp())
                .targetPort(device.getPort())
                .targetId(deviceId)
                .localId(sipConfig.getId())
                .localIp(localIp)
                .localPort(sipConfig.getPort())
                .transport(transport)
                .build();

        RecordInfoRequestDTO dto = RecordInfoRequestDTO.builder()
                .deviceId(channelId)
                .startTime(DateUtil.beginOfDay(date))
                .endTime(DateUtil.endOfDay(date))
                .sn(sn)
                .build();
        Request request = requestBuilder.createMessageRequest(callId,SipRequestBuilder.getCSeq(), MANSCDPUtils.toByteXml(dto, device.getCharset()));
        String key = GenericSubscribe.Helper.getKey(CmdType.RECORD_INFO, channelId, sn);
        subscribe.getSipRequestSubscribe().addPublisher(key);
        subscribe.getSipRequestSubscribe().addSubscribe(key, new RecordSubscriber(subscribe, key, result, deviceId));
        result.onTimeout(() -> {
            result.setResult(JsonResponse.build(ResponseStatus.PARTIAL_CONTENT,
                    RecordConvertor.INSTANCE.dto2Vo(Collections.emptyList()),
                    "查询超时, 结果可能不完整"));
            subscribe.getSipRequestSubscribe().delPublisher(key);
        });

        provider.sendRequest(request);
        return result;
    }

    private List<RecordInfoItemDTO> sortedRecordList(List<RecordInfoItemDTO> list){
        return list.stream().sorted((a,b)-> DateUtil.compare(a.getStartTime(),b.getStartTime())).collect(Collectors.toList());
    }
}
