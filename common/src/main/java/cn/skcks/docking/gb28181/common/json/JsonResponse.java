package cn.skcks.docking.gb28181.common.json;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
@SuppressWarnings("unused")
@Schema(title = "返回结果")
public class JsonResponse<T> {
    @Schema(title = "状态码")
    private int code;

    @Schema(title = "响应数据")
    private T data;

    @Schema(title = "响应消息")
    private String msg;

    public static <T> JsonResponse<T> success(T data) {
        return JsonResponse.build(data, ResponseStatus.OK);
    }

    public static <T> JsonResponse<T> success(T data, String message) {
        return JsonResponse.build(ResponseStatus.OK.getCode(), data, message);
    }

    public static <T> JsonResponse<T> error(String message) {
        return JsonResponse.build(ResponseStatus.INTERNAL_SERVER_ERROR.getCode(), null, message);
    }

    public static <T> JsonResponse<T> error(T data, String message) {
        return JsonResponse.build(ResponseStatus.INTERNAL_SERVER_ERROR.getCode(), data, message);
    }

    public static <T> JsonResponse<T> build(ResponseStatus status) {
        return new JsonResponse<>(status.getCode(), null, status.getMessage());
    }

    public static <T> JsonResponse<T> build(T data, ResponseStatus status) {
        return new JsonResponse<>(status.getCode(), data, status.getMessage());
    }

    public static <T> JsonResponse<T> build(ResponseStatus status, String message) {
        return new JsonResponse<>(status.getCode(), null, message);
    }

    public static <T> JsonResponse<T> build(ResponseStatus status, T data, String message) {
        return new JsonResponse<>(status.getCode(), data, message);
    }

    public static <T> JsonResponse<T> build(int status, T data, String msg) {
        return new JsonResponse<>(status, data, msg);
    }

    @Override
    public String toString() {
        return JsonUtils.toJson(this);
    }
}
