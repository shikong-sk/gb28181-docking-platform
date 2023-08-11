package cn.skcks.docking.gb28181.orm.mybatis.basic.mapper;

import cn.skcks.docking.gb28181.orm.mybatis.basic.model.DockingDevice;
import java.util.List;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Result;
import org.apache.ibatis.annotations.Results;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;
import org.apache.ibatis.type.JdbcType;

public interface DockingDeviceMapper {
    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table docking_device
     *
     * @mbg.generated
     */
    @Delete({
        "delete from docking_device",
        "where id = #{id,jdbcType=BIGINT}"
    })
    int deleteByPrimaryKey(Long id);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table docking_device
     *
     * @mbg.generated
     */
    @Insert({
        "insert into docking_device (id, device_id, ",
        "name, manufacturer, ",
        "model, firmware, ",
        "transport, stream_mode, ",
        "on_line, register_time, ",
        "keepalive_time, ip, ",
        "create_time, update_time, ",
        "port, expires, subscribe_cycle_for_catalog, ",
        "subscribe_cycle_for_mobile_position, mobile_position_submission_interval, ",
        "subscribe_cycle_for_alarm, host_address, ",
        "charset, ssrc_check, ",
        "geo_coord_sys, media_server_id, ",
        "custom_name, sdp_ip, ",
        "local_ip, password, ",
        "as_message_channel, keepalive_interval_time, ",
        "switch_primary_sub_stream, broadcast_push_after_ack)",
        "values (#{id,jdbcType=BIGINT}, #{deviceId,jdbcType=VARCHAR}, ",
        "#{name,jdbcType=VARCHAR}, #{manufacturer,jdbcType=VARCHAR}, ",
        "#{model,jdbcType=VARCHAR}, #{firmware,jdbcType=VARCHAR}, ",
        "#{transport,jdbcType=VARCHAR}, #{streamMode,jdbcType=VARCHAR}, ",
        "#{onLine,jdbcType=BIT}, #{registerTime,jdbcType=VARCHAR}, ",
        "#{keepaliveTime,jdbcType=VARCHAR}, #{ip,jdbcType=VARCHAR}, ",
        "#{createTime,jdbcType=VARCHAR}, #{updateTime,jdbcType=VARCHAR}, ",
        "#{port,jdbcType=INTEGER}, #{expires,jdbcType=INTEGER}, #{subscribeCycleForCatalog,jdbcType=INTEGER}, ",
        "#{subscribeCycleForMobilePosition,jdbcType=INTEGER}, #{mobilePositionSubmissionInterval,jdbcType=INTEGER}, ",
        "#{subscribeCycleForAlarm,jdbcType=INTEGER}, #{hostAddress,jdbcType=VARCHAR}, ",
        "#{charset,jdbcType=VARCHAR}, #{ssrcCheck,jdbcType=BIT}, ",
        "#{geoCoordSys,jdbcType=VARCHAR}, #{mediaServerId,jdbcType=VARCHAR}, ",
        "#{customName,jdbcType=VARCHAR}, #{sdpIp,jdbcType=VARCHAR}, ",
        "#{localIp,jdbcType=VARCHAR}, #{password,jdbcType=VARCHAR}, ",
        "#{asMessageChannel,jdbcType=BIT}, #{keepaliveIntervalTime,jdbcType=INTEGER}, ",
        "#{switchPrimarySubStream,jdbcType=BIT}, #{broadcastPushAfterAck,jdbcType=BIT})"
    })
    int insert(DockingDevice row);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table docking_device
     *
     * @mbg.generated
     */
    @Select({
        "select",
        "id, device_id, name, manufacturer, model, firmware, transport, stream_mode, ",
        "on_line, register_time, keepalive_time, ip, create_time, update_time, port, ",
        "expires, subscribe_cycle_for_catalog, subscribe_cycle_for_mobile_position, mobile_position_submission_interval, ",
        "subscribe_cycle_for_alarm, host_address, charset, ssrc_check, geo_coord_sys, ",
        "media_server_id, custom_name, sdp_ip, local_ip, password, as_message_channel, ",
        "keepalive_interval_time, switch_primary_sub_stream, broadcast_push_after_ack",
        "from docking_device",
        "where id = #{id,jdbcType=BIGINT}"
    })
    @Results({
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
    DockingDevice selectByPrimaryKey(Long id);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table docking_device
     *
     * @mbg.generated
     */
    @Select({
        "select",
        "id, device_id, name, manufacturer, model, firmware, transport, stream_mode, ",
        "on_line, register_time, keepalive_time, ip, create_time, update_time, port, ",
        "expires, subscribe_cycle_for_catalog, subscribe_cycle_for_mobile_position, mobile_position_submission_interval, ",
        "subscribe_cycle_for_alarm, host_address, charset, ssrc_check, geo_coord_sys, ",
        "media_server_id, custom_name, sdp_ip, local_ip, password, as_message_channel, ",
        "keepalive_interval_time, switch_primary_sub_stream, broadcast_push_after_ack",
        "from docking_device"
    })
    @Results({
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
    List<DockingDevice> selectAll();

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table docking_device
     *
     * @mbg.generated
     */
    @Update({
        "update docking_device",
        "set device_id = #{deviceId,jdbcType=VARCHAR},",
          "name = #{name,jdbcType=VARCHAR},",
          "manufacturer = #{manufacturer,jdbcType=VARCHAR},",
          "model = #{model,jdbcType=VARCHAR},",
          "firmware = #{firmware,jdbcType=VARCHAR},",
          "transport = #{transport,jdbcType=VARCHAR},",
          "stream_mode = #{streamMode,jdbcType=VARCHAR},",
          "on_line = #{onLine,jdbcType=BIT},",
          "register_time = #{registerTime,jdbcType=VARCHAR},",
          "keepalive_time = #{keepaliveTime,jdbcType=VARCHAR},",
          "ip = #{ip,jdbcType=VARCHAR},",
          "create_time = #{createTime,jdbcType=VARCHAR},",
          "update_time = #{updateTime,jdbcType=VARCHAR},",
          "port = #{port,jdbcType=INTEGER},",
          "expires = #{expires,jdbcType=INTEGER},",
          "subscribe_cycle_for_catalog = #{subscribeCycleForCatalog,jdbcType=INTEGER},",
          "subscribe_cycle_for_mobile_position = #{subscribeCycleForMobilePosition,jdbcType=INTEGER},",
          "mobile_position_submission_interval = #{mobilePositionSubmissionInterval,jdbcType=INTEGER},",
          "subscribe_cycle_for_alarm = #{subscribeCycleForAlarm,jdbcType=INTEGER},",
          "host_address = #{hostAddress,jdbcType=VARCHAR},",
          "charset = #{charset,jdbcType=VARCHAR},",
          "ssrc_check = #{ssrcCheck,jdbcType=BIT},",
          "geo_coord_sys = #{geoCoordSys,jdbcType=VARCHAR},",
          "media_server_id = #{mediaServerId,jdbcType=VARCHAR},",
          "custom_name = #{customName,jdbcType=VARCHAR},",
          "sdp_ip = #{sdpIp,jdbcType=VARCHAR},",
          "local_ip = #{localIp,jdbcType=VARCHAR},",
          "password = #{password,jdbcType=VARCHAR},",
          "as_message_channel = #{asMessageChannel,jdbcType=BIT},",
          "keepalive_interval_time = #{keepaliveIntervalTime,jdbcType=INTEGER},",
          "switch_primary_sub_stream = #{switchPrimarySubStream,jdbcType=BIT},",
          "broadcast_push_after_ack = #{broadcastPushAfterAck,jdbcType=BIT}",
        "where id = #{id,jdbcType=BIGINT}"
    })
    int updateByPrimaryKey(DockingDevice row);
}