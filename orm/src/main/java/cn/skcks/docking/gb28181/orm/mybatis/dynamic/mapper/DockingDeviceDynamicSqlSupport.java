package cn.skcks.docking.gb28181.orm.mybatis.dynamic.mapper;

import jakarta.annotation.Generated;
import java.sql.JDBCType;
import org.mybatis.dynamic.sql.AliasableSqlTable;
import org.mybatis.dynamic.sql.SqlColumn;

public final class DockingDeviceDynamicSqlSupport {
    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source Table: docking_device")
    public static final DockingDevice dockingDevice = new DockingDevice();

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source field: docking_device.id")
    public static final SqlColumn<Long> id = dockingDevice.id;

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source field: docking_device.device_id")
    public static final SqlColumn<String> deviceId = dockingDevice.deviceId;

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source field: docking_device.name")
    public static final SqlColumn<String> name = dockingDevice.name;

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source field: docking_device.manufacturer")
    public static final SqlColumn<String> manufacturer = dockingDevice.manufacturer;

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source field: docking_device.model")
    public static final SqlColumn<String> model = dockingDevice.model;

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source field: docking_device.firmware")
    public static final SqlColumn<String> firmware = dockingDevice.firmware;

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source field: docking_device.transport")
    public static final SqlColumn<String> transport = dockingDevice.transport;

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source field: docking_device.stream_mode")
    public static final SqlColumn<String> streamMode = dockingDevice.streamMode;

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source field: docking_device.on_line")
    public static final SqlColumn<Boolean> onLine = dockingDevice.onLine;

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source field: docking_device.register_time")
    public static final SqlColumn<String> registerTime = dockingDevice.registerTime;

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source field: docking_device.keepalive_time")
    public static final SqlColumn<String> keepaliveTime = dockingDevice.keepaliveTime;

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source field: docking_device.ip")
    public static final SqlColumn<String> ip = dockingDevice.ip;

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source field: docking_device.create_time")
    public static final SqlColumn<String> createTime = dockingDevice.createTime;

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source field: docking_device.update_time")
    public static final SqlColumn<String> updateTime = dockingDevice.updateTime;

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source field: docking_device.port")
    public static final SqlColumn<Integer> port = dockingDevice.port;

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source field: docking_device.expires")
    public static final SqlColumn<Integer> expires = dockingDevice.expires;

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source field: docking_device.subscribe_cycle_for_catalog")
    public static final SqlColumn<Integer> subscribeCycleForCatalog = dockingDevice.subscribeCycleForCatalog;

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source field: docking_device.subscribe_cycle_for_mobile_position")
    public static final SqlColumn<Integer> subscribeCycleForMobilePosition = dockingDevice.subscribeCycleForMobilePosition;

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source field: docking_device.mobile_position_submission_interval")
    public static final SqlColumn<Integer> mobilePositionSubmissionInterval = dockingDevice.mobilePositionSubmissionInterval;

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source field: docking_device.subscribe_cycle_for_alarm")
    public static final SqlColumn<Integer> subscribeCycleForAlarm = dockingDevice.subscribeCycleForAlarm;

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source field: docking_device.host_address")
    public static final SqlColumn<String> hostAddress = dockingDevice.hostAddress;

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source field: docking_device.charset")
    public static final SqlColumn<String> charset = dockingDevice.charset;

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source field: docking_device.ssrc_check")
    public static final SqlColumn<Boolean> ssrcCheck = dockingDevice.ssrcCheck;

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source field: docking_device.geo_coord_sys")
    public static final SqlColumn<String> geoCoordSys = dockingDevice.geoCoordSys;

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source field: docking_device.media_server_id")
    public static final SqlColumn<String> mediaServerId = dockingDevice.mediaServerId;

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source field: docking_device.custom_name")
    public static final SqlColumn<String> customName = dockingDevice.customName;

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source field: docking_device.sdp_ip")
    public static final SqlColumn<String> sdpIp = dockingDevice.sdpIp;

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source field: docking_device.local_ip")
    public static final SqlColumn<String> localIp = dockingDevice.localIp;

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source field: docking_device.password")
    public static final SqlColumn<String> password = dockingDevice.password;

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source field: docking_device.as_message_channel")
    public static final SqlColumn<Boolean> asMessageChannel = dockingDevice.asMessageChannel;

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source field: docking_device.keepalive_interval_time")
    public static final SqlColumn<Integer> keepaliveIntervalTime = dockingDevice.keepaliveIntervalTime;

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source field: docking_device.switch_primary_sub_stream")
    public static final SqlColumn<Boolean> switchPrimarySubStream = dockingDevice.switchPrimarySubStream;

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source field: docking_device.broadcast_push_after_ack")
    public static final SqlColumn<Boolean> broadcastPushAfterAck = dockingDevice.broadcastPushAfterAck;

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source Table: docking_device")
    public static final class DockingDevice extends AliasableSqlTable<DockingDevice> {
        public final SqlColumn<Long> id = column("id", JDBCType.BIGINT);

        public final SqlColumn<String> deviceId = column("device_id", JDBCType.VARCHAR);

        public final SqlColumn<String> name = column("name", JDBCType.VARCHAR);

        public final SqlColumn<String> manufacturer = column("manufacturer", JDBCType.VARCHAR);

        public final SqlColumn<String> model = column("model", JDBCType.VARCHAR);

        public final SqlColumn<String> firmware = column("firmware", JDBCType.VARCHAR);

        public final SqlColumn<String> transport = column("transport", JDBCType.VARCHAR);

        public final SqlColumn<String> streamMode = column("stream_mode", JDBCType.VARCHAR);

        public final SqlColumn<Boolean> onLine = column("on_line", JDBCType.BIT);

        public final SqlColumn<String> registerTime = column("register_time", JDBCType.VARCHAR);

        public final SqlColumn<String> keepaliveTime = column("keepalive_time", JDBCType.VARCHAR);

        public final SqlColumn<String> ip = column("ip", JDBCType.VARCHAR);

        public final SqlColumn<String> createTime = column("create_time", JDBCType.VARCHAR);

        public final SqlColumn<String> updateTime = column("update_time", JDBCType.VARCHAR);

        public final SqlColumn<Integer> port = column("port", JDBCType.INTEGER);

        public final SqlColumn<Integer> expires = column("expires", JDBCType.INTEGER);

        public final SqlColumn<Integer> subscribeCycleForCatalog = column("subscribe_cycle_for_catalog", JDBCType.INTEGER);

        public final SqlColumn<Integer> subscribeCycleForMobilePosition = column("subscribe_cycle_for_mobile_position", JDBCType.INTEGER);

        public final SqlColumn<Integer> mobilePositionSubmissionInterval = column("mobile_position_submission_interval", JDBCType.INTEGER);

        public final SqlColumn<Integer> subscribeCycleForAlarm = column("subscribe_cycle_for_alarm", JDBCType.INTEGER);

        public final SqlColumn<String> hostAddress = column("host_address", JDBCType.VARCHAR);

        public final SqlColumn<String> charset = column("charset", JDBCType.VARCHAR);

        public final SqlColumn<Boolean> ssrcCheck = column("ssrc_check", JDBCType.BIT);

        public final SqlColumn<String> geoCoordSys = column("geo_coord_sys", JDBCType.VARCHAR);

        public final SqlColumn<String> mediaServerId = column("media_server_id", JDBCType.VARCHAR);

        public final SqlColumn<String> customName = column("custom_name", JDBCType.VARCHAR);

        public final SqlColumn<String> sdpIp = column("sdp_ip", JDBCType.VARCHAR);

        public final SqlColumn<String> localIp = column("local_ip", JDBCType.VARCHAR);

        public final SqlColumn<String> password = column("password", JDBCType.VARCHAR);

        public final SqlColumn<Boolean> asMessageChannel = column("as_message_channel", JDBCType.BIT);

        public final SqlColumn<Integer> keepaliveIntervalTime = column("keepalive_interval_time", JDBCType.INTEGER);

        public final SqlColumn<Boolean> switchPrimarySubStream = column("switch_primary_sub_stream", JDBCType.BIT);

        public final SqlColumn<Boolean> broadcastPushAfterAck = column("broadcast_push_after_ack", JDBCType.BIT);

        public DockingDevice() {
            super("docking_device", DockingDevice::new);
        }
    }
}