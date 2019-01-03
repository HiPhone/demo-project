package org.hiphone.swagger.center.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.hiphone.swagger.center.constants.ReturnMsg;
import org.hiphone.swagger.center.entitys.ResultMessage;
import org.hiphone.swagger.center.entitys.SwaggerApiDocsDto;
import org.hiphone.swagger.center.mapper.SwaggerCommonMapper;
import org.hiphone.swagger.center.service.ApiFrontendService;
import org.hiphone.swagger.center.service.StandardCheckService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

/**
 * @author HiPHone
 */
@Slf4j
@Service
public class ApiFrontendServiceImpl implements ApiFrontendService {

    private static final String NAME = "name";
    private static final String TITLE = "title";
    private static final String API_SUM = "sum";
    private static final String NOT_STANDARD_NUM = "not_standard_num";
    private static final String UPDATE_TIME = "update_time";
    private static final String HOST = "host";
    private static final String UN_REST_APIS = "un_rest_apis";
    private static final ObjectMapper objectMapper = new ObjectMapper();

    @Autowired
    private SwaggerCommonMapper swaggerCommonMapper;

    @Autowired
    private StandardCheckService standardCheckService;

    //TODO
    @Override
    public ResultMessage querySimplifyApiDocs(String serviceName) {
        ResultMessage resultMessage;
        try {
            Map<String, Object> simlifyApiDocs = new LinkedHashMap<>();
            SwaggerApiDocsDto info = swaggerCommonMapper.querySwaggerInfoByServiceName(serviceName);
            JSONObject apiDocs = objectMapper.readValue(info.getSwaggerApiDocs(), JSONObject.class);
            simlifyApiDocs.put(HOST, apiDocs.getString(HOST));
            simlifyApiDocs.put(UPDATE_TIME, info.getUpdateTime());
            simlifyApiDocs.put(NOT_STANDARD_NUM, standardCheckService.getNotStandardNum(apiDocs));
            resultMessage = new ResultMessage(ReturnMsg.SUCCESS.getCode(),
                    ReturnMsg.SUCCESS.getMessage(),
                    simlifyApiDocs);
        } catch (Exception e) {
            log.warn("The operation to database get error!", e);
            resultMessage = new ResultMessage(ReturnMsg.SQL_ERROR.getCode(),
                    ReturnMsg.SQL_ERROR.getMessage(),
                    e.getMessage());
        }
        return resultMessage;
    }

    @Override
    public ResultMessage queryAllServiceNames() {
        ResultMessage resultMessage;
        try {
            resultMessage = new ResultMessage(ReturnMsg.SUCCESS.getCode(),
                    ReturnMsg.SUCCESS.getMessage(),
                    fillDataWithName(swaggerCommonMapper.queryAllServiceNames()));
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
                    objectMapper.readValue(swaggerCommonMapper.querySwaggerDocsByServiceName(serviceName), JSONObject.class));
        } catch (Exception e) {
            log.warn("The operation to database get error!", e);
            resultMessage = new ResultMessage(ReturnMsg.SQL_ERROR.getCode(),
                    ReturnMsg.SQL_ERROR.getMessage(),
                    e.getMessage());
        }
        return resultMessage;
    }

    /**
     * 修饰serviceName，方便前端处理
     * @param serviceNameList 数据库中获取的服务名称的列表
     * @return 修饰之后的数据
     */
    private List<Map<String, Object>> fillDataWithName(List<String> serviceNameList) {
        List<Map<String, Object>> result = new LinkedList<>();
        for (int i = 1; i <= serviceNameList.size(); i++) {
            Map<String, Object> dataMap = new HashMap<>();
            dataMap.put(NAME, i);
            dataMap.put(TITLE, serviceNameList.get(i - 1));
            result.add(dataMap);
        }
        return result;
    }
}
