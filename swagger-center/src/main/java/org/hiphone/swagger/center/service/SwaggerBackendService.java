package org.hiphone.swagger.center.service;

import com.alibaba.fastjson.JSONObject;
import org.hiphone.swagger.center.entitys.SwaggerApiDocsDTO;

import java.util.List;

/**
 * @author HiPhone
 */
public interface SwaggerBackendService {

    /**
     * 判断swagger api信息是否已经存在
     * @param serviceName 服务的名称
     * @return true or false
     */
    boolean isApiExits(String serviceName);

    /**
     * 获取所有已经入库的serviceName
     * @return 所有serviceName的列表
     */
    List<String> queryAllServiceNames();

    /**
     * 通过serviceName获取swagger api-docs
     * @param serviceName 服务名称
     * @return swagger api-docs
     */
    JSONObject querySwaggerDocsByServiceName(String serviceName);

    /**
     * 新增一条swagger api-docs
     * @param swaggerApiDocsDTO
     */
    void insertSwaggerApiInfo(SwaggerApiDocsDTO swaggerApiDocsDTO);


}
