package org.hiphone.auto.publisher.utils;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.hiphone.auto.publisher.constants.Constant;
import org.hiphone.auto.publisher.entitys.BaseParam;

import java.util.List;
import java.util.Map;

/**
 * @author HiPhone
 */
@Slf4j
public class ShellJointer {

    public static String joinShellCommand(BaseParam param) {
        String command = new StringBuilder()
                .append(param.getScript()).append(" ")
                .append(getRequiredParams(param)).append(" ")
                .append(getOptionalParams(param)).append(" ")
                .append(getExtendedParams(param)).append(" ").toString();
        log.info("Shell command create success, command is: " + command);
        return command;
    }

    private static String getRequiredParams(BaseParam param) {
        return constructParameters(null, param.getRequiredKey(), param.getRequiredParams());
    }

    private static String getOptionalParams(BaseParam param) {
        return constructParameters(null, param.getOptionalKey(), param.getOptionalParams());
    }

    private static String getExtendedParams(BaseParam param) {
        return constructParameters(Constant.EXTENDED_ACTION, param.getExtendedKey(), param.getExtendedParams());
    }


    /**
     * 将参数拼成命令的字符串
     * @param action 操作
     * @param keys 操作的key的集合
     * @param keyMap key对应操作的map
     * @return shell命令
     */
    private static String constructParameters(String action, List<String> keys, Map<String, String> keyMap) {
        StringBuilder stringBuilder = new StringBuilder();
        if (keys == null || keys.isEmpty() || keyMap == null || keyMap.isEmpty()) {
            return stringBuilder.toString();
        }

        if (action != null && "".equals(action.trim())) {
            stringBuilder.append(action).append(" ");
        }

        keys.forEach(k -> {
            String value = keyMap.get(k);
            if (!StringUtils.isBlank(value)) {
                stringBuilder.append(value).append(" ");
            }
        });

        return stringBuilder.toString();
    }
}
