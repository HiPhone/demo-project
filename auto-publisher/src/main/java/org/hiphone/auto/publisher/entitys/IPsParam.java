package org.hiphone.auto.publisher.entitys;

import org.hiphone.auto.publisher.constants.Constant;
import org.hiphone.auto.publisher.constants.ParamKeys;

import java.util.List;

/**
 * @author HiPhone
 */
public class IPsParam extends BaseParam {

    @Override
    public String getScript() {
        return Constant.IPS_SCRIPT;
    }

    @Override
    public List<String> getRequiredKey() {
        return ParamKeys.IPS_REQUIRED_KEY;
    }

    @Override
    public List<String> getOptionalKey() {
        return ParamKeys.IPS_OPTIONAL_KEY;
    }

    @Override
    public List<String> getExtendedKey() {
        return ParamKeys.IPS_EXTENDED_KEY;
    }
}
