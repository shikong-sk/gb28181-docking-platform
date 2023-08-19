package cn.skcks.docking.gb28181.media.dto.rtp;

import cn.skcks.docking.gb28181.media.dto.status.ResponseStatus;
import lombok.Data;

@Data
public class OpenRtpServerResp {
    private ResponseStatus code;
    private Integer port;
}
