package org.hiphone.swagger.center.utils;

import com.alibaba.fastjson.JSONObject;
import org.hiphone.swagger.center.constants.Constant;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * @author HiPhone
 */
public class StandardDetailsUtil {

    /**
     * 获取不符合restful命名规范的api
     * @param swaggerJson swaggerJSON
     * @return 不符合的列表
     */
    public static List<String> getUnRestFulApiDetails(JSONObject swaggerJson) {
        List<String> unRestFulApis = new ArrayList<>();
        String regex1 = "^[a-z0-9-a-z0-9]+$";
        String regex2 = "^[a-z]+$";
        JSONObject path = swaggerJson.getJSONObject(Constant.API_PATH);

        for (String api : path.keySet()) {
            String[] eachPath = api.split("/");
            for (int i = 0; i < eachPath.length; i++) {
                if (eachPath[i].contains("{") || StringUtils.isEmpty(eachPath[i])) {
                    continue;
                } else {
                    if (!eachPath[i].matches(regex1) && !eachPath[i].matches(regex2)) {
                        unRestFulApis.add(api);
                        break;
                    }
                }
            }
        }
        return unRestFulApis;
    }
}
