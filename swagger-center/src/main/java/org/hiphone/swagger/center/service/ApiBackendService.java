package org.hiphone.swagger.center.service;

import org.hiphone.swagger.center.entitys.SwaggerApiDocsDto;

import java.util.List;

/**
 * @author HiPhone
 */
public interface ApiBackendService {

    /**
     * 判断api信息是否已经存在于数据库中
     * @param serviceName 服务的名称
     * @return true or false
     */
    boolean isApiInfoExist(String serviceName);

    /**
     * 获取所有已经入库的serviceName
     * @return 所有serviceName的列表
     */
    List<String> queryAllServiceNames();

    /**
     * 通过serviceName获取swagger api-docs
     * @param serviceName 服务的名称
     * @return 对应的swagger api-docs
     */
    String queryApiDocByServiceName(String serviceName);

    /**
     * 在数据库中新增一条swagger api-docs
     * @param swaggerApiDocsDTO api info
     * @return true or false
     */
    boolean insertApiInfo(SwaggerApiDocsDto swaggerApiDocsDTO);

    /**
     * 更新数据库中的api信息
     * @param swaggerApiDocsDTO api info
     * @return true or false
     */
    boolean updateApiInfo(SwaggerApiDocsDto swaggerApiDocsDTO);

}
