package cn.skcks.docking.gb28181.sip.manscdp.recordinfo.request;

import cn.skcks.docking.gb28181.constant.CmdType;
import cn.skcks.docking.gb28181.constant.GB28181Constant;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@JacksonXmlRootElement(localName = "Query")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class RecordInfoRequestDTO {
    /**
     * 命令类型:设备录像查询(必选)
     */
    @Builder.Default
    private String cmdType = CmdType.RECORD_INFO;

    /**
     * 命令序列号(必选)
     */
    @JacksonXmlProperty(localName = "SN")
    private String sn;

    /**
     * 目标设备的设备编码(必选)
     */
    @JacksonXmlProperty(localName = "DeviceID")
    private String deviceId;

    @JsonFormat(pattern = GB28181Constant.DATETIME_FORMAT, timezone = GB28181Constant.TIME_ZONE)
    private Date startTime;

    @JsonFormat(pattern = GB28181Constant.DATETIME_FORMAT, timezone = GB28181Constant.TIME_ZONE)
    private Date endTime;

    private String filePath;

    private String address;

    @Builder.Default
    private Integer Secrecy = 0;

    @Builder.Default
    private String type = "all";

    @JacksonXmlProperty(localName = "RecorderID")
    private String recorderId;

    /**
     * 录像模糊查询属性(可选) 缺省为 0; <p/>
     * 0: 不进行模糊查询,此时根据SIP消息中To头域
     * URI 中的ID值确定查询录像位置,若 ID 值为本域系统 ID 则进行中心历史记录检索
     * 若为前端设备 ID 则进行前端设备历史记录检索; <p/>
     *
     * 1: 进行模糊查询,此时设备所在域应同时进行中心检索和前端检索并将结果统一返回
     */
    private Integer IndistinctQuery;
}
