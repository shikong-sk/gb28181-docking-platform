package cn.skcks.docking.gb28181.media.dto.record;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
@Data
public class GetMp4RecordFile {
    /**
     * 添加的流的虚拟主机，例如__defaultVhost__
     */
    @Builder.Default
    private String vhost = "__defaultVhost__";

    /**
     * 添加的流的应用名，例如live
     */
    private String app;

    /**
     * 添加的流的id名
     */
    private String stream;

    /**
     * 流的录像日期，格式为 2020-02-01,如果不是完整的日期，那么是搜索录像文件夹列表，否则搜索对应日期下的 mp4 文件列表
     */
    private String period;

    /**
     * 自定义搜索路径，与 startRecord 方法中的 customized_path 一样，默认为配置文件的路径
     */
    private String customizedPath;
}
