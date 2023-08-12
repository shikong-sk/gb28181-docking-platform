package cn.skcks.docking.gb28181.service.docking.device.cache;

import cn.hutool.core.date.DateUtil;
import cn.skcks.docking.gb28181.common.redis.RedisUtil;
import cn.skcks.docking.gb28181.core.sip.gb28181.cache.CacheUtil;
import cn.skcks.docking.gb28181.core.sip.gb28181.constant.DeviceConstant;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;

@Slf4j
@Service
@RequiredArgsConstructor
public class DeviceOnlineCacheService {
    private String getKey(String deviceId) {
        return CacheUtil.getKey(DeviceConstant.Cache.ONLINE,deviceId);
    }

    public boolean isOnline(String deviceId){
        return RedisUtil.KeyOps.hasKey(getKey(deviceId));
    }

    public void setOnline(String deviceId, long time, TimeUnit unit){
        String key = getKey(deviceId);
        RedisUtil.StringOps.set(key, DateUtil.now());
        RedisUtil.KeyOps.expire(key, time, unit);
    }

    public void setOffline(String deviceId){
        String key = getKey(deviceId);
        RedisUtil.KeyOps.delete(key);
    }
}
