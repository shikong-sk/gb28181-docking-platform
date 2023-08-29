package cn.skcks.docking.gb28181.service.info;

import cn.skcks.docking.gb28181.orm.mybatis.dynamic.mapper.DockingDeviceMapper;
import cn.skcks.docking.gb28181.orm.mybatis.dynamic.model.DockingDevice;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class InfoService {
    private final DockingDeviceMapper dockingDeviceMapper;

    public List<DockingDevice> getDeviceList(){
//        PageHelper.
        return dockingDeviceMapper.select((d->d));
    }
}
