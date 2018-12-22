package org.hiphone.swagger.center.service;

import com.alibaba.fastjson.JSONObject;

import java.util.Map;

/**
 * @author HiPhone
 */
public interface StandardCheckService {

    /**
     * 获取swagger api-docs对接不规范的数目
     * @param swaggerApiDocs swagger api-docs
     * @return 对接不规范的数目
     */
    Integer getNotStandardNum(JSONObject swaggerApiDocs);

//    /**
//     * 获取对接不规范的详细信息
//     * @param swaggerJsonObject swagger api-docs
//     * @return 对接不规范的详细信息
//     */
//    Map<String, Object> getNotStandardDetails(JSONObject swaggerJsonObject);
}
