package cn.skcks.docking.gb28181.utils;

import cn.skcks.docking.gb28181.common.json.JsonResponse;
import org.springframework.web.context.request.async.DeferredResult;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;

public class FutureDeferredResult {
    public static <T> DeferredResult<JsonResponse<T>> toDeferredResultWithJson(CompletableFuture<T> future){
        DeferredResult<JsonResponse<T>> result = new DeferredResult<>();
        future.whenComplete((data,throwable)->{
            if(throwable!= null){
                result.setResult(JsonResponse.error(throwable.getMessage()));
                return;
            }
            result.setResult(JsonResponse.success(data));
        });
        return result;
    }

    public static <T> DeferredResult<JsonResponse<T>> toDeferredResultWithJsonAndTimeout(CompletableFuture<T> future, long time, TimeUnit timeUnit){
        DeferredResult<JsonResponse<T>> result = new DeferredResult<>(timeUnit.toMillis(time));
        result.onTimeout(()-> result.setResult(JsonResponse.error("请求超时")));
        future.whenComplete((data,throwable)->{
            if(throwable!= null){
                result.setResult(JsonResponse.error(throwable.getMessage()));
                return;
            }
            result.setResult(JsonResponse.success(data));
        });
        return result;
    }

    public static <T> DeferredResult<T> toDeferredResult(CompletableFuture<T> future){
        DeferredResult<T> result = new DeferredResult<>();
        future.whenComplete((data,throwable)->{
            result.setResult(data);
        });
        return result;
    }

    public static <T> DeferredResult<T> toDeferredResultWithTimeout(CompletableFuture<T> future, T timeoutResult,long time, TimeUnit timeUnit){
        DeferredResult<T> result = new DeferredResult<>(timeUnit.toMillis(time), timeoutResult);
        future.completeOnTimeout(timeoutResult,time,timeUnit);
        future.whenComplete((data, throwable) -> {
            result.setResult(data);
        });
        return result;
    }
}
