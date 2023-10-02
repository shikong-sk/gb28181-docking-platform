package cn.skcks.docking.gb28181.orm.mybatis.dynamic.mapper;

import static cn.skcks.docking.gb28181.orm.mybatis.dynamic.mapper.DockingDeviceChannelDynamicSqlSupport.*;
import static org.mybatis.dynamic.sql.SqlBuilder.isEqualTo;

import cn.skcks.docking.gb28181.orm.mybatis.dynamic.model.DockingDeviceChannel;
import jakarta.annotation.Generated;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Result;
import org.apache.ibatis.annotations.ResultMap;
import org.apache.ibatis.annotations.Results;
import org.apache.ibatis.annotations.SelectProvider;
import org.apache.ibatis.type.JdbcType;
import org.mybatis.dynamic.sql.BasicColumn;
import org.mybatis.dynamic.sql.delete.DeleteDSLCompleter;
import org.mybatis.dynamic.sql.select.CountDSLCompleter;
import org.mybatis.dynamic.sql.select.SelectDSLCompleter;
import org.mybatis.dynamic.sql.select.render.SelectStatementProvider;
import org.mybatis.dynamic.sql.update.UpdateDSL;
import org.mybatis.dynamic.sql.update.UpdateDSLCompleter;
import org.mybatis.dynamic.sql.update.UpdateModel;
import org.mybatis.dynamic.sql.util.SqlProviderAdapter;
import org.mybatis.dynamic.sql.util.mybatis3.CommonCountMapper;
import org.mybatis.dynamic.sql.util.mybatis3.CommonDeleteMapper;
import org.mybatis.dynamic.sql.util.mybatis3.CommonInsertMapper;
import org.mybatis.dynamic.sql.util.mybatis3.CommonUpdateMapper;
import org.mybatis.dynamic.sql.util.mybatis3.MyBatis3Utils;

@Mapper
public interface DockingDeviceChannelMapper extends CommonCountMapper, CommonDeleteMapper, CommonInsertMapper<DockingDeviceChannel>, CommonUpdateMapper {
    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source Table: docking_device_channel")
    BasicColumn[] selectList = BasicColumn.columnList(id, gbDeviceId, gbDeviceChannelId, name, address);

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source Table: docking_device_channel")
    @SelectProvider(type=SqlProviderAdapter.class, method="select")
    @Results(id="DockingDeviceChannelResult", value = {
        @Result(column="id", property="id", jdbcType=JdbcType.BIGINT, id=true),
        @Result(column="gb_device_id", property="gbDeviceId", jdbcType=JdbcType.VARCHAR),
        @Result(column="gb_device_channel_id", property="gbDeviceChannelId", jdbcType=JdbcType.VARCHAR),
        @Result(column="name", property="name", jdbcType=JdbcType.VARCHAR),
        @Result(column="address", property="address", jdbcType=JdbcType.VARCHAR)
    })
    List<DockingDeviceChannel> selectMany(SelectStatementProvider selectStatement);

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source Table: docking_device_channel")
    @SelectProvider(type=SqlProviderAdapter.class, method="select")
    @ResultMap("DockingDeviceChannelResult")
    Optional<DockingDeviceChannel> selectOne(SelectStatementProvider selectStatement);

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source Table: docking_device_channel")
    default long count(CountDSLCompleter completer) {
        return MyBatis3Utils.countFrom(this::count, dockingDeviceChannel, completer);
    }

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source Table: docking_device_channel")
    default int delete(DeleteDSLCompleter completer) {
        return MyBatis3Utils.deleteFrom(this::delete, dockingDeviceChannel, completer);
    }

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source Table: docking_device_channel")
    default int deleteByPrimaryKey(Long id_) {
        return delete(c -> 
            c.where(id, isEqualTo(id_))
        );
    }

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source Table: docking_device_channel")
    default int insert(DockingDeviceChannel row) {
        return MyBatis3Utils.insert(this::insert, row, dockingDeviceChannel, c ->
            c.map(id).toProperty("id")
            .map(gbDeviceId).toProperty("gbDeviceId")
            .map(gbDeviceChannelId).toProperty("gbDeviceChannelId")
            .map(name).toProperty("name")
            .map(address).toProperty("address")
        );
    }

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source Table: docking_device_channel")
    default int insertMultiple(Collection<DockingDeviceChannel> records) {
        return MyBatis3Utils.insertMultiple(this::insertMultiple, records, dockingDeviceChannel, c ->
            c.map(id).toProperty("id")
            .map(gbDeviceId).toProperty("gbDeviceId")
            .map(gbDeviceChannelId).toProperty("gbDeviceChannelId")
            .map(name).toProperty("name")
            .map(address).toProperty("address")
        );
    }

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source Table: docking_device_channel")
    default int insertSelective(DockingDeviceChannel row) {
        return MyBatis3Utils.insert(this::insert, row, dockingDeviceChannel, c ->
            c.map(id).toPropertyWhenPresent("id", row::getId)
            .map(gbDeviceId).toPropertyWhenPresent("gbDeviceId", row::getGbDeviceId)
            .map(gbDeviceChannelId).toPropertyWhenPresent("gbDeviceChannelId", row::getGbDeviceChannelId)
            .map(name).toPropertyWhenPresent("name", row::getName)
            .map(address).toPropertyWhenPresent("address", row::getAddress)
        );
    }

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source Table: docking_device_channel")
    default Optional<DockingDeviceChannel> selectOne(SelectDSLCompleter completer) {
        return MyBatis3Utils.selectOne(this::selectOne, selectList, dockingDeviceChannel, completer);
    }

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source Table: docking_device_channel")
    default List<DockingDeviceChannel> select(SelectDSLCompleter completer) {
        return MyBatis3Utils.selectList(this::selectMany, selectList, dockingDeviceChannel, completer);
    }

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source Table: docking_device_channel")
    default List<DockingDeviceChannel> selectDistinct(SelectDSLCompleter completer) {
        return MyBatis3Utils.selectDistinct(this::selectMany, selectList, dockingDeviceChannel, completer);
    }

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source Table: docking_device_channel")
    default Optional<DockingDeviceChannel> selectByPrimaryKey(Long id_) {
        return selectOne(c ->
            c.where(id, isEqualTo(id_))
        );
    }

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source Table: docking_device_channel")
    default int update(UpdateDSLCompleter completer) {
        return MyBatis3Utils.update(this::update, dockingDeviceChannel, completer);
    }

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source Table: docking_device_channel")
    static UpdateDSL<UpdateModel> updateAllColumns(DockingDeviceChannel row, UpdateDSL<UpdateModel> dsl) {
        return dsl.set(id).equalTo(row::getId)
                .set(gbDeviceId).equalTo(row::getGbDeviceId)
                .set(gbDeviceChannelId).equalTo(row::getGbDeviceChannelId)
                .set(name).equalTo(row::getName)
                .set(address).equalTo(row::getAddress);
    }

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source Table: docking_device_channel")
    static UpdateDSL<UpdateModel> updateSelectiveColumns(DockingDeviceChannel row, UpdateDSL<UpdateModel> dsl) {
        return dsl.set(id).equalToWhenPresent(row::getId)
                .set(gbDeviceId).equalToWhenPresent(row::getGbDeviceId)
                .set(gbDeviceChannelId).equalToWhenPresent(row::getGbDeviceChannelId)
                .set(name).equalToWhenPresent(row::getName)
                .set(address).equalToWhenPresent(row::getAddress);
    }

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source Table: docking_device_channel")
    default int updateByPrimaryKey(DockingDeviceChannel row) {
        return update(c ->
            c.set(gbDeviceId).equalTo(row::getGbDeviceId)
            .set(gbDeviceChannelId).equalTo(row::getGbDeviceChannelId)
            .set(name).equalTo(row::getName)
            .set(address).equalTo(row::getAddress)
            .where(id, isEqualTo(row::getId))
        );
    }

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source Table: docking_device_channel")
    default int updateByPrimaryKeySelective(DockingDeviceChannel row) {
        return update(c ->
            c.set(gbDeviceId).equalToWhenPresent(row::getGbDeviceId)
            .set(gbDeviceChannelId).equalToWhenPresent(row::getGbDeviceChannelId)
            .set(name).equalToWhenPresent(row::getName)
            .set(address).equalToWhenPresent(row::getAddress)
            .where(id, isEqualTo(row::getId))
        );
    }
}