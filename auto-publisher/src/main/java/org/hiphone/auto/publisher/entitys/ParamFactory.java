package org.hiphone.auto.publisher.entitys;

import org.hiphone.auto.publisher.constants.ParamGroup;

/**
 * 参数工厂类
 * @author HiPhone
 */
public class ParamFactory {

    private static BaseParam createBaseParam(ParamGroup group) {
        BaseParam baseParam = null;

        switch (group) {
            case IPS:
                baseParam = new IPsParam();
                break;
            case AUTH:
                baseParam = new AuthParam();
                break;
            case DEPLOY:
                baseParam = new DeployParam();
            default: break;
        }

        return baseParam;
    }
}
