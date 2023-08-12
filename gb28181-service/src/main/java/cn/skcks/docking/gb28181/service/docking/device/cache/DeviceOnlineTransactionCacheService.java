package cn.skcks.docking.gb28181.service.docking.device.cache;

import cn.skcks.docking.gb28181.common.json.JsonUtils;
import cn.skcks.docking.gb28181.common.redis.RedisUtil;
import cn.skcks.docking.gb28181.core.sip.dto.SipTransactionInfo;
import cn.skcks.docking.gb28181.core.sip.gb28181.cache.CacheUtil;
import cn.skcks.docking.gb28181.core.sip.gb28181.constant.DeviceConstant;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;

@Slf4j
@Service
@RequiredArgsConstructor
public class DeviceOnlineTransactionCacheService {
    private String getKey(String deviceId) {
        return CacheUtil.getKey(DeviceConstant.Cache.TRANSACTION,deviceId);
    }

    public boolean hasTransaction(String deviceId){
        return RedisUtil.KeyOps.hasKey(getKey(deviceId));
    }

    public void setTransaction(String deviceId, SipTransactionInfo transaction, long time, TimeUnit unit){
        String key = getKey(deviceId);
        RedisUtil.StringOps.set(key, JsonUtils.toCompressJson(transaction));
        RedisUtil.KeyOps.expire(key, time, unit);
    }

    public SipTransactionInfo getTransaction(String deviceId){
        String key = getKey(deviceId);
        String json = RedisUtil.StringOps.get(key);
        if(json == null){
            return null;
        } else {
            return JsonUtils.parse(json,SipTransactionInfo.class);
        }
    }

    public void delTransaction(String deviceId){
        String key = getKey(deviceId);
        RedisUtil.KeyOps.delete(key);
    }
}
