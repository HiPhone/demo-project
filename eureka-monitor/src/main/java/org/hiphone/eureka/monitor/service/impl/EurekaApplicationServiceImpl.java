package org.hiphone.eureka.monitor.service.impl;

import lombok.extern.slf4j.Slf4j;
import org.hiphone.eureka.monitor.constants.ReturnMsg;
import org.hiphone.eureka.monitor.entitys.ApplicationDto;
import org.hiphone.eureka.monitor.entitys.ApplicationInstanceDto;
import org.hiphone.eureka.monitor.entitys.ResultMessage;
import org.hiphone.eureka.monitor.mapper.EurekaApplicationMapper;
import org.hiphone.eureka.monitor.service.EurekaApplicationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.LinkedHashSet;
import java.util.Set;

/**
 * @author HiPhone
 */
@Slf4j
@Service
public class EurekaApplicationServiceImpl implements EurekaApplicationService {

    @Autowired
    private EurekaApplicationMapper eurekaApplicationMapper;

    @Override
    public ResultMessage queryDistinctClusterId() {
        ResultMessage resultMessage = null;
        try {
            resultMessage = new ResultMessage(ReturnMsg.SUCCESS.getCode(),
                    ReturnMsg.SUCCESS.getMessage(),
                    eurekaApplicationMapper.queryDistinctClusterId());
        } catch (Exception e) {
            log.error("Database connect get error! please check it: {}", e.getMessage());
        }
        return resultMessage;
    }

    @Override
    public Set<ApplicationDto> queryApplicationsByClusterId(String clusterId) {
        try {
            return eurekaApplicationMapper.queryApplicationsByClusterId(clusterId);
        } catch (Exception e) {
            log.error("Database connect get error! please check it: {}", e.getMessage());
        }
        return new LinkedHashSet<>();
    }

    @Override
    public void insertOrUpdateApplicationInfo(Set<ApplicationInstanceDto> instanceSets) {
        try {
            eurekaApplicationMapper.insertOrUpdateApplicationInfo(instanceSets);
        } catch (Exception e) {
            log.error("Database connect get error! please check it: {}", e.getMessage());
        }
    }
}
