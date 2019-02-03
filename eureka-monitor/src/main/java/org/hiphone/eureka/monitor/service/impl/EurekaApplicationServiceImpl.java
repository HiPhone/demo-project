package org.hiphone.eureka.monitor.service.impl;

import lombok.extern.slf4j.Slf4j;
import org.hiphone.eureka.monitor.entitys.ApplicationDto;
import org.hiphone.eureka.monitor.entitys.ApplicationInstanceDto;
import org.hiphone.eureka.monitor.entitys.ResultMessage;
import org.hiphone.eureka.monitor.exception.ReturnMsg;
import org.hiphone.eureka.monitor.mapper.EurekaApplicationMapper;
import org.hiphone.eureka.monitor.service.EurekaApplicationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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
        log.info("start to query distinct clusterId......");
        return new ResultMessage(ReturnMsg.SUCCESS.getCode(),
                ReturnMsg.SUCCESS.getMessage(),
                eurekaApplicationMapper.queryDistinctClusterId());
    }

    @Override
    public Set<ApplicationDto> queryApplicationsByClusterId(String clusterId) {
        log.info("query application's info by clusterId which is {}", clusterId);
        return eurekaApplicationMapper.queryApplicationsByClusterId(clusterId);
    }

    @Override
    public void insertOrUpdateApplicationInfo(Set<ApplicationInstanceDto> instanceSets) {
        log.info("batch insert or update application info by instanceSet which size is {}", instanceSets.size());
        eurekaApplicationMapper.insertOrUpdateApplicationInfo(instanceSets);
    }
}
