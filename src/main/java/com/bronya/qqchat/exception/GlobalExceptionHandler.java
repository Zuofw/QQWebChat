package com.bronya.qqchat.exception;


import com.bronya.qqchat.domain.enums.ErrorCodeEnum;
import com.bronya.qqchat.domain.vo.Result;
import com.bronya.qqchat.util.ResultUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import jakarta.validation.ConstraintViolationException;
@ControllerAdvice
@Slf4j
public class GlobalExceptionHandler {


    @ExceptionHandler(value = BusinessException.class)
    @ResponseBody
    public Result<?> businessExceptionHandler(BusinessException e) {
        return ResultUtils.error(e);
    }

    @ExceptionHandler(value = RuntimeException.class)
    @ResponseBody
    public Result<?> runtimeExceptionHandler(RuntimeException e) {
        return ResultUtils.error(
                ErrorCodeEnum.INTERNAL_SERVER_ERROR.getCode(),
                e.getMessage(),
                "服务器发生错误, 请及时告知开发人员"
        );
    }

    @ExceptionHandler(value = {MethodArgumentNotValidException.class, HttpMessageNotReadableException.class, ConstraintViolationException.class})
    @ResponseBody
    public Result<?> ArgumentValidExceptionHandler(Exception ignored) {
        return ResultUtils.error(
                ErrorCodeEnum.BAD_REQUEST,
                "请检查参数是否正确"
        );
    }

    @ExceptionHandler(value = {MissingServletRequestParameterException.class})
    @ResponseBody
    public Result<?> MissingServletParameterExceptionHandler(Exception e) {
        return ResultUtils.error(
                ErrorCodeEnum.BAD_REQUEST,
                "请核对参数，以免重新登录"
        );
    }
}
