package org.hiphone.auto.publisher.entitys;

import org.apache.commons.lang3.StringUtils;
import org.hiphone.auto.publisher.constants.Constant;
import org.hiphone.auto.publisher.constants.ParamGroup;
import org.hiphone.auto.publisher.exception.ReturnMsg;
import org.hiphone.auto.publisher.utils.AssertUtils;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * 参数工厂类
 * @author HiPhone
 */
public class ParamFactory {

    /**
     * 根据group及http参数创建参数实例
     * @param request http request
     * @param group 组参数
     * @return 对应的实例
     */
    public static BaseParam createParamInstance(HttpServletRequest request, ParamGroup group) {
        BaseParam baseParam = null;
        switch (group) {
            case DEPLOY:
                baseParam = new DeployParam();
                break;
            case AUTH:
                baseParam = new AuthParam();
                break;
            case IPS:
                baseParam = new IPsParam();
            default: break;
        }

        for (String k : baseParam.getRequiredKey()) {
            String value = request.getParameter(k);
            AssertUtils.notNull(ReturnMsg.PARAM_ERROR, value);
            baseParam.addRequiredParams(k, value);
        }

        for (String k : baseParam.getOptionalKey()) {
            String value = request.getParameter(k);
            if (!StringUtils.isEmpty(value)) {
                baseParam.addOptionalParams(k, value);
            }
        }

        String extendedValues = request.getParameter(Constant.EXTENDED_NAME);
        if (!StringUtils.isEmpty(extendedValues)) {
            extendedValues = extendedValues.replace("\\r\\n", "\\n");
            String[] extendedValue = extendedValues.split("\\n");
            List<String> extendedKey = baseParam.getExtendedKey();
            String key = null;
            for (int i = 0; i < extendedValue.length; i++) {
                key = extendedValue[i].split("=")[0];
                if (key != null && extendedKey.contains(key)) {
                    baseParam.addExtendedParams(key, extendedValue[1]);
                }
            }
        }
        return baseParam;
    }
}
