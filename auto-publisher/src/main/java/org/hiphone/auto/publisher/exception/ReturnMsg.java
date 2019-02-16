package org.hiphone.auto.publisher.exception;

import lombok.Getter;

/**
 * @author HiPhone
 */
public enum ReturnMsg {

    SUCCESS(0, "服务调用成功"),
    PARAM_ERROR(1001, "参数错误，请检查"),
    HOST_INFO_ERROR(1002, "host信息格式错误"),
    SSH_AUTH_ERROR(1003, "ssh认证信息不能为空"),
    UNKNOWN_ERROR(9999, "未知错误请排查");

    @Getter
    private String message;

    @Getter
    private int code;

    ReturnMsg(int code, String message) {
        this.code = code;
        this.message = message;
    }

}
