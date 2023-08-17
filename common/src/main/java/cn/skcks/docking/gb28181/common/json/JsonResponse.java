package cn.skcks.docking.gb28181.common.json;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Data
@SuppressWarnings("unused")
@Schema(title = "返回结果")
public class JsonResponse<T> {
    @Schema(title = "状态码")
    private int code;

    @Schema(title = "响应消息")
    private String msg;

    @Schema(title = "响应数据")
    private T data;

    public JsonResponse(int code, String msg, T data) {
        this.code = code;
        this.msg = msg;
        this.data = data;
    }

    public static <T> JsonResponse<T> success(T data) {
        return JsonResponse.build(data, ResponseStatus.OK);
    }

    public static <T> JsonResponse<T> success(T data, String message) {
        return JsonResponse.build(data, ResponseStatus.OK.getCode(), message);
    }

    public static <T> JsonResponse<T> error(T data) {
        return JsonResponse.build(data, ResponseStatus.INTERNAL_SERVER_ERROR);
    }

    public static <T> JsonResponse<T> error(T data, String message) {
        return JsonResponse.build(data, ResponseStatus.INTERNAL_SERVER_ERROR.getCode(), message);
    }

    public static <T> JsonResponse<T> build(ResponseStatus status) {
        return new JsonResponse<>(status.getCode(), status.getMessage(),null);
    }

    public static <T> JsonResponse<T> build(T data, ResponseStatus status) {
        return new JsonResponse<>(status.getCode(), status.getMessage(), data);
    }

    public static <T> JsonResponse<T> build(ResponseStatus status,String message) {
        return new JsonResponse<>(status.getCode(), message, null);
    }

    public static <T> JsonResponse<T> build(T data, int status, String msg) {
        return new JsonResponse<>(status, msg, data);
    }

    @Override
    public String toString(){
        return JsonUtils.toJson(this);
    }
}
