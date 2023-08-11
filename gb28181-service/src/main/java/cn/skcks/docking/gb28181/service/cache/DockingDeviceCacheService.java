package cn.skcks.docking.gb28181.service.cache;

import cn.skcks.docking.gb28181.common.json.JsonUtils;
import cn.skcks.docking.gb28181.common.redis.RedisUtil;
import cn.skcks.docking.gb28181.orm.mybatis.dynamic.model.DockingDevice;
import cn.skcks.docking.gb28181.service.docking.DockingDeviceService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

@Slf4j
@RequiredArgsConstructor
@Service
public class DockingDeviceCacheService {
    private final DockingDeviceService dockingDeviceService;
    public static final String PREFIX = "DockingDevice";

    public DockingDevice getDeviceInfo(String deviceId) {
        String key = StringUtils.joinWith(":", PREFIX, deviceId);
        String json = RedisUtil.StringOps.get(key);
        if (json == null){
            DockingDevice device = dockingDeviceService.getDeviceInfo(deviceId);
            if(device != null){
                RedisUtil.StringOps.set(key, JsonUtils.toCompressJson(device));
            }
            return device;
        }
        return JsonUtils.parse(json, DockingDevice.class);
    }
}
