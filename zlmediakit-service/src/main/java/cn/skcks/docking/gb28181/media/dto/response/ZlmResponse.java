package cn.skcks.docking.gb28181.media.dto.response;

import cn.skcks.docking.gb28181.common.json.JsonUtils;
import cn.skcks.docking.gb28181.media.dto.status.ResponseStatus;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class ZlmResponse<T> {
    @Schema(title = "状态码")
    private ResponseStatus code;

    @Schema(title = "响应消息")
    private String msg;

    @Schema(title = "响应数据")
    private T data;

    @Override
    public String toString(){
        return JsonUtils.toJson(this);
    }
}
