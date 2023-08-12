package cn.skcks.docking.gb28181.service.docking.device.cache;

import cn.skcks.docking.gb28181.common.json.JsonUtils;
import cn.skcks.docking.gb28181.common.redis.RedisUtil;
import cn.skcks.docking.gb28181.core.sip.gb28181.cache.CacheUtil;
import cn.skcks.docking.gb28181.core.sip.gb28181.constant.DeviceConstant;
import cn.skcks.docking.gb28181.orm.mybatis.dynamic.model.DockingDevice;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

@Slf4j
@RequiredArgsConstructor
@Service
public class DockingDeviceCacheService {
    private String getKey(String deviceId) {
        return CacheUtil.getKey(DeviceConstant.Cache.DEVICE,deviceId);
    }

    public DockingDevice getDevice(String deviceId) {
        String key = getKey(deviceId);
        String json = RedisUtil.StringOps.get(key);
        if (json == null){
            return null;
        }
        return JsonUtils.parse(json, DockingDevice.class);
    }

    public void removeDevice(String deviceId) {
        String key = getKey(deviceId);
        RedisUtil.KeyOps.delete(key);
    }

    public void cacheDevice(String deviceId, DockingDevice device) {
        String key = getKey(deviceId);
        RedisUtil.StringOps.set(key,JsonUtils.toCompressJson(device));
    }
}
