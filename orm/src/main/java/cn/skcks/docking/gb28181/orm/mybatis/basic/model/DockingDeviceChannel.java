package cn.skcks.docking.gb28181.orm.mybatis.basic.model;

/**
 *
 * This class was generated by MyBatis Generator.
 * This class corresponds to the database table docking_device_channel
 */
public class DockingDeviceChannel {
    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column docking_device_channel.id
     *
     * @mbg.generated
     */
    private Long id;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column docking_device_channel.gb_device_id
     *
     * @mbg.generated
     */
    private String gbDeviceId;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column docking_device_channel.gb_device_channel_id
     *
     * @mbg.generated
     */
    private String gbDeviceChannelId;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column docking_device_channel.name
     *
     * @mbg.generated
     */
    private String name;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column docking_device_channel.address
     *
     * @mbg.generated
     */
    private String address;

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column docking_device_channel.id
     *
     * @return the value of docking_device_channel.id
     *
     * @mbg.generated
     */
    public Long getId() {
        return id;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column docking_device_channel.id
     *
     * @param id the value for docking_device_channel.id
     *
     * @mbg.generated
     */
    public void setId(Long id) {
        this.id = id;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column docking_device_channel.gb_device_id
     *
     * @return the value of docking_device_channel.gb_device_id
     *
     * @mbg.generated
     */
    public String getGbDeviceId() {
        return gbDeviceId;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column docking_device_channel.gb_device_id
     *
     * @param gbDeviceId the value for docking_device_channel.gb_device_id
     *
     * @mbg.generated
     */
    public void setGbDeviceId(String gbDeviceId) {
        this.gbDeviceId = gbDeviceId == null ? null : gbDeviceId.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column docking_device_channel.gb_device_channel_id
     *
     * @return the value of docking_device_channel.gb_device_channel_id
     *
     * @mbg.generated
     */
    public String getGbDeviceChannelId() {
        return gbDeviceChannelId;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column docking_device_channel.gb_device_channel_id
     *
     * @param gbDeviceChannelId the value for docking_device_channel.gb_device_channel_id
     *
     * @mbg.generated
     */
    public void setGbDeviceChannelId(String gbDeviceChannelId) {
        this.gbDeviceChannelId = gbDeviceChannelId == null ? null : gbDeviceChannelId.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column docking_device_channel.name
     *
     * @return the value of docking_device_channel.name
     *
     * @mbg.generated
     */
    public String getName() {
        return name;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column docking_device_channel.name
     *
     * @param name the value for docking_device_channel.name
     *
     * @mbg.generated
     */
    public void setName(String name) {
        this.name = name == null ? null : name.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column docking_device_channel.address
     *
     * @return the value of docking_device_channel.address
     *
     * @mbg.generated
     */
    public String getAddress() {
        return address;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column docking_device_channel.address
     *
     * @param address the value for docking_device_channel.address
     *
     * @mbg.generated
     */
    public void setAddress(String address) {
        this.address = address == null ? null : address.trim();
    }
}