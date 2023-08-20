package cn.skcks.docking.gb28181.media.dto.media;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class GetMediaList {
    /**
     * 筛选协议，例如 rtsp或rtmp
     */
    private String schema;
    /**
     * 筛选虚拟主机，例如__defaultVhost__
     */
    private String vhost;
    /**
     * 筛选应用名，例如 live
     */
    private String app;
    /**
     * 筛选流id，例如 test
     */
    private String stream;
}
