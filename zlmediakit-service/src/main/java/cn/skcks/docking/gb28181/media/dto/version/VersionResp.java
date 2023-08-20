package cn.skcks.docking.gb28181.media.dto.version;

import cn.hutool.core.date.DatePattern;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.util.Date;

@Data
public class VersionResp {
    private String branchName;
    @JsonFormat(pattern = DatePattern.UTC_SIMPLE_PATTERN)
    private Date buildTime;
    private String commitHash;
}
