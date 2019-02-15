package org.hiphone.auto.publisher.entitys;

import org.hiphone.auto.publisher.constants.Constant;
import org.hiphone.auto.publisher.constants.ParamKeys;

import java.util.List;

/**
 * @author HiPhone
 */
public class AuthParam extends BaseParam {

    @Override
    public String getScript() {
        return Constant.AUTH_SCRIPT;
    }

    @Override
    public List<String> getRequiredKey() {
        return ParamKeys.AUTH_REQUIRED_KEY;
    }

    @Override
    public List<String> getOptionalKey() {
        return ParamKeys.AUTH_OPTIONAL_KEY;
    }

    @Override
    public List<String> getExtendedKey() {
        return ParamKeys.AUTH_EXTENDED_KEY;
    }
}
