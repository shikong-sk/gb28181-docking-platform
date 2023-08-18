package cn.skcks.docking.gb28181.media.dto.config;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.Data;

@Data
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class ProtocolConfig {
    private Integer addMuteAudio;
    private Integer autoClose;
    private Integer continuePushMs;
    private Integer enableAudio;
    private Integer enableFmp4;
    private Integer enableHls;
    private Integer enableHlsFmp4;
    private Integer enableMp4;
    private Integer enableRtmp;
    private Integer enableRtsp;
    private Integer enableTs;
    private Integer fmp4Demand;
    private Integer hlsDemand;
    private String hlsSavePath;
    private Integer modifyStamp;
    private Integer mp4AsPlayer;
    private Integer mp4MaxSecond;
    private String mp4SavePath;
    private Integer rtmpDemand;
    private Integer rtspDemand;
    private Integer tsDemand;
}
