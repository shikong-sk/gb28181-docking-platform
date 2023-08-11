package cn.skcks.docking.gb28181.orm.mybatis.dynamic.mapper;

import static cn.skcks.docking.gb28181.orm.mybatis.dynamic.mapper.DockingDeviceDynamicSqlSupport.*;
import static org.mybatis.dynamic.sql.SqlBuilder.isEqualTo;

import cn.skcks.docking.gb28181.orm.mybatis.dynamic.model.DockingDevice;
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
public interface DockingDeviceMapper extends CommonCountMapper, CommonDeleteMapper, CommonInsertMapper<DockingDevice>, CommonUpdateMapper {
    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source Table: docking_device")
    BasicColumn[] selectList = BasicColumn.columnList(id, deviceId, name, manufacturer, model, firmware, transport, streamMode, onLine, registerTime, keepaliveTime, ip, createTime, updateTime, port, expires, subscribeCycleForCatalog, subscribeCycleForMobilePosition, mobilePositionSubmissionInterval, subscribeCycleForAlarm, hostAddress, charset, ssrcCheck, geoCoordSys, mediaServerId, customName, sdpIp, localIp, password, asMessageChannel, keepaliveIntervalTime, switchPrimarySubStream, broadcastPushAfterAck);

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source Table: docking_device")
    @SelectProvider(type=SqlProviderAdapter.class, method="select")
    @Results(id="DockingDeviceResult", value = {
        @Result(column="id", property="id", jdbcType=JdbcType.BIGINT, id=true),
        @Result(column="device_id", property="deviceId", jdbcType=JdbcType.VARCHAR),
        @Result(column="name", property="name", jdbcType=JdbcType.VARCHAR),
        @Result(column="manufacturer", property="manufacturer", jdbcType=JdbcType.VARCHAR),
        @Result(column="model", property="model", jdbcType=JdbcType.VARCHAR),
        @Result(column="firmware", property="firmware", jdbcType=JdbcType.VARCHAR),
        @Result(column="transport", property="transport", jdbcType=JdbcType.VARCHAR),
        @Result(column="stream_mode", property="streamMode", jdbcType=JdbcType.VARCHAR),
        @Result(column="on_line", property="onLine", jdbcType=JdbcType.BIT),
        @Result(column="register_time", property="registerTime", jdbcType=JdbcType.VARCHAR),
        @Result(column="keepalive_time", property="keepaliveTime", jdbcType=JdbcType.VARCHAR),
        @Result(column="ip", property="ip", jdbcType=JdbcType.VARCHAR),
        @Result(column="create_time", property="createTime", jdbcType=JdbcType.VARCHAR),
        @Result(column="update_time", property="updateTime", jdbcType=JdbcType.VARCHAR),
        @Result(column="port", property="port", jdbcType=JdbcType.INTEGER),
        @Result(column="expires", property="expires", jdbcType=JdbcType.INTEGER),
        @Result(column="subscribe_cycle_for_catalog", property="subscribeCycleForCatalog", jdbcType=JdbcType.INTEGER),
        @Result(column="subscribe_cycle_for_mobile_position", property="subscribeCycleForMobilePosition", jdbcType=JdbcType.INTEGER),
        @Result(column="mobile_position_submission_interval", property="mobilePositionSubmissionInterval", jdbcType=JdbcType.INTEGER),
        @Result(column="subscribe_cycle_for_alarm", property="subscribeCycleForAlarm", jdbcType=JdbcType.INTEGER),
        @Result(column="host_address", property="hostAddress", jdbcType=JdbcType.VARCHAR),
        @Result(column="charset", property="charset", jdbcType=JdbcType.VARCHAR),
        @Result(column="ssrc_check", property="ssrcCheck", jdbcType=JdbcType.BIT),
        @Result(column="geo_coord_sys", property="geoCoordSys", jdbcType=JdbcType.VARCHAR),
        @Result(column="media_server_id", property="mediaServerId", jdbcType=JdbcType.VARCHAR),
        @Result(column="custom_name", property="customName", jdbcType=JdbcType.VARCHAR),
        @Result(column="sdp_ip", property="sdpIp", jdbcType=JdbcType.VARCHAR),
        @Result(column="local_ip", property="localIp", jdbcType=JdbcType.VARCHAR),
        @Result(column="password", property="password", jdbcType=JdbcType.VARCHAR),
        @Result(column="as_message_channel", property="asMessageChannel", jdbcType=JdbcType.BIT),
        @Result(column="keepalive_interval_time", property="keepaliveIntervalTime", jdbcType=JdbcType.INTEGER),
        @Result(column="switch_primary_sub_stream", property="switchPrimarySubStream", jdbcType=JdbcType.BIT),
        @Result(column="broadcast_push_after_ack", property="broadcastPushAfterAck", jdbcType=JdbcType.BIT)
    })
    List<DockingDevice> selectMany(SelectStatementProvider selectStatement);

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source Table: docking_device")
    @SelectProvider(type=SqlProviderAdapter.class, method="select")
    @ResultMap("DockingDeviceResult")
    Optional<DockingDevice> selectOne(SelectStatementProvider selectStatement);

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source Table: docking_device")
    default long count(CountDSLCompleter completer) {
        return MyBatis3Utils.countFrom(this::count, dockingDevice, completer);
    }

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source Table: docking_device")
    default int delete(DeleteDSLCompleter completer) {
        return MyBatis3Utils.deleteFrom(this::delete, dockingDevice, completer);
    }

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source Table: docking_device")
    default int deleteByPrimaryKey(Long id_) {
        return delete(c -> 
            c.where(id, isEqualTo(id_))
        );
    }

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source Table: docking_device")
    default int insert(DockingDevice row) {
        return MyBatis3Utils.insert(this::insert, row, dockingDevice, c ->
            c.map(id).toProperty("id")
            .map(deviceId).toProperty("deviceId")
            .map(name).toProperty("name")
            .map(manufacturer).toProperty("manufacturer")
            .map(model).toProperty("model")
            .map(firmware).toProperty("firmware")
            .map(transport).toProperty("transport")
            .map(streamMode).toProperty("streamMode")
            .map(onLine).toProperty("onLine")
            .map(registerTime).toProperty("registerTime")
            .map(keepaliveTime).toProperty("keepaliveTime")
            .map(ip).toProperty("ip")
            .map(createTime).toProperty("createTime")
            .map(updateTime).toProperty("updateTime")
            .map(port).toProperty("port")
            .map(expires).toProperty("expires")
            .map(subscribeCycleForCatalog).toProperty("subscribeCycleForCatalog")
            .map(subscribeCycleForMobilePosition).toProperty("subscribeCycleForMobilePosition")
            .map(mobilePositionSubmissionInterval).toProperty("mobilePositionSubmissionInterval")
            .map(subscribeCycleForAlarm).toProperty("subscribeCycleForAlarm")
            .map(hostAddress).toProperty("hostAddress")
            .map(charset).toProperty("charset")
            .map(ssrcCheck).toProperty("ssrcCheck")
            .map(geoCoordSys).toProperty("geoCoordSys")
            .map(mediaServerId).toProperty("mediaServerId")
            .map(customName).toProperty("customName")
            .map(sdpIp).toProperty("sdpIp")
            .map(localIp).toProperty("localIp")
            .map(password).toProperty("password")
            .map(asMessageChannel).toProperty("asMessageChannel")
            .map(keepaliveIntervalTime).toProperty("keepaliveIntervalTime")
            .map(switchPrimarySubStream).toProperty("switchPrimarySubStream")
            .map(broadcastPushAfterAck).toProperty("broadcastPushAfterAck")
        );
    }

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source Table: docking_device")
    default int insertMultiple(Collection<DockingDevice> records) {
        return MyBatis3Utils.insertMultiple(this::insertMultiple, records, dockingDevice, c ->
            c.map(id).toProperty("id")
            .map(deviceId).toProperty("deviceId")
            .map(name).toProperty("name")
            .map(manufacturer).toProperty("manufacturer")
            .map(model).toProperty("model")
            .map(firmware).toProperty("firmware")
            .map(transport).toProperty("transport")
            .map(streamMode).toProperty("streamMode")
            .map(onLine).toProperty("onLine")
            .map(registerTime).toProperty("registerTime")
            .map(keepaliveTime).toProperty("keepaliveTime")
            .map(ip).toProperty("ip")
            .map(createTime).toProperty("createTime")
            .map(updateTime).toProperty("updateTime")
            .map(port).toProperty("port")
            .map(expires).toProperty("expires")
            .map(subscribeCycleForCatalog).toProperty("subscribeCycleForCatalog")
            .map(subscribeCycleForMobilePosition).toProperty("subscribeCycleForMobilePosition")
            .map(mobilePositionSubmissionInterval).toProperty("mobilePositionSubmissionInterval")
            .map(subscribeCycleForAlarm).toProperty("subscribeCycleForAlarm")
            .map(hostAddress).toProperty("hostAddress")
            .map(charset).toProperty("charset")
            .map(ssrcCheck).toProperty("ssrcCheck")
            .map(geoCoordSys).toProperty("geoCoordSys")
            .map(mediaServerId).toProperty("mediaServerId")
            .map(customName).toProperty("customName")
            .map(sdpIp).toProperty("sdpIp")
            .map(localIp).toProperty("localIp")
            .map(password).toProperty("password")
            .map(asMessageChannel).toProperty("asMessageChannel")
            .map(keepaliveIntervalTime).toProperty("keepaliveIntervalTime")
            .map(switchPrimarySubStream).toProperty("switchPrimarySubStream")
            .map(broadcastPushAfterAck).toProperty("broadcastPushAfterAck")
        );
    }

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source Table: docking_device")
    default int insertSelective(DockingDevice row) {
        return MyBatis3Utils.insert(this::insert, row, dockingDevice, c ->
            c.map(id).toPropertyWhenPresent("id", row::getId)
            .map(deviceId).toPropertyWhenPresent("deviceId", row::getDeviceId)
            .map(name).toPropertyWhenPresent("name", row::getName)
            .map(manufacturer).toPropertyWhenPresent("manufacturer", row::getManufacturer)
            .map(model).toPropertyWhenPresent("model", row::getModel)
            .map(firmware).toPropertyWhenPresent("firmware", row::getFirmware)
            .map(transport).toPropertyWhenPresent("transport", row::getTransport)
            .map(streamMode).toPropertyWhenPresent("streamMode", row::getStreamMode)
            .map(onLine).toPropertyWhenPresent("onLine", row::getOnLine)
            .map(registerTime).toPropertyWhenPresent("registerTime", row::getRegisterTime)
            .map(keepaliveTime).toPropertyWhenPresent("keepaliveTime", row::getKeepaliveTime)
            .map(ip).toPropertyWhenPresent("ip", row::getIp)
            .map(createTime).toPropertyWhenPresent("createTime", row::getCreateTime)
            .map(updateTime).toPropertyWhenPresent("updateTime", row::getUpdateTime)
            .map(port).toPropertyWhenPresent("port", row::getPort)
            .map(expires).toPropertyWhenPresent("expires", row::getExpires)
            .map(subscribeCycleForCatalog).toPropertyWhenPresent("subscribeCycleForCatalog", row::getSubscribeCycleForCatalog)
            .map(subscribeCycleForMobilePosition).toPropertyWhenPresent("subscribeCycleForMobilePosition", row::getSubscribeCycleForMobilePosition)
            .map(mobilePositionSubmissionInterval).toPropertyWhenPresent("mobilePositionSubmissionInterval", row::getMobilePositionSubmissionInterval)
            .map(subscribeCycleForAlarm).toPropertyWhenPresent("subscribeCycleForAlarm", row::getSubscribeCycleForAlarm)
            .map(hostAddress).toPropertyWhenPresent("hostAddress", row::getHostAddress)
            .map(charset).toPropertyWhenPresent("charset", row::getCharset)
            .map(ssrcCheck).toPropertyWhenPresent("ssrcCheck", row::getSsrcCheck)
            .map(geoCoordSys).toPropertyWhenPresent("geoCoordSys", row::getGeoCoordSys)
            .map(mediaServerId).toPropertyWhenPresent("mediaServerId", row::getMediaServerId)
            .map(customName).toPropertyWhenPresent("customName", row::getCustomName)
            .map(sdpIp).toPropertyWhenPresent("sdpIp", row::getSdpIp)
            .map(localIp).toPropertyWhenPresent("localIp", row::getLocalIp)
            .map(password).toPropertyWhenPresent("password", row::getPassword)
            .map(asMessageChannel).toPropertyWhenPresent("asMessageChannel", row::getAsMessageChannel)
            .map(keepaliveIntervalTime).toPropertyWhenPresent("keepaliveIntervalTime", row::getKeepaliveIntervalTime)
            .map(switchPrimarySubStream).toPropertyWhenPresent("switchPrimarySubStream", row::getSwitchPrimarySubStream)
            .map(broadcastPushAfterAck).toPropertyWhenPresent("broadcastPushAfterAck", row::getBroadcastPushAfterAck)
        );
    }

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source Table: docking_device")
    default Optional<DockingDevice> selectOne(SelectDSLCompleter completer) {
        return MyBatis3Utils.selectOne(this::selectOne, selectList, dockingDevice, completer);
    }

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source Table: docking_device")
    default List<DockingDevice> select(SelectDSLCompleter completer) {
        return MyBatis3Utils.selectList(this::selectMany, selectList, dockingDevice, completer);
    }

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source Table: docking_device")
    default List<DockingDevice> selectDistinct(SelectDSLCompleter completer) {
        return MyBatis3Utils.selectDistinct(this::selectMany, selectList, dockingDevice, completer);
    }

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source Table: docking_device")
    default Optional<DockingDevice> selectByPrimaryKey(Long id_) {
        return selectOne(c ->
            c.where(id, isEqualTo(id_))
        );
    }

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source Table: docking_device")
    default int update(UpdateDSLCompleter completer) {
        return MyBatis3Utils.update(this::update, dockingDevice, completer);
    }

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source Table: docking_device")
    static UpdateDSL<UpdateModel> updateAllColumns(DockingDevice row, UpdateDSL<UpdateModel> dsl) {
        return dsl.set(id).equalTo(row::getId)
                .set(deviceId).equalTo(row::getDeviceId)
                .set(name).equalTo(row::getName)
                .set(manufacturer).equalTo(row::getManufacturer)
                .set(model).equalTo(row::getModel)
                .set(firmware).equalTo(row::getFirmware)
                .set(transport).equalTo(row::getTransport)
                .set(streamMode).equalTo(row::getStreamMode)
                .set(onLine).equalTo(row::getOnLine)
                .set(registerTime).equalTo(row::getRegisterTime)
                .set(keepaliveTime).equalTo(row::getKeepaliveTime)
                .set(ip).equalTo(row::getIp)
                .set(createTime).equalTo(row::getCreateTime)
                .set(updateTime).equalTo(row::getUpdateTime)
                .set(port).equalTo(row::getPort)
                .set(expires).equalTo(row::getExpires)
                .set(subscribeCycleForCatalog).equalTo(row::getSubscribeCycleForCatalog)
                .set(subscribeCycleForMobilePosition).equalTo(row::getSubscribeCycleForMobilePosition)
                .set(mobilePositionSubmissionInterval).equalTo(row::getMobilePositionSubmissionInterval)
                .set(subscribeCycleForAlarm).equalTo(row::getSubscribeCycleForAlarm)
                .set(hostAddress).equalTo(row::getHostAddress)
                .set(charset).equalTo(row::getCharset)
                .set(ssrcCheck).equalTo(row::getSsrcCheck)
                .set(geoCoordSys).equalTo(row::getGeoCoordSys)
                .set(mediaServerId).equalTo(row::getMediaServerId)
                .set(customName).equalTo(row::getCustomName)
                .set(sdpIp).equalTo(row::getSdpIp)
                .set(localIp).equalTo(row::getLocalIp)
                .set(password).equalTo(row::getPassword)
                .set(asMessageChannel).equalTo(row::getAsMessageChannel)
                .set(keepaliveIntervalTime).equalTo(row::getKeepaliveIntervalTime)
                .set(switchPrimarySubStream).equalTo(row::getSwitchPrimarySubStream)
                .set(broadcastPushAfterAck).equalTo(row::getBroadcastPushAfterAck);
    }

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source Table: docking_device")
    static UpdateDSL<UpdateModel> updateSelectiveColumns(DockingDevice row, UpdateDSL<UpdateModel> dsl) {
        return dsl.set(id).equalToWhenPresent(row::getId)
                .set(deviceId).equalToWhenPresent(row::getDeviceId)
                .set(name).equalToWhenPresent(row::getName)
                .set(manufacturer).equalToWhenPresent(row::getManufacturer)
                .set(model).equalToWhenPresent(row::getModel)
                .set(firmware).equalToWhenPresent(row::getFirmware)
                .set(transport).equalToWhenPresent(row::getTransport)
                .set(streamMode).equalToWhenPresent(row::getStreamMode)
                .set(onLine).equalToWhenPresent(row::getOnLine)
                .set(registerTime).equalToWhenPresent(row::getRegisterTime)
                .set(keepaliveTime).equalToWhenPresent(row::getKeepaliveTime)
                .set(ip).equalToWhenPresent(row::getIp)
                .set(createTime).equalToWhenPresent(row::getCreateTime)
                .set(updateTime).equalToWhenPresent(row::getUpdateTime)
                .set(port).equalToWhenPresent(row::getPort)
                .set(expires).equalToWhenPresent(row::getExpires)
                .set(subscribeCycleForCatalog).equalToWhenPresent(row::getSubscribeCycleForCatalog)
                .set(subscribeCycleForMobilePosition).equalToWhenPresent(row::getSubscribeCycleForMobilePosition)
                .set(mobilePositionSubmissionInterval).equalToWhenPresent(row::getMobilePositionSubmissionInterval)
                .set(subscribeCycleForAlarm).equalToWhenPresent(row::getSubscribeCycleForAlarm)
                .set(hostAddress).equalToWhenPresent(row::getHostAddress)
                .set(charset).equalToWhenPresent(row::getCharset)
                .set(ssrcCheck).equalToWhenPresent(row::getSsrcCheck)
                .set(geoCoordSys).equalToWhenPresent(row::getGeoCoordSys)
                .set(mediaServerId).equalToWhenPresent(row::getMediaServerId)
                .set(customName).equalToWhenPresent(row::getCustomName)
                .set(sdpIp).equalToWhenPresent(row::getSdpIp)
                .set(localIp).equalToWhenPresent(row::getLocalIp)
                .set(password).equalToWhenPresent(row::getPassword)
                .set(asMessageChannel).equalToWhenPresent(row::getAsMessageChannel)
                .set(keepaliveIntervalTime).equalToWhenPresent(row::getKeepaliveIntervalTime)
                .set(switchPrimarySubStream).equalToWhenPresent(row::getSwitchPrimarySubStream)
                .set(broadcastPushAfterAck).equalToWhenPresent(row::getBroadcastPushAfterAck);
    }

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source Table: docking_device")
    default int updateByPrimaryKey(DockingDevice row) {
        return update(c ->
            c.set(deviceId).equalTo(row::getDeviceId)
            .set(name).equalTo(row::getName)
            .set(manufacturer).equalTo(row::getManufacturer)
            .set(model).equalTo(row::getModel)
            .set(firmware).equalTo(row::getFirmware)
            .set(transport).equalTo(row::getTransport)
            .set(streamMode).equalTo(row::getStreamMode)
            .set(onLine).equalTo(row::getOnLine)
            .set(registerTime).equalTo(row::getRegisterTime)
            .set(keepaliveTime).equalTo(row::getKeepaliveTime)
            .set(ip).equalTo(row::getIp)
            .set(createTime).equalTo(row::getCreateTime)
            .set(updateTime).equalTo(row::getUpdateTime)
            .set(port).equalTo(row::getPort)
            .set(expires).equalTo(row::getExpires)
            .set(subscribeCycleForCatalog).equalTo(row::getSubscribeCycleForCatalog)
            .set(subscribeCycleForMobilePosition).equalTo(row::getSubscribeCycleForMobilePosition)
            .set(mobilePositionSubmissionInterval).equalTo(row::getMobilePositionSubmissionInterval)
            .set(subscribeCycleForAlarm).equalTo(row::getSubscribeCycleForAlarm)
            .set(hostAddress).equalTo(row::getHostAddress)
            .set(charset).equalTo(row::getCharset)
            .set(ssrcCheck).equalTo(row::getSsrcCheck)
            .set(geoCoordSys).equalTo(row::getGeoCoordSys)
            .set(mediaServerId).equalTo(row::getMediaServerId)
            .set(customName).equalTo(row::getCustomName)
            .set(sdpIp).equalTo(row::getSdpIp)
            .set(localIp).equalTo(row::getLocalIp)
            .set(password).equalTo(row::getPassword)
            .set(asMessageChannel).equalTo(row::getAsMessageChannel)
            .set(keepaliveIntervalTime).equalTo(row::getKeepaliveIntervalTime)
            .set(switchPrimarySubStream).equalTo(row::getSwitchPrimarySubStream)
            .set(broadcastPushAfterAck).equalTo(row::getBroadcastPushAfterAck)
            .where(id, isEqualTo(row::getId))
        );
    }

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source Table: docking_device")
    default int updateByPrimaryKeySelective(DockingDevice row) {
        return update(c ->
            c.set(deviceId).equalToWhenPresent(row::getDeviceId)
            .set(name).equalToWhenPresent(row::getName)
            .set(manufacturer).equalToWhenPresent(row::getManufacturer)
            .set(model).equalToWhenPresent(row::getModel)
            .set(firmware).equalToWhenPresent(row::getFirmware)
            .set(transport).equalToWhenPresent(row::getTransport)
            .set(streamMode).equalToWhenPresent(row::getStreamMode)
            .set(onLine).equalToWhenPresent(row::getOnLine)
            .set(registerTime).equalToWhenPresent(row::getRegisterTime)
            .set(keepaliveTime).equalToWhenPresent(row::getKeepaliveTime)
            .set(ip).equalToWhenPresent(row::getIp)
            .set(createTime).equalToWhenPresent(row::getCreateTime)
            .set(updateTime).equalToWhenPresent(row::getUpdateTime)
            .set(port).equalToWhenPresent(row::getPort)
            .set(expires).equalToWhenPresent(row::getExpires)
            .set(subscribeCycleForCatalog).equalToWhenPresent(row::getSubscribeCycleForCatalog)
            .set(subscribeCycleForMobilePosition).equalToWhenPresent(row::getSubscribeCycleForMobilePosition)
            .set(mobilePositionSubmissionInterval).equalToWhenPresent(row::getMobilePositionSubmissionInterval)
            .set(subscribeCycleForAlarm).equalToWhenPresent(row::getSubscribeCycleForAlarm)
            .set(hostAddress).equalToWhenPresent(row::getHostAddress)
            .set(charset).equalToWhenPresent(row::getCharset)
            .set(ssrcCheck).equalToWhenPresent(row::getSsrcCheck)
            .set(geoCoordSys).equalToWhenPresent(row::getGeoCoordSys)
            .set(mediaServerId).equalToWhenPresent(row::getMediaServerId)
            .set(customName).equalToWhenPresent(row::getCustomName)
            .set(sdpIp).equalToWhenPresent(row::getSdpIp)
            .set(localIp).equalToWhenPresent(row::getLocalIp)
            .set(password).equalToWhenPresent(row::getPassword)
            .set(asMessageChannel).equalToWhenPresent(row::getAsMessageChannel)
            .set(keepaliveIntervalTime).equalToWhenPresent(row::getKeepaliveIntervalTime)
            .set(switchPrimarySubStream).equalToWhenPresent(row::getSwitchPrimarySubStream)
            .set(broadcastPushAfterAck).equalToWhenPresent(row::getBroadcastPushAfterAck)
            .where(id, isEqualTo(row::getId))
        );
    }
}