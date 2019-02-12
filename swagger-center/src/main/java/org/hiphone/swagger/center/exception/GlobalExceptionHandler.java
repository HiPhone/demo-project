package org.hiphone.swagger.center.exception;

import lombok.extern.slf4j.Slf4j;
import org.hiphone.swagger.center.entitys.ResultMessage;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.io.IOException;
import java.sql.SQLException;

/**
 * @author HiPhone
 */
@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    /**
     * 未知错误的异常处理
     * @param ex 抛出的exception
     * @return 返回前端的封装数据
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
     * 数据库异常的处理
     * @param ex 抛出的exception
     * @return 返回前端的封装数据
     */
    @ExceptionHandler(SQLException.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ResultMessage handleException(SQLException ex) {
        log.error("发生数据库错误： ", ex);
        return new ResultMessage(
                ReturnMsg.SQL_ERROR.getCode(),
                ReturnMsg.SQL_ERROR.getMessage(),
                ex.getMessage()
        );
    }

    /**
     * IO异常的处理
     * @param ex 抛出的exception
     * @return 返回前端的封装数据
     */
    @ExceptionHandler(IOException.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ResultMessage handleException(IOException ex) {
        log.error("发生读写错误： ", ex);
        return new ResultMessage(
                ReturnMsg.IO_ERROR.getCode(),
                ReturnMsg.IO_ERROR.getMessage(),
                ex.getMessage()
        );
    }
}
