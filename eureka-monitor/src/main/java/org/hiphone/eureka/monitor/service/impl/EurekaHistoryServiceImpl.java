package org.hiphone.eureka.monitor.service.impl;

import lombok.extern.slf4j.Slf4j;
import org.hiphone.eureka.monitor.entitys.ApplicationHistoryDto;
import org.hiphone.eureka.monitor.mapper.InstanceHistoryMapper;
import org.hiphone.eureka.monitor.service.EurekaHistoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author HiPhone
 */
@Slf4j
@Service
public class EurekaHistoryServiceImpl implements EurekaHistoryService {

    @Autowired
    private InstanceHistoryMapper instanceHistoryMapper;

    @Override
    public void insertInstanceHistory(ApplicationHistoryDto history) {
        try {
            instanceHistoryMapper.insertInstanceHistory(history);
        } catch (Exception e) {
            log.error("Database connect get error! please check it: {}", e.getMessage());
        }
    }
}
