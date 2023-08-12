package cn.skcks.docking.gb28181.service.docking.device;

import cn.skcks.docking.gb28181.orm.mybatis.dynamic.mapper.DockingDeviceDynamicSqlSupport;
import cn.skcks.docking.gb28181.orm.mybatis.dynamic.mapper.DockingDeviceMapper;
import cn.skcks.docking.gb28181.orm.mybatis.dynamic.model.DockingDevice;
import cn.skcks.docking.gb28181.service.docking.device.cache.DockingDeviceCacheService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.mybatis.dynamic.sql.SqlBuilder;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class DockingDeviceService {
    private final DockingDeviceMapper dockingDeviceMapper;
    private final DockingDeviceCacheService deviceCacheService;

    public DockingDevice getDeviceInfo(String deviceId) {
        DockingDevice device = deviceCacheService.getDeviceInfo(deviceId);
        if (device == null) {
            device = dockingDeviceMapper
                    .selectOne((s -> s.where(DockingDeviceDynamicSqlSupport.deviceId, SqlBuilder.isEqualTo(deviceId))))
                    .orElse(null);
            if (device != null) {
                deviceCacheService.cacheDeviceInfo(deviceId, device);
            }
        }
        return device;
    }

    public void online(DockingDevice device) {
        String deviceId = device.getDeviceId();
        log.info("[设备上线] deviceId => {}, {}://{}:{}", deviceId, device.getTransport(), device.getIp(), device.getPort());
        dockingDeviceMapper.insert(device);
        deviceCacheService.cacheDeviceInfo(deviceId, device);
    }
}
