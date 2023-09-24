package cn.skcks.docking.gb28181.sip.manscdp.catalog.query;


import cn.skcks.docking.gb28181.constant.GB28181Constant;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;


@JacksonXmlRootElement(localName = "Item")
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Data
public class CatalogItemDTO {
    /**
     * 设备/区域/系统编码(必选)
     */
    @JacksonXmlProperty(localName = "DeviceID")
    private String deviceId;

    /**
     * 设备/区域/系统名称(必选)
     */
    private String name;

    /**
     * 当为设备时,设备厂商(必选)
     */
    private String manufacturer;

    /**
     * 当为设备时,设备型号(必选)
     */
    private String model;

    /**
     * 当为设备时,设备归属(必选)
     */
    private String owner;

    /**
     * 行政区域(必选)
     */
    @JacksonXmlProperty(localName = "CivilCode")
    private String civilCode;

    /**
     * 警区(可选)
     */
    private String block;

    /**
     * 当为设备时,安装地址(必选)
     */
    private String address;

    /**
     * 当为设备时,是否有子设备(必选)1有, 0没有
     */
    @Builder.Default
    private Integer parental = 0;

    /**
     * 父设备/区域/系统ID(必选)
     */
    @JacksonXmlProperty(localName = "ParentID")
    private String parentId;

    /**
     * 信令安全模式(可选)缺省为0; 0:不采用;2:S/MIME 签名方式;3:S/ MIME加密签名同时采用方式;4:数字摘要方式
     */
    @Builder.Default
    private Integer safetyWay = 0;

    /**
     * 注册方式(必选)缺省为1;1:符合IETF RFC3261标准的认证注册模 式;2:基于口令的双向认证注册模式;3:基于数字证书的双向认证注册模式
     */
    @Builder.Default
    private Integer registerWay = 1;

    /**
     * 证书序列号(有证书的设备必选)
     */
    private String certNum;

    /**
     * 证书有效标识(有证书的设备必选)缺省为0;证书有效标识:0:无效 1: 有效
     */
    @Builder.Default
    private Integer certifiable = 0;

    /**
     * 无效原因码(有证书且证书无效的设备必选)
     */
    @Builder.Default
    private Integer errCode = 0;

    /**
     * 证书终止有效期(有证书的设备必选)
     */
    @JsonFormat(pattern = GB28181Constant.DATETIME_FORMAT, timezone = GB28181Constant.TIME_ZONE)
    private Date endTime;

    /**
     * 保密属性(必选)缺省为0;0:不涉密,1:涉密
     */
    @Builder.Default
    private Integer secrecy = 0;

    /**
     * 设备/区域/系统IP地址(可选)
     */
    @JacksonXmlProperty(localName = "IPAddress")
    private String ipAddress;

    /**
     * 设备/区域/系统端口(可选)
     */
    private Integer port;

    /**
     * 设备口令(可选)
     */
    private String password;

    /**
     * 设备状态(必选)
     */
    @Builder.Default
    private String status = "ON";

    /**
     * 经度(可选)
     */
    @Builder.Default
    private String longitude = "0.0";

    /**
     * 纬度(可选)
     */
    @Builder.Default
    private String latitude = "0.0";
}
