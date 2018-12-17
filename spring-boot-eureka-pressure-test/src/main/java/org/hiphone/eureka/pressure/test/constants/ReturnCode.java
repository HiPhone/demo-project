package org.hiphone.eureka.pressure.test.constants;

/**
 * @author HiPhone
 */
public enum ReturnCode {

    SUCCESS(0, "服务调用成功"),
    UNKNOWN_ERROR(9999, "未知错误请排查"),
    UNAUTHORIZED(1001, "服务调用未授权"),
    BUSY_ERROR(1004, "服务繁忙，请稍后尝试");


    private String message;

    private int code;

    ReturnCode(int code, String message) {
        this.code = code;
        this.message = message;
    }

    public String getMessage() {
        return this.message;
    }

    public int getCode() {
        return this.code;
    }
}
