package org.hiphone.eureka.monitor.exception;

import lombok.extern.slf4j.Slf4j;
import org.hiphone.eureka.monitor.entitys.ResultMessage;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.sql.SQLException;
import java.text.ParseException;

/**
 * @author HiPhone
 */
@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    /**
     * 参数错误的异常处理
     * @param ex 抛出的exception
     * @return 返回前端的封装数据
     */
    @ExceptionHandler(IllegalArgumentException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResultMessage handleException(IllegalArgumentException ex) {
      log.error("参数错误异常：", ex);
      return new ResultMessage(
              ReturnMsg.PARAM_ERROR.getCode(),
              ReturnMsg.PARAM_ERROR.getMessage(),
              ex.getMessage()
      );
    }

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
     * 数据解析出错的异常处理
     * @param ex 抛出的exception
     * @return 返回前端的封装数据
     */
    @ExceptionHandler(ParseException.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ResultMessage handleException(ParseException ex) {
        log.error("数据解析出错： ", ex);
        return new ResultMessage(
                ReturnMsg.PARSE_ERROR.getCode(),
                ReturnMsg.PARSE_ERROR.getMessage(),
                ex.getMessage()
        );
    }
}
