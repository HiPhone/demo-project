package org.hiphone.auto.publisher.entitys;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * @author HiPhone
 */
@Getter
@ApiModel(value = "BaseParam", description = "shell脚本参数的封装类")
public abstract class BaseParam {

    @ApiModelProperty(value = "必须的参数", name = "requiredParams")
    private Map<String, String> requiredParams = new LinkedHashMap<>();

    @ApiModelProperty(value = "可选参数", name = "optionalParams")
    private Map<String, String> optionalParams = new LinkedHashMap<>();

    @ApiModelProperty(value = "扩展参数", name = "extendedParams")
    private Map<String, String> extendedParams = new LinkedHashMap<>();

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
