package org.hiphone.eureka.monitor.service.impl;

import lombok.extern.slf4j.Slf4j;
import org.hiphone.eureka.monitor.entitys.ApplicationInstanceDto;
import org.hiphone.eureka.monitor.entitys.ResultMessage;
import org.hiphone.eureka.monitor.mapper.EurekaInstanceMapper;
import org.hiphone.eureka.monitor.service.EurekaInstanceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.LinkedHashSet;
import java.util.Set;

/**
 * @author HiPhone
 */
@Slf4j
@Service
public class EurekaInstanceServiceImpl implements EurekaInstanceService {

    @Autowired
    private EurekaInstanceMapper eurekaInstanceMapper;

    @Override
    public Set<ApplicationInstanceDto> queryInstancesByStateAndClusterId(int state, String clusterId) {
        try {
            return eurekaInstanceMapper.queryInstancesByStateAndClusterId(state, clusterId);
        } catch (Exception e) {
            log.error("Database connect get error! please check it: {}", e.getMessage());
        }
        return new LinkedHashSet<>();
    }

    @Override
    public void insertOrUpdateInstance(ApplicationInstanceDto instance) {
        try {
            eurekaInstanceMapper.insertOrUpdateDownInstance(instance);
        } catch (Exception e) {
            log.error("Database connect get error! please check it: {}", e.getMessage());
        }
    }

    @Override
    public ResultMessage queryInstancesByClusterIdOrApplicationNameOrState(String clusterId, String applicationName, Integer state) {
        try {
            eurekaInstanceMapper.queryInstancesByClusterIdOrApplicationNameOrState(clusterId, applicationName, state);
        } catch (Exception e) {
            log.error("Database connect get error! please check it: {}", e.getMessage());
        }
    }
}
