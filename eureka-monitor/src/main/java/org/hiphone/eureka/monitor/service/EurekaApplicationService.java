package org.hiphone.eureka.monitor.service;

import org.hiphone.eureka.monitor.entitys.ApplicationDto;
import org.hiphone.eureka.monitor.entitys.ApplicationInstanceDto;

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
     * 批量插入applications
     * @param applications application的set
     */
    void batchSaveApplications(Set<ApplicationDto> applications);

    /**
     * 更新application的信息
     * @param instancesSet instance的set
     */
    void insertOrUpdateApplicationInfo(Set<ApplicationInstanceDto> instancesSet);
}