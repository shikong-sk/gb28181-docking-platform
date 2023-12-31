package cn.skcks.docking.gb28181.api.gb28181.catalog;

import cn.skcks.docking.gb28181.annotation.web.JsonMapping;
import cn.skcks.docking.gb28181.annotation.web.methods.GetJson;
import cn.skcks.docking.gb28181.common.json.JsonResponse;
import cn.skcks.docking.gb28181.service.catalog.CatalogService;
import cn.skcks.docking.gb28181.sip.manscdp.catalog.response.CatalogItemDTO;
import cn.skcks.docking.gb28181.utils.FutureDeferredResult;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.async.DeferredResult;

import java.util.List;
import java.util.concurrent.CompletableFuture;

@Tag(name="获取设备目录信息")
@RestController
@JsonMapping("/api/gb28181/catalog")
@RequiredArgsConstructor
public class CatalogController {
    private final CatalogService catalogService;

    @SneakyThrows
    @GetJson
    public DeferredResult<JsonResponse<List<CatalogItemDTO>>> catalog(String gbDeviceId){
        CompletableFuture<List<CatalogItemDTO>> catalog = catalogService.catalog(gbDeviceId);
        return FutureDeferredResult.toDeferredResultWithJson(catalog);
    }
}
