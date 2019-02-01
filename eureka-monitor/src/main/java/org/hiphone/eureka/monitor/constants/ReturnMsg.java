package org.hiphone.eureka.monitor.constants;

import lombok.Getter;

/**
 * @author HiPhone
 */
public enum ReturnMsg {

    SUCCESS(0, "服务调用成功"),
    UNKNOWN_ERROR(9999, "未知错误请排查"),
    UNAUTHORIZED(1001, "服务调用未授权"),
    PARAM_ERROR(1002, "参数错误"),
    BUSY_ERROR(1004, "服务繁忙，请稍后尝试");

    @Getter
    private String message;

    @Getter
    private int code;

    private ReturnMsg(int code, String message) {
        this.code = code;
        this.message = message;
    }

}
