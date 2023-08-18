package cn.skcks.docking.gb28181.media.dto.response;

import cn.skcks.docking.gb28181.common.json.JsonResponse;
import cn.skcks.docking.gb28181.media.dto.status.ResponseStatus;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public interface ZlmResponseConvertor {
    ZlmResponseConvertor INSTANCE = Mappers.getMapper(ZlmResponseConvertor.class);

    default <T> JsonResponse<T> toJsonResponse(ZlmResponse<T> response){
        return JsonResponse.build(response.getData(), response.getCode().getCode(), response.getMsg());
    }
    default <T> ZlmResponse<T> toZlmResponse(JsonResponse<T> response){
        return new ZlmResponse<>(ResponseStatus.fromCode(response.getCode()), response.getData(), response.getMsg());
    }
}
