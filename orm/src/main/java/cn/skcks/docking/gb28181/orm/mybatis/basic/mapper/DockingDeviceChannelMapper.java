package cn.skcks.docking.gb28181.orm.mybatis.basic.mapper;

import cn.skcks.docking.gb28181.orm.mybatis.basic.model.DockingDeviceChannel;
import java.util.List;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Result;
import org.apache.ibatis.annotations.Results;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;
import org.apache.ibatis.type.JdbcType;

public interface DockingDeviceChannelMapper {
    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table docking_device_channel
     *
     * @mbg.generated
     */
    @Delete({
        "delete from docking_device_channel",
        "where id = #{id,jdbcType=BIGINT}"
    })
    int deleteByPrimaryKey(Long id);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table docking_device_channel
     *
     * @mbg.generated
     */
    @Insert({
        "insert into docking_device_channel (id, gb_device_id, ",
        "gb_device_channel_id, name, ",
        "address)",
        "values (#{id,jdbcType=BIGINT}, #{gbDeviceId,jdbcType=VARCHAR}, ",
        "#{gbDeviceChannelId,jdbcType=VARCHAR}, #{name,jdbcType=VARCHAR}, ",
        "#{address,jdbcType=VARCHAR})"
    })
    int insert(DockingDeviceChannel row);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table docking_device_channel
     *
     * @mbg.generated
     */
    @Select({
        "select",
        "id, gb_device_id, gb_device_channel_id, name, address",
        "from docking_device_channel",
        "where id = #{id,jdbcType=BIGINT}"
    })
    @Results({
        @Result(column="id", property="id", jdbcType=JdbcType.BIGINT, id=true),
        @Result(column="gb_device_id", property="gbDeviceId", jdbcType=JdbcType.VARCHAR),
        @Result(column="gb_device_channel_id", property="gbDeviceChannelId", jdbcType=JdbcType.VARCHAR),
        @Result(column="name", property="name", jdbcType=JdbcType.VARCHAR),
        @Result(column="address", property="address", jdbcType=JdbcType.VARCHAR)
    })
    DockingDeviceChannel selectByPrimaryKey(Long id);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table docking_device_channel
     *
     * @mbg.generated
     */
    @Select({
        "select",
        "id, gb_device_id, gb_device_channel_id, name, address",
        "from docking_device_channel"
    })
    @Results({
        @Result(column="id", property="id", jdbcType=JdbcType.BIGINT, id=true),
        @Result(column="gb_device_id", property="gbDeviceId", jdbcType=JdbcType.VARCHAR),
        @Result(column="gb_device_channel_id", property="gbDeviceChannelId", jdbcType=JdbcType.VARCHAR),
        @Result(column="name", property="name", jdbcType=JdbcType.VARCHAR),
        @Result(column="address", property="address", jdbcType=JdbcType.VARCHAR)
    })
    List<DockingDeviceChannel> selectAll();

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table docking_device_channel
     *
     * @mbg.generated
     */
    @Update({
        "update docking_device_channel",
        "set gb_device_id = #{gbDeviceId,jdbcType=VARCHAR},",
          "gb_device_channel_id = #{gbDeviceChannelId,jdbcType=VARCHAR},",
          "name = #{name,jdbcType=VARCHAR},",
          "address = #{address,jdbcType=VARCHAR}",
        "where id = #{id,jdbcType=BIGINT}"
    })
    int updateByPrimaryKey(DockingDeviceChannel row);
}