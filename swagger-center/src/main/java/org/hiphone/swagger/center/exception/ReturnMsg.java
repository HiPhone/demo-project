package org.hiphone.swagger.center.exception;

import lombok.Getter;

/**
 * @author HiPhone
 */
public enum ReturnMsg {

    SUCCESS(0, "服务调用成功"),
    UNKNOWN_ERROR(9999, "未知错误请排查"),
    LOGIN_FAIL(1001, "用户名或密码错误"),
    LOGIN_SUCCESS(1002, "登陆成功"),
    PARAM_ERROR(1001, "接口参数有误"),
    FORBIDDEN(1003, "未登录，无法访问"),
    SQL_ERROR(1005, "数据库异常"),
    IO_ERROR(1004, "读写错误");

    @Getter
    private String message;

    @Getter
    private int code;

    ReturnMsg(int code, String message) {
        this.code = code;
        this.message = message;
    }

}
