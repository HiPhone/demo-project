package org.hiphone.auto.publisher.utils;

import javax.servlet.http.HttpServletRequest;
import java.util.Enumeration;

/**
 * @author HiPhone
 */
public class LoadParamUtils {

    public static String loadParam(HttpServletRequest request) {
        StringBuffer str = new StringBuffer();
        String key = null;
        for (Enumeration<String> paramNames = request.getParameterNames(); paramNames.hasMoreElements();) {
            key = paramNames.nextElement();
            str.append(key).append("=").append(request.getParameter(key)).append("` ");
        }
        return str.toString();
    }
}
