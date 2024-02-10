package cn.skcks.docking.gb28181.media.dto.record;

import cn.skcks.docking.gb28181.media.dto.status.ResponseStatus;
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
public class DeleteRecordDirectoryResp {
    private ResponseStatus code;
    private String path;
}
