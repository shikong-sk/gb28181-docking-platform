package cn.skcks.docking.gb28181.service.docking;

import cn.skcks.docking.gb28181.common.redis.RedisUtil;
import cn.skcks.docking.gb28181.orm.mybatis.dynamic.mapper.DockingDeviceDynamicSqlSupport;
import cn.skcks.docking.gb28181.orm.mybatis.dynamic.mapper.DockingDeviceMapper;
import cn.skcks.docking.gb28181.orm.mybatis.dynamic.model.DockingDevice;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.mybatis.dynamic.sql.SqlBuilder;
import org.springframework.stereotype.Service;

@Slf4j
@RequiredArgsConstructor
@Service
public class DockingDeviceService {
    private final DockingDeviceMapper dockingDeviceMapper;

    public DockingDevice getDeviceInfo(String deviceId){
        return dockingDeviceMapper.selectOne((s -> {
            return s.where(DockingDeviceDynamicSqlSupport.deviceId, SqlBuilder.isEqualTo(deviceId));
        })).orElse(null);
    }
}
