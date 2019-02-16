package org.hiphone.auto.publisher.utils;

import org.apache.commons.lang3.StringUtils;
import org.hiphone.auto.publisher.exception.BusinessException;
import org.hiphone.auto.publisher.exception.ReturnMsg;

/**
 * @author HiPhone
 */
public class AssertUtils {

    /**
     * param不为空
     * @param error 错误码与错误信息
     * @param param 判断的param
     */
    public static void notNull(ReturnMsg error, Object param) {
        notNull(error.getCode(), error.getMessage(), param);
    }

    /**
     * param不为空
     * @param code 错误码
     * @param message 错误信息
     * @param param 判断的param
     */
    public static void notNull(int code, String message, Object param) {
        if (param == null) {
            throw new BusinessException(code, message);
        }
        if (param instanceof String) {
            if (StringUtils.isBlank((String) param)) {
                throw new BusinessException(code, message);
            }
        }
    }

    /**
     * 变长的所有param不为空
     * @param code 错误码
     * @param message 错误信息
     * @param params 判断的param
     */
    public static void allNotNull(int code, String message, Object ... params) {
        for (Object o : params) {
            if (o == null) {
                throw new BusinessException(code, message);
            }
            if (o instanceof String) {
                if (StringUtils.isBlank((String) o)) {
                    throw new BusinessException(code, message);
                }
            }
        }
    }

    /**
     * 变长的所有param不为空
     * @param error 错误码和错误信息
     * @param params 判断的params
     */
    public static void allNotNull(ReturnMsg error, Object ... params) {
        allNotNull(error.getCode(), error.getMessage(), params);
    }

    /**
     * 至少一个参数部位空
     * @param code 错误码
     * @param message 错误信息
     * @param params 判断的params
     */
    public static void ontNotNull(int code, String message, Object ... params) {
        boolean isAllNull = true;
        for (Object o : params) {
            if (o == null) {
                isAllNull = false;
                break;
            }

            if (o instanceof String) {
                if (StringUtils.isNoneBlank((String) o)) {
                    isAllNull = false;
                    break;
                }
            }
        }
        if (isAllNull) {
            throw new BusinessException(code, message);
        }
    }

    /**
     * 参数的值为true
     * @param code 错误码
     * @param message 错误信息
     * @param condition 判断的参数
     */
    public static void shouldBeTrue(int code, String message, boolean condition) {
        if (!condition) {
            throw new BusinessException(code, message);
        }
    }

    /**
     * 多个参数的值为true
     * @param code 错误码
     * @param message 错误信息
     * @param conditions 判断的参数列表
     */
    public static void allShouldBeTrue(int code, String message, boolean ... conditions) {
        for (boolean c : conditions) {
            if (!c) {
                throw new BusinessException(code, message);
            }
        }
    }

}
