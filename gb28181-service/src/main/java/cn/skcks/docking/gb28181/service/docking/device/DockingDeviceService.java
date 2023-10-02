package cn.skcks.docking.gb28181.service.docking.device;

import cn.hutool.core.date.DateUtil;
import cn.skcks.docking.gb28181.core.sip.dto.SipTransactionInfo;
import cn.skcks.docking.gb28181.core.sip.gb28181.constant.DeviceConstant;
import cn.skcks.docking.gb28181.orm.mybatis.dynamic.mapper.DockingDeviceDynamicSqlSupport;
import cn.skcks.docking.gb28181.orm.mybatis.dynamic.mapper.DockingDeviceMapper;
import cn.skcks.docking.gb28181.orm.mybatis.dynamic.model.DockingDevice;
import cn.skcks.docking.gb28181.service.docking.device.cache.DeviceOnlineCacheService;
import cn.skcks.docking.gb28181.service.docking.device.cache.DeviceOnlineTransactionCacheService;
import cn.skcks.docking.gb28181.service.docking.device.cache.DockingDeviceCacheService;
import gov.nist.javax.sip.message.SIPResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.mybatis.dynamic.sql.SqlBuilder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.sip.message.Response;

@Slf4j
@Service
@RequiredArgsConstructor
public class DockingDeviceService {
    private final DockingDeviceMapper dockingDeviceMapper;
    private final DockingDeviceCacheService deviceCacheService;
    private final DeviceOnlineCacheService onlineCacheService;
    private final DeviceOnlineTransactionCacheService transactionCacheService;

    /**
     * 根据设备Id 获取设备信息 并缓存
     * @param deviceId 设备id
     * @return 设备信息
     */
    public DockingDevice getDevice(String deviceId) {
        DockingDevice device = deviceCacheService.getDevice(deviceId);
        if (device == null) {
            device = dockingDeviceMapper
                    .selectOne((s -> s.where(DockingDeviceDynamicSqlSupport.deviceId, SqlBuilder.isEqualTo(deviceId))))
                    .orElse(null);
            if (device != null) {
                deviceCacheService.cacheDevice(deviceId, device);
            }
        }
        return device;
    }

    public boolean isOnline(String deviceId){
        return onlineCacheService.isOnline(deviceId);
    }

    @Transactional
    public void online(DockingDevice device, Response response) {
        String deviceId = device.getDeviceId();
        log.info("[设备上线] deviceId => {}, {}://{}:{}", deviceId, device.getTransport(), device.getIp(), device.getPort());
        device.setUpdateTime(DateUtil.now());
        if (device.getKeepaliveIntervalTime() == null || device.getKeepaliveIntervalTime() == 0) {
            // 默认心跳间隔60
            device.setKeepaliveIntervalTime(DeviceConstant.KEEP_ALIVE_INTERVAL);
        }
        dockingDeviceMapper
                .selectOne((s -> s.where(DockingDeviceDynamicSqlSupport.deviceId, SqlBuilder.isEqualTo(deviceId))))
                .ifPresentOrElse((ignore -> {
                    dockingDeviceMapper.updateByPrimaryKey(device);
                }),()->{
                    dockingDeviceMapper.insert(device);
                });
        deviceCacheService.removeDevice(deviceId);
        deviceCacheService.cacheDevice(deviceId,device);
        onlineCacheService.setOnline(deviceId, DeviceConstant.KEEP_ALIVE_INTERVAL * 3, DeviceConstant.UNIT);
        setTransaction(deviceId, response);
    }

    public void offline(DockingDevice device){
        String deviceId = device.getDeviceId();
        dockingDeviceMapper.updateByPrimaryKey(device);

        log.info("[设备离线] deviceId => {}", deviceId);
        deviceCacheService.removeDevice(deviceId);
        delTransaction(deviceId);
        onlineCacheService.setOffline(deviceId);
    }

    public boolean hasTransaction(String deviceId){
        return transactionCacheService.hasTransaction(deviceId);
    }

    public void setTransaction(String deviceId, Response response){
        SipTransactionInfo sipTransactionInfo = new SipTransactionInfo((SIPResponse)response);
        transactionCacheService.setTransaction(deviceId, sipTransactionInfo, DeviceConstant.KEEP_ALIVE_INTERVAL * 3, DeviceConstant.UNIT);
    }

    public SipTransactionInfo getTransaction(String deviceId){
        return transactionCacheService.getTransaction(deviceId);
    }

    public void delTransaction(String deviceId){
        transactionCacheService.delTransaction(deviceId);
    }
}
