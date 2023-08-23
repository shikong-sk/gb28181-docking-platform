package cn.skcks.docking.gb28181.service.record.vo;

import cn.hutool.core.date.DatePattern;
import cn.skcks.docking.gb28181.core.sip.gb28181.constant.GB28181Constant;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.util.Date;

@Data
public class RecordInfoItemVO {
    private String deviceId;

    private String name;

    private String address;

    @JsonFormat(pattern = DatePattern.NORM_DATETIME_PATTERN, timezone = GB28181Constant.TIME_ZONE)
    private Date startTime;

    @JsonFormat(pattern = DatePattern.NORM_DATETIME_PATTERN, timezone = GB28181Constant.TIME_ZONE)
    private Date endTime;

    private Integer secrecy;

    private String type;

    private Long fileSize;
}
