package cn.skcks.docking.gb28181.service.record;

import cn.skcks.docking.gb28181.core.sip.service.SipService;
import cn.skcks.docking.gb28181.orm.mybatis.dynamic.model.DockingDevice;
import cn.skcks.docking.gb28181.service.docking.device.DockingDeviceService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.sip.SipProvider;

@Slf4j
@Service
@RequiredArgsConstructor
public class RecordService {
    private final DockingDeviceService deviceService;
    private final SipService sipService;

    public void requestRecordInfo(String deviceId){
        DockingDevice device = deviceService.getDevice(deviceId);
        if(device == null){
            log.info("未能找到 编码为 => {} 的设备", deviceId);
            return;
        }

        String transport = device.getTransport();
        String senderIp = device.getLocalIp();
        SipProvider provider = sipService.getProvider(transport, senderIp);
        log.info("provider => {}", provider);
    }
}
