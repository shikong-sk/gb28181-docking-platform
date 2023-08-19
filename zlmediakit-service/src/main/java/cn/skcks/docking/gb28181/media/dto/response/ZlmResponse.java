package cn.skcks.docking.gb28181.media.dto.response;

import cn.skcks.docking.gb28181.common.json.JsonResponse;
import cn.skcks.docking.gb28181.common.json.JsonUtils;
import cn.skcks.docking.gb28181.media.dto.status.ResponseStatus;
import com.fasterxml.jackson.annotation.JsonIgnore;
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

    @Schema(title = "响应数据")
    private T data;

    @Schema(title = "响应消息")
    private String msg;

    @Override
    public String toString(){
        return JsonUtils.toJson(this);
    }

    @JsonIgnore
    public JsonResponse<T> getJsonResponse(){
        return ZlmResponseConvertor.INSTANCE.toJsonResponse(this);
    }
}
