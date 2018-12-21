package org.hiphone.swagger.center.service.impl;

import com.alibaba.fastjson.JSONObject;
import lombok.extern.slf4j.Slf4j;
import org.hiphone.swagger.center.entitys.SwaggerApiDocsDTO;
import org.hiphone.swagger.center.mapper.SwaggerCommonMapper;
import org.hiphone.swagger.center.service.ApiBackendService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * @author HiPhone
 */
@Slf4j
@Service
public class ApiBackendServiceImpl implements ApiBackendService {

    @Autowired
    private SwaggerCommonMapper swaggerCommonMapper;

    @Override
    public boolean isApiInfoExist(String serviceName) {
        return StringUtils.isEmpty(swaggerCommonMapper.isInfoExist(serviceName));
    }

    @Override
    public List<String> queryAllServiceNames() {
        List<String> serviceNames;
        try {
            serviceNames = swaggerCommonMapper.queryAllServiceNames();
        } catch (Exception e) {
            log.warn("The operation to database get error! {}", e);
            serviceNames = new ArrayList<>();
        }
        return serviceNames;
    }

    @Override
    public JSONObject queryApiDocByServiceName(String serviceName) {
        JSONObject apiDocs = null;
        try {
            apiDocs = swaggerCommonMapper.querySwaggerDocsByServiceId(serviceId);
        } catch (Exception e) {
            log.warn("The operation to database get error! {}", e);
        }
        return apiDocs;
    }

    @Override
    public boolean insertApiInfo(SwaggerApiDocsDTO swaggerApiDocsDTO) {
        try {
            swaggerCommonMapper.insertSwaggerApiInfo(swaggerApiDocsDTO);
            return true
        } catch (Exception e) {
            log.warn("The operation to database get error! {}", e);
        }
        return false;
    }

    @Override
    public boolean updateApiInfo(SwaggerApiDocsDTO swaggerApiDocsDTO) {
        try {
            swaggerCommonMapper.updateApiInfo(swaggerApiDocsDTO);
            return true;
        } catch (Exception e) {
            log.warn("The operation to database get error! {}", e);
        }
        return false;
    }
}
