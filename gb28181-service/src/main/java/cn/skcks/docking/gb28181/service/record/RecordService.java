package cn.skcks.docking.gb28181.service.record;

import cn.hutool.core.date.DateUtil;
import cn.skcks.docking.gb28181.common.xml.XmlUtils;
import cn.skcks.docking.gb28181.core.sip.message.processor.message.types.recordinfo.query.dto.RecordInfoRequestDTO;
import cn.skcks.docking.gb28181.core.sip.message.request.SipRequestBuilder;
import cn.skcks.docking.gb28181.core.sip.message.sender.SipMessageSender;
import cn.skcks.docking.gb28181.core.sip.service.SipService;
import cn.skcks.docking.gb28181.core.sip.utils.SipUtil;
import cn.skcks.docking.gb28181.orm.mybatis.dynamic.model.DockingDevice;
import cn.skcks.docking.gb28181.service.docking.device.DockingDeviceService;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.sip.SipProvider;
import javax.sip.header.CallIdHeader;
import javax.sip.message.Request;

@Slf4j
@Service
@RequiredArgsConstructor
public class RecordService {
    private final DockingDeviceService deviceService;
    private final SipService sipService;
    private final SipMessageSender sender;

    @SneakyThrows
    public void requestRecordInfo(String deviceId){
        DockingDevice device = deviceService.getDevice(deviceId);
        if (device == null) {
            log.info("未能找到 编码为 => {} 的设备", deviceId);
            return;
        }

        String transport = device.getTransport();
        String senderIp = device.getLocalIp();
        SipProvider provider = sipService.getProvider(transport, senderIp);
        CallIdHeader callId = provider.getNewCallId();
        RecordInfoRequestDTO dto = RecordInfoRequestDTO.builder()
                .deviceId(deviceId)
                .startTime(DateUtil.beginOfDay(DateUtil.date()))
                .endTime(DateUtil.endOfDay(DateUtil.date()))
                .sn(String.valueOf((int)(Math.random() * 9 + 1) * 100000))
                .build();
        Request request = SipRequestBuilder.createMessageRequest(device,
                XmlUtils.toXml(dto),
                SipUtil.generateViaTag(),
                SipUtil.generateFromTag(),
                null,
                callId);
        sender.send(senderIp, request);
    }
}
