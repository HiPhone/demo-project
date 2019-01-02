package org.hiphone.swagger.center.utils;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import org.hiphone.swagger.center.constants.Constant;
import org.hiphone.swagger.center.entitys.ApiParamVo;
import org.hiphone.swagger.center.entitys.SimplifyApiVo;

import java.util.LinkedList;
import java.util.List;
import java.util.Set;

/**
 * @author HiPhone
 */
public class ParseSwaggerUtil {

    private static final String UPPER_STRING = "String";
    private static final String LOWER_STRING = "string";
    private static final String INT_32 = "int32";
    private static final String INT_64 = "int64";
    private static final String INTEGER = "Integer";
    private static final String LONG = "Long";
    private static final String DATE_TIME = "date_time";
    private static final String TIMESTAMP = "Timestamp";
    private static final String DECIMAL = "Decimal";
    private static final String NUMBER = "number";
    private static final String REF = "$ref";
    private static final String TYPE = "type";
    private static final String ITEMS = "items";
    private static final String FORMAT = "format";
    private static final String ARRAY = "array";
    private static final String COLLECTION = "Collection<";
    private static final String SUFFIX = ">";
    private static final String NOT_WRITTEN = "没有填写";
    private static final String DEFAULT_DESCRIPTION = "接口测试页面";
    private static final String DEFAULT_TITLE = "SWAGGER_2";
    private static final String TAGS = "tags";
    private static final String SUMMARY = "summary";
    private static final String PARAMETERS = "parameters";
    private static final String REQUIRED = "required";
    private static final String SCHEMA = "schema";
    private static final String PROPERTIES = "properties";

    /**
     * 解析swaggerJson的主方法
     * @param swaggerJson swagger json
     * @return 解析的结果
     */
    public static JSONObject parseSwaggerJson(JSONObject swaggerJson) {
        JSONObject result = new JSONObject();

        List<SimplifyApiVo> resultList = new LinkedList<>();
        JSONArray tagsArray = swaggerJson.getJSONArray(TAGS);
        JSONObject pathsObj = swaggerJson.getJSONObject(Constant.API_PATH);
        JSONObject definitions = swaggerJson.getJSONObject(Constant.API_DEFINITIONS);
        String controller = null, controllerTag;

        for (String key : pathsObj.keySet()) {
            JSONObject pathObj = pathsObj.getJSONObject(key);

            for (String method : pathObj.keySet()) {
                JSONObject methodObj = pathObj.getJSONObject(method);
                JSONArray tags = methodObj.getJSONArray(TAGS);
                List<ApiParamVo> paramVoList = new LinkedList<>();

                for (Object o : tagsArray) {
                    JSONObject obj = (JSONObject) o;
                    if (obj.getString(Constant.NAME).equals(tags.get(0))) {
                        controller = obj.getString(Constant.DESCRIPTION).replaceAll(" ", "");
                    }
                }

                controllerTag = methodObj.getString(SUMMARY);
                JSONArray parameters = methodObj.getJSONArray(PARAMETERS);

                if (parameters != null) {
                    for (Object o : parameters) {
                        JSONObject param = (JSONObject) o;
                        ApiParamVo apiParamVo = new ApiParamVo();
                        apiParamVo.setDescription(param.getString(Constant.DESCRIPTION));
                        apiParamVo.setName(param.getString(Constant.NAME));
                        apiParamVo.setRequired(param.getString(REQUIRED));
                        //有type的入参为基础类型，一般方法为get。无type的入参为bean或者string的post形式
                        if (param.getString(TYPE) != null) {
                            apiParamVo.setType(conventFormatToType(param.getString("format")));
                        } else {
                            JSONObject schema = param.getJSONObject(SCHEMA);
                            if (schema == null) {
                                continue;
                            }

                            if (schema.getString(TYPE) != null) {
                                apiParamVo.setType(schema.getString(TYPE));
                            } else {
                                String ref = schema.getString(REF);
                                apiParamVo.setType(ref.substring(ref.lastIndexOf("/") + 1));
                                JSONObject refObj = definitions.getJSONObject(apiParamVo.getType());
                                if (refObj != null) {
                                    JSONObject prop = refObj.getJSONObject(PROPERTIES);
                                    if (prop != null) {
                                        JSONArray schemaArray = parseApiSchema(prop);
                                        apiParamVo.setSchema(schemaArray);
                                    }
                                }
                            }
                        }
                        paramVoList.add(apiParamVo);
                    }
                }
                SimplifyApiVo simplifyApiVo = new SimplifyApiVo(key, method, controllerTag, controller, tags.getString(0), paramVoList);
                resultList.add(simplifyApiVo);
            }
        }
        result.put(Constant.DESCRIPTION, resultList);
        JSONObject swaggerInfo = swaggerJson.getJSONObject(Constant.API_INFO);
        JSONObject apiInfo = parseInfo(swaggerInfo);
        result.put(Constant.API_INFO, apiInfo);

        return result;
    }

    /**
     * 转换swagger json中的format为javaType
     * @param format swagger json中的format
     * @return 对应的javaType
     */
    private static String conventFormatToType(String format) {
        if (format != null) {
            switch (format) {
                case INT_64: return LONG;
                case INT_32: return INTEGER;
                case DATE_TIME: return TIMESTAMP;
                case NUMBER: return DECIMAL;
                default:
            }
        }
        return UPPER_STRING;
    }

    /**
     * 解析json 中的schema字段
     * @param schema schema字段
     * @return 解析结果
     */
    private static JSONArray parseApiSchema(JSONObject schema) {
        JSONArray schemaArray = new JSONArray();
        Set<String> propsSet = schema.keySet();

        removeItems(propsSet, schema);

        for(String key : propsSet) {
            JSONObject schemaObj = new JSONObject();
            schemaObj.put(Constant.NAME, key);
            JSONObject tmpObj = schemaObj.getJSONObject(key);
            String type = tmpObj.getString(TYPE);
            String format = tmpObj.getString(FORMAT);

            if (format != null) {
                if (!ARRAY.equals(type)) {
                    schemaObj.put(TYPE, conventFormatToType(format));
                } else {
                    schemaObj.put(TYPE, COLLECTION.concat(format).concat(SUFFIX));
                }
            } else {
                if (!LOWER_STRING.equals(type)) {
                    schemaObj.put(TYPE, type);
                } else {
                    schemaObj.put(TYPE, conventFormatToType(type));
                }
            }
            schemaArray.add(schemaObj);
        }
        return schemaArray;
    }

    /**
     * 特殊处理schema中的items
     * @param propsSet schema的propSet
     * @param schema schemaJson
     */
    private static void removeItems(Set<String> propsSet, JSONObject schema) {
        for (String key: propsSet) {
            JSONObject value = schema.getJSONObject(key);
            if (value.getString(REF) != null) {
                value.put(TYPE, value.getString(REF).substring(value.getString(REF).lastIndexOf("/") + 1));
                value.remove(REF);
            } else if (value.getJSONObject(ITEMS) != null) {
                JSONObject items = value.getJSONObject(ITEMS);
                if (items.getString(REF) != null) {
                    value.put(FORMAT, items.getString(REF).substring(items.getString(REF).lastIndexOf("/") + 1));
                } else {
                    value.put(FORMAT, items.getString(TYPE));
                }
                value.remove(ITEMS);
            }
        }
    }

    /**
     * 解析contact部分, 可以适当添加规则
     * @param contact contact部分
     * @param infoObj infoObj
     */
    private static void parseContract(JSONObject contact, JSONObject infoObj) {
        if (contact == null) {
            infoObj.put(Constant.NAME, NOT_WRITTEN);
            infoObj.put(Constant.URL, NOT_WRITTEN);
            infoObj.put(Constant.EMAIL, NOT_WRITTEN);
        } else {
            infoObj.put(Constant.NAME, contact.getString(Constant.NAME));
            infoObj.put(Constant.URL, contact.getString(Constant.URL));
            infoObj.put(Constant.EMAIL, contact.getString(Constant.EMAIL));
        }
    }

    /**
     * 解析swaggerJson中info部分
     * @param swaggerJson swaggerJson
     * @return 解析结果
     */
    public static JSONObject parseInfo(JSONObject swaggerJson) {
        JSONObject infoObj = new JSONObject();
        //可在此之前添加规则
        String description = swaggerJson.getString(Constant.DESCRIPTION);
        if (DEFAULT_DESCRIPTION.equals(description)) {
            infoObj.put(Constant.DESCRIPTION, NOT_WRITTEN);
        } else {
            infoObj.put(Constant.DESCRIPTION, description);
        }

        String title = swaggerJson.getString(Constant.TITLE);
        if (DEFAULT_TITLE.equals(title)) {
            infoObj.put(Constant.TITLE, NOT_WRITTEN);
        } else {
            infoObj.put(Constant.TITLE, title);
        }
        parseContract(swaggerJson.getJSONObject(Constant.CONTACT), infoObj);
        return infoObj;
    }
}
