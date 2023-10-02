package cn.skcks.docking.gb28181.orm.mybatis.dynamic.mapper;

import jakarta.annotation.Generated;
import java.sql.JDBCType;
import org.mybatis.dynamic.sql.AliasableSqlTable;
import org.mybatis.dynamic.sql.SqlColumn;

public final class DockingDeviceChannelDynamicSqlSupport {
    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source Table: docking_device_channel")
    public static final DockingDeviceChannel dockingDeviceChannel = new DockingDeviceChannel();

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source field: docking_device_channel.id")
    public static final SqlColumn<Long> id = dockingDeviceChannel.id;

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source field: docking_device_channel.gb_device_id")
    public static final SqlColumn<String> gbDeviceId = dockingDeviceChannel.gbDeviceId;

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source field: docking_device_channel.gb_device_channel_id")
    public static final SqlColumn<String> gbDeviceChannelId = dockingDeviceChannel.gbDeviceChannelId;

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source field: docking_device_channel.name")
    public static final SqlColumn<String> name = dockingDeviceChannel.name;

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source field: docking_device_channel.address")
    public static final SqlColumn<String> address = dockingDeviceChannel.address;

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="Source Table: docking_device_channel")
    public static final class DockingDeviceChannel extends AliasableSqlTable<DockingDeviceChannel> {
        public final SqlColumn<Long> id = column("id", JDBCType.BIGINT);

        public final SqlColumn<String> gbDeviceId = column("gb_device_id", JDBCType.VARCHAR);

        public final SqlColumn<String> gbDeviceChannelId = column("gb_device_channel_id", JDBCType.VARCHAR);

        public final SqlColumn<String> name = column("name", JDBCType.VARCHAR);

        public final SqlColumn<String> address = column("address", JDBCType.VARCHAR);

        public DockingDeviceChannel() {
            super("docking_device_channel", DockingDeviceChannel::new);
        }
    }
}