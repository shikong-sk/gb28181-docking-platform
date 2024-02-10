package cn.skcks.docking.gb28181.media.dto.record;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Data
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class StartRecord {
    /**
     * 添加的流的虚拟主机，例如__defaultVhost__
     */
    @Builder.Default
    private String vhost = "__defaultVhost__";

    /**
     * 0 为 hls，1 为 mp4
     */
    @Builder.Default
    private Integer type = 1;

    /**
     * 添加的流的应用名，例如live
     */
    private String app;
    /**
     * 添加的流的id名
     */
    private String stream;

    /**
     * 录像保存目录
     */
    private String customizedPath;

    /**
     * mp4 录像切片时间大小,单位秒，置 0 则采用配置项
     */
    private int maxSecond = 0;
}
