package org.hiphone.auto.publisher.entitys;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * @author HiPhone
 */
public abstract class BaseParam {

    /**
     * 必须的参数
     */
    private Map<String, String> requiredParams = new LinkedHashMap<>();

    /**
     * 选填参数
     */
    private Map<String, String> optionalParams = new LinkedHashMap<>();

    /**
     * 扩展的参数
     */
    private Map<String, String> extendedParams = new LinkedHashMap<>();

    public Map<String, String> getRequiredParams() {
        return requiredParams;
    }

    public Map<String, String> getOptionalParams() {
        return optionalParams;
    }

    public Map<String, String> getExtendedParams() {
        return extendedParams;
    }

    public void addRequiredParams(String key, String value) {
        requiredParams.put(key, value);
    }

    public void addOptionalParams(String key, String value) {
        optionalParams.put(key, value);
    }

    public void addExtendedParams(String key, String value) {
        extendedParams.put(key, value);
    }

    public abstract String getScript();
    public abstract List<String> getRequiredKey();
    public abstract List<String> getOptionalKey();
    public abstract List<String> getExtendedKey();
}
