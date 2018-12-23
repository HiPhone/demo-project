package org.hiphone.swagger.center.service.impl;

import lombok.extern.slf4j.Slf4j;
import org.hiphone.swagger.center.constants.ReturnMsg;
import org.hiphone.swagger.center.entitys.ResultMessage;
import org.hiphone.swagger.center.mapper.SwaggerCommonMapper;
import org.hiphone.swagger.center.service.ApiFrontendService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author HiPHone
 */
@Slf4j
@Service
public class ApiFrontendServiceImpl implements ApiFrontendService {

    @Autowired
    private SwaggerCommonMapper swaggerCommonMapper;

    @Override
    public ResultMessage queryAllServiceNames() {
        ResultMessage resultMessage;
        try {
            resultMessage = new ResultMessage(ReturnMsg.SUCCESS.getCode(),
                    ReturnMsg.SUCCESS.getMessage(),
                    swaggerCommonMapper.queryAllServiceNames());
        } catch (Exception e) {
            log.warn("The operation to database get error!", e);
            resultMessage = new ResultMessage(ReturnMsg.SQL_ERROR.getCode(),
                    ReturnMsg.SQL_ERROR.getMessage(),
                    e.getMessage());
        }
        return resultMessage;
    }

    @Override
    public ResultMessage queryApiDocsByServiceName(String serviceName) {
        ResultMessage resultMessage;
        try {
            resultMessage = new ResultMessage(ReturnMsg.SUCCESS.getCode(),
                    ReturnMsg.SUCCESS.getMessage(),
                    swaggerCommonMapper.querySwaggerDocsByServiceName(serviceName));
        } catch (Exception e) {
            log.warn("The operation to database get error!", e);
            resultMessage = new ResultMessage(ReturnMsg.SQL_ERROR.getCode(),
                    ReturnMsg.SQL_ERROR.getMessage(),
                    e.getMessage());
        }
        return resultMessage;
    }
}
