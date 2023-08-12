package cn.skcks.docking.gb28181.service.docking.device;

import cn.hutool.core.date.DateUtil;
import cn.skcks.docking.gb28181.core.sip.gb28181.constant.DeviceConstant;
import cn.skcks.docking.gb28181.orm.mybatis.dynamic.mapper.DockingDeviceDynamicSqlSupport;
import cn.skcks.docking.gb28181.orm.mybatis.dynamic.mapper.DockingDeviceMapper;
import cn.skcks.docking.gb28181.orm.mybatis.dynamic.model.DockingDevice;
import cn.skcks.docking.gb28181.service.docking.device.cache.DeviceOnlineCacheService;
import cn.skcks.docking.gb28181.service.docking.device.cache.DockingDeviceCacheService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.math.NumberUtils;
import org.mybatis.dynamic.sql.SqlBuilder;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;

@Slf4j
@Service
@RequiredArgsConstructor
public class DockingDeviceService {
    private final DockingDeviceMapper dockingDeviceMapper;
    private final DockingDeviceCacheService deviceCacheService;
    private final DeviceOnlineCacheService onlineCacheService;

    /**
     * 根据设备Id 获取设备信息 并缓存
     * @param deviceId 设备id
     * @return 设备信息
     */
    public DockingDevice getDeviceInfo(String deviceId) {
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

    public void online(DockingDevice device) {
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

        getDeviceInfo(deviceId);
        onlineCacheService.setOnline(deviceId, 180, TimeUnit.SECONDS);
    }

    public void offline(DockingDevice device){
        String deviceId = device.getDeviceId();
        dockingDeviceMapper.updateByPrimaryKey(device);

        log.info("[设备离线] deviceId => {}", deviceId);
        deviceCacheService.removeDevice(deviceId);
        onlineCacheService.setOffline(deviceId);
    }
}
