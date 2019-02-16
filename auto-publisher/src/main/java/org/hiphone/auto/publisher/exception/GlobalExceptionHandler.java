package org.hiphone.auto.publisher.exception;

import lombok.extern.slf4j.Slf4j;
import org.hiphone.auto.publisher.entitys.ResultMessage;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * @author HiPhone
 */
@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    /**
     * 未知异常的处理
     * @param ex 异常
     * @return 异常提示
     */
    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ResultMessage handleException(Exception ex) {
        log.error("发生未知错误： ", ex);
        return new ResultMessage(
                ReturnMsg.UNKNOWN_ERROR.getCode(),
                ReturnMsg.UNKNOWN_ERROR.getMessage(),
                ex.getMessage()
        );
    }

    /**
     * 自定义业务异常的处理
     * @param ex 异常
     * @return 异常提示
     */
    @ExceptionHandler(BusinessException.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ResultMessage handleException(BusinessException ex) {
        log.error("发生业务异常: ", ex);
        return new ResultMessage(
                ex.getErrorCode(),
                ex.getMessage(),
                ex.getException()
        );
    }
}
