package org.hiphone.auto.publisher.entitys;

import org.hiphone.auto.publisher.constants.Constant;
import org.hiphone.auto.publisher.constants.ParamKeys;

import java.util.List;

/**
 * @author HiPhone
 */
public class DeployParam extends BaseParam {

    @Override
    public String getScript() {
        return Constant.DEPLOY_SCRIPT;
    }

    @Override
    public List<String> getRequiredKey() {
        return ParamKeys.DEPLOY_REQUIRED_KEY;
    }

    @Override
    public List<String> getOptionalKey() {
        return ParamKeys.DEPLOY_OPTIONAL_KEY;
    }

    @Override
    public List<String> getExtendedKey() {
        return ParamKeys.DEPLOY_EXTEND_KEY;
    }
}
