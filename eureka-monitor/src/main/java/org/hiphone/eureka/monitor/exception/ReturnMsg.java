package org.hiphone.eureka.monitor.exception;

import lombok.Getter;

/**
 * @author HiPhone
 */
public enum ReturnMsg {

    SUCCESS(0, "服务调用成功"),
    UNKNOWN_ERROR(9999, "未知错误请排查"),
    UNAUTHORIZED(1001, "服务调用未授权"),
    PARAM_ERROR(1002, "参数错误"),
    SQL_ERROR(1003, "数据库出错"),
    PARSE_ERROR(1004, "数据解析出错"),
    BUSY_ERROR(1005, "服务繁忙，请稍后尝试");


    @Getter
    private String message;

    @Getter
    private int code;

    ReturnMsg(int code, String message) {
        this.code = code;
        this.message = message;
    }

}
