package cn.skcks.docking.gb28181.media.dto.config;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.Data;

@Data
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class FFMpegConfig {
    private String bin;
    private String cmd;
    private String log;
    private Integer restartSec;
    private String snap;
}
