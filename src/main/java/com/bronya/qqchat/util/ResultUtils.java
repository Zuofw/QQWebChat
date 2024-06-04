package com.bronya.qqchat.util;

import com.bronya.qqchat.domain.enums.ErrorCodeEnum;
import com.bronya.qqchat.domain.vo.Result;
import com.bronya.qqchat.exception.BusinessException;
import org.springframework.stereotype.Component;

@Component
public class ResultUtils {
    public static <T> Result<T> success(T data) {
        return new Result<>(ErrorCodeEnum.OK, data, "操作成功");
    }

    public static Result<?> error(BusinessException exception) {
        return new Result<>(exception.getCode(), exception.getMessage(), exception.getDescription(), null);
    }

    public static Result<?> error(Integer code, String message, String description) {
        return new Result<>(code, message, description, null);
    }

    public static Result<?> error(ErrorCodeEnum codeEnum, String description) {
        return error(codeEnum.getCode(), codeEnum.getMessage(), description);
    }
}
