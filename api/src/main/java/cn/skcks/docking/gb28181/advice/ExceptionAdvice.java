package cn.skcks.docking.gb28181.advice;

import cn.skcks.docking.gb28181.common.json.JsonResponse;
import jakarta.validation.ConstraintViolationException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.BindException;
import org.springframework.web.HttpMediaTypeNotAcceptableException;
import org.springframework.web.HttpMediaTypeNotSupportedException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.async.AsyncRequestTimeoutException;

import java.util.Objects;

/**
 * 全局异常处理类
 *
 * @author Shikong
 */
@Slf4j
@RestControllerAdvice
public class ExceptionAdvice {

    @ExceptionHandler(MissingServletRequestParameterException.class)
    public JsonResponse<String> missingServletRequestParameterException(MissingServletRequestParameterException e) {
        return JsonResponse.error(e.getMessage());
    }

    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    public JsonResponse<String> httpRequestMethodNotSupportedException(HttpRequestMethodNotSupportedException e){
        return JsonResponse.error(e.getMessage());
    }

    @ExceptionHandler(HttpMediaTypeNotSupportedException.class)
    public JsonResponse<String> unsupportedMediaTypeException(Exception e) {
        e.printStackTrace();
        return JsonResponse.error(e.getMessage());
    }

    @ExceptionHandler(HttpMediaTypeNotAcceptableException.class)
    public JsonResponse<String> httpMediaTypeNotAcceptableException(HttpMediaTypeNotAcceptableException e){
        return JsonResponse.error(e.getMessage());
    }

    @ExceptionHandler(RuntimeException.class)
    public JsonResponse<String> runtimeException(RuntimeException e) {
        e.printStackTrace();
        return JsonResponse.error(e.getMessage());
    }

    @ExceptionHandler(BindException.class)
    public JsonResponse<String> handleValidationBindException(BindException e) {
        return JsonResponse.error(Objects.requireNonNull(e.getBindingResult().getFieldError()).getDefaultMessage());
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public JsonResponse<String> handleMethodArgumentNotValidException(MethodArgumentNotValidException e) {
        return JsonResponse.error(Objects.requireNonNull(e.getBindingResult().getFieldError()).getDefaultMessage());
    }

    @ExceptionHandler(ConstraintViolationException.class)
    public JsonResponse<String> handleConstraintViolationException(ConstraintViolationException e) {
        return JsonResponse.error(Objects.requireNonNull(e.getMessage()));
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public JsonResponse<String> handleHttpMessageNotReadableException(HttpMessageNotReadableException e){
        log.warn("{}", e.getMessage());
        return JsonResponse.error("参数异常");
    }

    @ExceptionHandler(AsyncRequestTimeoutException.class)
    public JsonResponse<String> exception(AsyncRequestTimeoutException e) {
        e.printStackTrace();
        return JsonResponse.error("请求超时");
    }

    @ExceptionHandler(Exception.class)
    public JsonResponse<String> exception(Exception e) {
        e.printStackTrace();
        return JsonResponse.error(e.getMessage());
    }
}
