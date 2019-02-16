package org.hiphone.auto.publisher.exception;

/**
 * @author HiPhone
 */
public class BusinessException extends RuntimeException {

    private static final long serialVersionUID = 6971191699145009474L;

    private int errorCode;

    private String errorMsg;

    private Throwable exception;

    public BusinessException(int errorCode, String errorMsg) {
        this.errorCode = errorCode;
        this.errorMsg = errorMsg;
    }

    public BusinessException(int errorCode, String errorMsg, Throwable exception) {
        this(errorCode, errorMsg);
        this.exception = exception;
    }

    public BusinessException(ReturnMsg errorMsg) {
        this(errorMsg.getCode(), errorMsg.getMessage());
    }

    public BusinessException(ReturnMsg errorMsg, Throwable exception) {
        this(errorMsg.getCode(), errorMsg.getMessage(), exception);
    }

    public int getErrorCode() {
        return errorCode;
    }

    public void setErrorCode(int errorCode) {
        this.errorCode = errorCode;
    }

    public String getErrorMsg() {
        return errorMsg;
    }

    public void setErrorMsg(String errorMsg) {
        this.errorMsg = errorMsg;
    }

    public Throwable getException() {
        return exception;
    }

    public void setException(Throwable exception) {
        this.exception = exception;
    }

    @Override
    public String toString() {
        return "BusinessException{" +
                "errorCode=" + errorCode +
                ", errorMsg='" + errorMsg + '\'' +
                '}';
    }
}
