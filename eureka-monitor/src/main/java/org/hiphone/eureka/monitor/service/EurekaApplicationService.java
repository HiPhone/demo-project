package org.hiphone.eureka.monitor.service;

import org.hiphone.eureka.monitor.entitys.ApplicationDto;
import org.hiphone.eureka.monitor.entitys.ApplicationInstanceDto;
import org.hiphone.eureka.monitor.entitys.ResultMessage;

import java.util.List;
import java.util.Set;

/**
 * @author HiPhone
 */
public interface EurekaApplicationService {

    /**
     * 通过集群id获取application信息
     * @param clusterId 集群id
     * @return application信息
     */
    Set<ApplicationDto> queryApplicationsByClusterId(String clusterId);

    /**
     * 更新application的信息
     * @param instancesSet instance的set
     */
    void insertOrUpdateApplicationInfo(Set<ApplicationInstanceDto> instancesSet);

    /**
     * 获取所有clusterId的list
     * @return resultMessage
     */
    ResultMessage queryDistinctClusterId();
}
