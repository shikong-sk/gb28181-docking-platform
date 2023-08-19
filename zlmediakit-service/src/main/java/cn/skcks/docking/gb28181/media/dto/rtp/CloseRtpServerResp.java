package cn.skcks.docking.gb28181.media.dto.rtp;

import cn.skcks.docking.gb28181.media.dto.status.ResponseStatus;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.Data;

@Data
public class CloseRtpServerResp {
    private ResponseStatus code;
    private Integer hit;
}
