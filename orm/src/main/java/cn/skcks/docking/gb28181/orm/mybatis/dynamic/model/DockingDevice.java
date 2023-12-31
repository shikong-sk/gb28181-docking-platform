package cn.skcks.docking.gb28181.orm.mybatis.dynamic.model;

import jakarta.annotation.Generated;

/**
 *
 * This class was generated by MyBatis Generator.
 * This class corresponds to the database table docking_device
 */
public class DockingDevice {
    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source field: docking_device.id")
    private Long id;

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source field: docking_device.device_id")
    private String deviceId;

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source field: docking_device.name")
    private String name;

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source field: docking_device.manufacturer")
    private String manufacturer;

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source field: docking_device.model")
    private String model;

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source field: docking_device.firmware")
    private String firmware;

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source field: docking_device.transport")
    private String transport;

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source field: docking_device.stream_mode")
    private String streamMode;

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source field: docking_device.on_line")
    private Boolean onLine;

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source field: docking_device.register_time")
    private String registerTime;

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source field: docking_device.keepalive_time")
    private String keepaliveTime;

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source field: docking_device.ip")
    private String ip;

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source field: docking_device.create_time")
    private String createTime;

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source field: docking_device.update_time")
    private String updateTime;

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source field: docking_device.port")
    private Integer port;

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source field: docking_device.expires")
    private Integer expires;

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source field: docking_device.subscribe_cycle_for_catalog")
    private Integer subscribeCycleForCatalog;

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source field: docking_device.subscribe_cycle_for_mobile_position")
    private Integer subscribeCycleForMobilePosition;

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source field: docking_device.mobile_position_submission_interval")
    private Integer mobilePositionSubmissionInterval;

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source field: docking_device.subscribe_cycle_for_alarm")
    private Integer subscribeCycleForAlarm;

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source field: docking_device.host_address")
    private String hostAddress;

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source field: docking_device.charset")
    private String charset;

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source field: docking_device.ssrc_check")
    private Boolean ssrcCheck;

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source field: docking_device.geo_coord_sys")
    private String geoCoordSys;

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source field: docking_device.media_server_id")
    private String mediaServerId;

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source field: docking_device.custom_name")
    private String customName;

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source field: docking_device.sdp_ip")
    private String sdpIp;

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source field: docking_device.local_ip")
    private String localIp;

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source field: docking_device.password")
    private String password;

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source field: docking_device.as_message_channel")
    private Boolean asMessageChannel;

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source field: docking_device.keepalive_interval_time")
    private Integer keepaliveIntervalTime;

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source field: docking_device.switch_primary_sub_stream")
    private Boolean switchPrimarySubStream;

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source field: docking_device.broadcast_push_after_ack")
    private Boolean broadcastPushAfterAck;

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source field: docking_device.id")
    public Long getId() {
        return id;
    }

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source field: docking_device.id")
    public void setId(Long id) {
        this.id = id;
    }

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source field: docking_device.device_id")
    public String getDeviceId() {
        return deviceId;
    }

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source field: docking_device.device_id")
    public void setDeviceId(String deviceId) {
        this.deviceId = deviceId == null ? null : deviceId.trim();
    }

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source field: docking_device.name")
    public String getName() {
        return name;
    }

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source field: docking_device.name")
    public void setName(String name) {
        this.name = name == null ? null : name.trim();
    }

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source field: docking_device.manufacturer")
    public String getManufacturer() {
        return manufacturer;
    }

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source field: docking_device.manufacturer")
    public void setManufacturer(String manufacturer) {
        this.manufacturer = manufacturer == null ? null : manufacturer.trim();
    }

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source field: docking_device.model")
    public String getModel() {
        return model;
    }

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source field: docking_device.model")
    public void setModel(String model) {
        this.model = model == null ? null : model.trim();
    }

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source field: docking_device.firmware")
    public String getFirmware() {
        return firmware;
    }

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source field: docking_device.firmware")
    public void setFirmware(String firmware) {
        this.firmware = firmware == null ? null : firmware.trim();
    }

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source field: docking_device.transport")
    public String getTransport() {
        return transport;
    }

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source field: docking_device.transport")
    public void setTransport(String transport) {
        this.transport = transport == null ? null : transport.trim();
    }

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source field: docking_device.stream_mode")
    public String getStreamMode() {
        return streamMode;
    }

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source field: docking_device.stream_mode")
    public void setStreamMode(String streamMode) {
        this.streamMode = streamMode == null ? null : streamMode.trim();
    }

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source field: docking_device.on_line")
    public Boolean getOnLine() {
        return onLine;
    }

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source field: docking_device.on_line")
    public void setOnLine(Boolean onLine) {
        this.onLine = onLine;
    }

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source field: docking_device.register_time")
    public String getRegisterTime() {
        return registerTime;
    }

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source field: docking_device.register_time")
    public void setRegisterTime(String registerTime) {
        this.registerTime = registerTime == null ? null : registerTime.trim();
    }

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source field: docking_device.keepalive_time")
    public String getKeepaliveTime() {
        return keepaliveTime;
    }

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source field: docking_device.keepalive_time")
    public void setKeepaliveTime(String keepaliveTime) {
        this.keepaliveTime = keepaliveTime == null ? null : keepaliveTime.trim();
    }

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source field: docking_device.ip")
    public String getIp() {
        return ip;
    }

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source field: docking_device.ip")
    public void setIp(String ip) {
        this.ip = ip == null ? null : ip.trim();
    }

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source field: docking_device.create_time")
    public String getCreateTime() {
        return createTime;
    }

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source field: docking_device.create_time")
    public void setCreateTime(String createTime) {
        this.createTime = createTime == null ? null : createTime.trim();
    }

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source field: docking_device.update_time")
    public String getUpdateTime() {
        return updateTime;
    }

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source field: docking_device.update_time")
    public void setUpdateTime(String updateTime) {
        this.updateTime = updateTime == null ? null : updateTime.trim();
    }

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source field: docking_device.port")
    public Integer getPort() {
        return port;
    }

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source field: docking_device.port")
    public void setPort(Integer port) {
        this.port = port;
    }

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source field: docking_device.expires")
    public Integer getExpires() {
        return expires;
    }

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source field: docking_device.expires")
    public void setExpires(Integer expires) {
        this.expires = expires;
    }

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source field: docking_device.subscribe_cycle_for_catalog")
    public Integer getSubscribeCycleForCatalog() {
        return subscribeCycleForCatalog;
    }

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source field: docking_device.subscribe_cycle_for_catalog")
    public void setSubscribeCycleForCatalog(Integer subscribeCycleForCatalog) {
        this.subscribeCycleForCatalog = subscribeCycleForCatalog;
    }

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source field: docking_device.subscribe_cycle_for_mobile_position")
    public Integer getSubscribeCycleForMobilePosition() {
        return subscribeCycleForMobilePosition;
    }

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source field: docking_device.subscribe_cycle_for_mobile_position")
    public void setSubscribeCycleForMobilePosition(Integer subscribeCycleForMobilePosition) {
        this.subscribeCycleForMobilePosition = subscribeCycleForMobilePosition;
    }

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source field: docking_device.mobile_position_submission_interval")
    public Integer getMobilePositionSubmissionInterval() {
        return mobilePositionSubmissionInterval;
    }

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source field: docking_device.mobile_position_submission_interval")
    public void setMobilePositionSubmissionInterval(Integer mobilePositionSubmissionInterval) {
        this.mobilePositionSubmissionInterval = mobilePositionSubmissionInterval;
    }

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source field: docking_device.subscribe_cycle_for_alarm")
    public Integer getSubscribeCycleForAlarm() {
        return subscribeCycleForAlarm;
    }

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source field: docking_device.subscribe_cycle_for_alarm")
    public void setSubscribeCycleForAlarm(Integer subscribeCycleForAlarm) {
        this.subscribeCycleForAlarm = subscribeCycleForAlarm;
    }

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source field: docking_device.host_address")
    public String getHostAddress() {
        return hostAddress;
    }

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source field: docking_device.host_address")
    public void setHostAddress(String hostAddress) {
        this.hostAddress = hostAddress == null ? null : hostAddress.trim();
    }

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source field: docking_device.charset")
    public String getCharset() {
        return charset;
    }

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source field: docking_device.charset")
    public void setCharset(String charset) {
        this.charset = charset == null ? null : charset.trim();
    }

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source field: docking_device.ssrc_check")
    public Boolean getSsrcCheck() {
        return ssrcCheck;
    }

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source field: docking_device.ssrc_check")
    public void setSsrcCheck(Boolean ssrcCheck) {
        this.ssrcCheck = ssrcCheck;
    }

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source field: docking_device.geo_coord_sys")
    public String getGeoCoordSys() {
        return geoCoordSys;
    }

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source field: docking_device.geo_coord_sys")
    public void setGeoCoordSys(String geoCoordSys) {
        this.geoCoordSys = geoCoordSys == null ? null : geoCoordSys.trim();
    }

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source field: docking_device.media_server_id")
    public String getMediaServerId() {
        return mediaServerId;
    }

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source field: docking_device.media_server_id")
    public void setMediaServerId(String mediaServerId) {
        this.mediaServerId = mediaServerId == null ? null : mediaServerId.trim();
    }

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source field: docking_device.custom_name")
    public String getCustomName() {
        return customName;
    }

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source field: docking_device.custom_name")
    public void setCustomName(String customName) {
        this.customName = customName == null ? null : customName.trim();
    }

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source field: docking_device.sdp_ip")
    public String getSdpIp() {
        return sdpIp;
    }

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source field: docking_device.sdp_ip")
    public void setSdpIp(String sdpIp) {
        this.sdpIp = sdpIp == null ? null : sdpIp.trim();
    }

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source field: docking_device.local_ip")
    public String getLocalIp() {
        return localIp;
    }

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source field: docking_device.local_ip")
    public void setLocalIp(String localIp) {
        this.localIp = localIp == null ? null : localIp.trim();
    }

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source field: docking_device.password")
    public String getPassword() {
        return password;
    }

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source field: docking_device.password")
    public void setPassword(String password) {
        this.password = password == null ? null : password.trim();
    }

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source field: docking_device.as_message_channel")
    public Boolean getAsMessageChannel() {
        return asMessageChannel;
    }

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source field: docking_device.as_message_channel")
    public void setAsMessageChannel(Boolean asMessageChannel) {
        this.asMessageChannel = asMessageChannel;
    }

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source field: docking_device.keepalive_interval_time")
    public Integer getKeepaliveIntervalTime() {
        return keepaliveIntervalTime;
    }

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source field: docking_device.keepalive_interval_time")
    public void setKeepaliveIntervalTime(Integer keepaliveIntervalTime) {
        this.keepaliveIntervalTime = keepaliveIntervalTime;
    }

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source field: docking_device.switch_primary_sub_stream")
    public Boolean getSwitchPrimarySubStream() {
        return switchPrimarySubStream;
    }

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source field: docking_device.switch_primary_sub_stream")
    public void setSwitchPrimarySubStream(Boolean switchPrimarySubStream) {
        this.switchPrimarySubStream = switchPrimarySubStream;
    }

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source field: docking_device.broadcast_push_after_ack")
    public Boolean getBroadcastPushAfterAck() {
        return broadcastPushAfterAck;
    }

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source field: docking_device.broadcast_push_after_ack")
    public void setBroadcastPushAfterAck(Boolean broadcastPushAfterAck) {
        this.broadcastPushAfterAck = broadcastPushAfterAck;
    }
}