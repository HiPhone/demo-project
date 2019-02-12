package org.hiphone.swagger.center.service.impl;

import lombok.extern.slf4j.Slf4j;
import org.hiphone.swagger.center.entitys.SwaggerApiDocsDto;
import org.hiphone.swagger.center.mapper.SwaggerCommonMapper;
import org.hiphone.swagger.center.service.ApiBackendService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.sql.SQLException;
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
    public String queryApiDocByServiceName(String serviceName) {
        return swaggerCommonMapper.querySwaggerDocsByServiceName(serviceName);
    }

    @Override
    public boolean insertApiInfo(SwaggerApiDocsDto swaggerApiDocsDTO) {
        swaggerCommonMapper.insertSwaggerApiInfo(swaggerApiDocsDTO);
        return true;
    }

    @Override
    public boolean updateApiInfo(SwaggerApiDocsDto swaggerApiDocsDTO) {
        swaggerCommonMapper.updateApiInfo(swaggerApiDocsDTO);
        return true;
    }
}
