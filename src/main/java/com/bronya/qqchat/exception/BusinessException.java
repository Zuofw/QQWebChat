package com.bronya.qqchat.exception;


import com.bronya.qqchat.domain.enums.ErrorCodeEnum;
import lombok.Data;
import lombok.EqualsAndHashCode;


@Data
@EqualsAndHashCode(callSuper = false)
public class BusinessException extends RuntimeException {

    private final Integer code;

    private final String message;

    private final String description;

    public BusinessException(ErrorCodeEnum errorCodeEnum, String description) {
        this.code = errorCodeEnum.getCode();
        this.message = errorCodeEnum.getMessage();
        this.description = description;
    }

}
