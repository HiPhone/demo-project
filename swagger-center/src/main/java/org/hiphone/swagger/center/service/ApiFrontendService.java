package org.hiphone.swagger.center.service;

import org.hiphone.swagger.center.entitys.ResultMessage;

import java.io.IOException;

/**
 * @author HiPhone
 */
public interface ApiFrontendService {

    /**
     * 获取所有的serviceName列表
     * @return resultMessage
     */
    ResultMessage queryAllServiceNames();

    /**
     * 通过serviceName获取原生的api-docs
     * @param serviceName 服务的名称
     * @return resultMessage
     */
    ResultMessage queryApiDocsByServiceName(String serviceName) throws IOException;

    /**
     * 通过serviceName获取简化的api-docs
     * @param serviceName 服务的名称
     * @return 简化之后的结果
     */
    ResultMessage querySimplifyApiDocs(String serviceName) throws IOException;

}
