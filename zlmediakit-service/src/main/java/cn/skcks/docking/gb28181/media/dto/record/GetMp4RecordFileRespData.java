package cn.skcks.docking.gb28181.media.dto.record;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class GetMp4RecordFileRespData {
    private List<String> paths;
    private String rootPath;
}
