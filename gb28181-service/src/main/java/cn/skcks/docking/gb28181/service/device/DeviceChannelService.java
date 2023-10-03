package cn.skcks.docking.gb28181.service.device;

import cn.skcks.docking.gb28181.common.json.JsonException;
import cn.skcks.docking.gb28181.common.json.JsonUtils;
import cn.skcks.docking.gb28181.orm.mybatis.dynamic.mapper.DockingDeviceChannelDynamicSqlSupport;
import cn.skcks.docking.gb28181.orm.mybatis.dynamic.mapper.DockingDeviceChannelMapper;
import cn.skcks.docking.gb28181.orm.mybatis.dynamic.model.DockingDeviceChannel;
import com.github.pagehelper.ISelect;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.mybatis.dynamic.sql.AndOrCriteriaGroup;
import org.mybatis.dynamic.sql.SqlBuilder;
import org.mybatis.dynamic.sql.where.WhereDSL;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

import static org.mybatis.dynamic.sql.SqlBuilder.isEqualTo;

@Slf4j
@Service
@RequiredArgsConstructor
public class DeviceChannelService {
    private final DockingDeviceChannelMapper deviceChannelMapper;

    public Optional<DockingDeviceChannel> getDeviceChannelById(Long id){
        return deviceChannelMapper.selectOne(s->s.where(DockingDeviceChannelDynamicSqlSupport.id, isEqualTo(id)));
    }

    public List<DockingDeviceChannel> getDeviceByGbDeviceId(String gbDeviceId){
        return deviceChannelMapper.select(s->
                s.where(DockingDeviceChannelDynamicSqlSupport.gbDeviceId,isEqualTo(gbDeviceId)));
    }

    public Optional<DockingDeviceChannel> getDeviceChannel(String gbDeviceId,String gbDeviceChannelId){
        return deviceChannelMapper.selectOne(s->
                s.where()
                        .and(DockingDeviceChannelDynamicSqlSupport.gbDeviceId, isEqualTo(gbDeviceId))
                        .and(DockingDeviceChannelDynamicSqlSupport.gbDeviceChannelId, isEqualTo(gbDeviceChannelId))
                        .limit(1));
    }

    /**
     * 分页查询
     * @param page 页数
     * @param size 数量
     * @param select 查询语句
     * @return 分页设备
     */
    public PageInfo<DockingDeviceChannel> getWithPage(int page, int size, ISelect select){
        PageInfo<DockingDeviceChannel> pageInfo;
        try (Page<DockingDeviceChannel> startPage = PageHelper.startPage(page, size)) {
            pageInfo = startPage.doSelectPageInfo(select);
        }
        return pageInfo;
    }

    public PageInfo<DockingDeviceChannel> getWithPage(int page, int size){
        ISelect select = () -> deviceChannelMapper.select(u -> u.orderBy(DockingDeviceChannelDynamicSqlSupport.id.descending()));
        return getWithPage(page, size, select);
    }

    @SneakyThrows
    @Transactional
    public boolean add(DockingDeviceChannel model){
        if(model == null){
            return false;
        }

        String gbDeviceId = model.getGbDeviceId();
        String gbDeviceChannelId = model.getGbDeviceChannelId();
        if(StringUtils.isAnyBlank(gbDeviceChannelId,gbDeviceId)){
            throw new JsonException("国标id 或 通道id 不能为空");
        }

        Optional<DockingDeviceChannel> deviceChannel = getDeviceChannel(gbDeviceId, gbDeviceChannelId);
        log.info("{}", JsonUtils.toJson(deviceChannel.orElse(null)));
        return deviceChannelMapper.insert(model) > 0;
    }

    @SneakyThrows
    @Transactional
    public boolean del(DockingDeviceChannel model){
        if(model == null){
            return false;
        }

        Long id = model.getId();
        String gbDeviceId = model.getGbDeviceId();
        String gbDeviceChannelId = model.getGbDeviceChannelId();
        if(id != null){
            return deviceChannelMapper.deleteByPrimaryKey(id) > 0;
        }

        if(StringUtils.isBlank(gbDeviceId)){
            throw new JsonException("国标id 不能为空");
        }

        if(StringUtils.isBlank(gbDeviceChannelId)){
            return deviceChannelMapper.delete(d->d.where(DockingDeviceChannelDynamicSqlSupport.gbDeviceId, isEqualTo(gbDeviceId))) > 0;
        }

        return deviceChannelMapper.delete(d->d.where(DockingDeviceChannelDynamicSqlSupport.gbDeviceId, isEqualTo(gbDeviceId))) > 0;
    }
}
