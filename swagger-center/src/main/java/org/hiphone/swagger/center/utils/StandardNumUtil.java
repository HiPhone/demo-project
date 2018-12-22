package org.hiphone.swagger.center.utils;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import org.springframework.util.StringUtils;

import java.util.Set;

/**
 * @author HiPhone
 */
public class StandardNumUtil {

    private static final String CONTACT = "contact";
    private static final String URL = "url";
    private static final String NAME = "name";
    private static final String EMAIL = "email";
    private static final String DESCRIPTION = "description";
    private static final String TITLE = "title";
    private static final String ERROR_PATH = "/error";
    private static final String PROPERTIES = "properties";

    /**
     * 统计swagger json数据中的info不规范数目
     * @param infoObj swaggerJson中的info字段
     * @return info不规范的数目
     */
    public static int getNotStandardNumOfInfo(JSONObject infoObj) {
       int sum = 0;

       //检查info中的contact部分
        JSONObject contact = infoObj.getJSONObject(CONTACT);
        if (contact == null) {
            return -1;
        } else {
            //检查contact中的数据
            if (StringUtils.isEmpty(contact.getString(NAME))) { sum++; }
            if (StringUtils.isEmpty(contact.getString(URL))) { sum++; }
            if (StringUtils.isEmpty(contact.getString(EMAIL))) { sum++; }
        }
        //检查info中的description
        if (StringUtils.isEmpty(infoObj.getString(DESCRIPTION))) { sum++; }
        //检查info中的title
        if (StringUtils.isEmpty(infoObj.getString(TITLE))) { sum++; }
        return sum;
    }

    /**
     * 获取tags不规范的数目
     * @param tagsArray tags的JSONArray
     * @return 不规范的数目
     */
    public static int getNotStandardNumOfTags(JSONArray tagsArray) {
        int sum = 0;

        for (Object t : tagsArray) {
            JSONObject tagsObj = (JSONObject) t;
            String description = tagsObj.getString(DESCRIPTION).toLowerCase().replace(" ", "-");
            if (description.equals(tagsObj.getString(NAME))) { sum++; }
        }
        return sum;
    }

    /**
     * 获取swagger api-docs中path部分对接不规范的数目
     * @param pathObj swagger path Object
     * @return 不规范的数目
     */
    public static int getNotStandardNumOfPaths(JSONObject pathObj) {
        int sum = 0;
        Set<String> pathKeySet = pathObj.keySet();
        if (pathKeySet.contains(ERROR_PATH)) { sum++; }

        for (String key : pathKeySet) {
            JSONObject contextPath = pathObj.getJSONObject(key);
            Set<String> contextMethod = contextPath.keySet();
            if (contextMethod.size() == 7) { sum++; }
        }
        return sum;
    }

    /**
     * 获取javaBean对接不规范的数目
     * @param definitionsObj swagger api-docs javabean部分
     * @param whiteList 不检查的白名单
     * @return 不规范的数目
     */
    public static int getNotStandardNumOfDefinitions(JSONObject definitionsObj, String[] whiteList) {
        int sum = 0;

        if (definitionsObj != null) {
            Set<String> definitionsSet = definitionsObj.keySet();
            for (String definition : definitionsSet) {
                boolean inWhiteList = false;
                JSONObject javaBean = definitionsObj.getJSONObject(definition);
                JSONObject props = javaBean.getJSONObject(PROPERTIES);
                for (String s : whiteList) {
                    if (definition.equals(s)) {
                        inWhiteList = true;
                        break;
                    }
                }
                if (inWhiteList || props == null) {
                    continue;
                } else {
                    for (String s : props.keySet()) {
                        JSONObject each  = props.getJSONObject(s);
                        if (each.getString(DESCRIPTION) == null) {
                            sum++;
                        }
                    }
                }
            }
        }
        return sum;
    }

}
