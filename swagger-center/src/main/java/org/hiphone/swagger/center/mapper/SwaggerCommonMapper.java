package org.hiphone.swagger.center.mapper;

import org.apache.ibatis.annotations.Param;
import org.hiphone.swagger.center.entitys.SwaggerApiDocsDto;

import java.util.List;

/**
 * @author HiPhone
 */
public interface SwaggerCommonMapper {

    /**
     * 通过serviceName判断数据库是否已存在api信息
     * @param serviceName 服务名称
     * @return 存在则返回服务名称，不存在则返回null
     */
    String isInfoExist(@Param("serviceName") String serviceName);

    /**
     * 获取数据库中所有的serviceName
     * @return 所有service的名称
     */
    List<String> queryAllServiceNames();

    /**
     * 通过服务名称获取swagger的api-docs
     * @param serviceName 服务名称
     * @return service对应的api-docs
     */
    String querySwaggerDocsByServiceName(@Param("serviceName") String serviceName);

    /**
     * 向数据库中添加swagger api-docs
     * @param swaggerApiDocsDTO swaggerApiDTO
     * @return 插入结果
     */
    Integer insertSwaggerApiInfo(SwaggerApiDocsDto swaggerApiDocsDTO);

    /**
     * 更新数据库中的api信息
     * @param swaggerApiDocsDTO swagger api 封装类
     * @return 更新结果
     */
    Integer updateApiInfo(SwaggerApiDocsDto swaggerApiDocsDTO);

    /**
     * 获取所有的serviceId及其对应的api-docs
     * @return list
     */
    List<SwaggerApiDocsDto> queryAllServiceNamesAndApiDocs();
}
