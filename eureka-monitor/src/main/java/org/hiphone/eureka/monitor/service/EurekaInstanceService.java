package org.hiphone.eureka.monitor.service;

import org.hiphone.eureka.monitor.entitys.ServiceInstanceDto;

import java.util.Set;

/**
 * @author HiPhone
 */
public interface EurekaInstanceService  {

    /**
     * 查询数据库中所有状态为up的
     * @param state 状态
     * @param clusterId 集群id
     * @return 该状态的集合
     */
    Set<ServiceInstanceDto> queryInstancesByStateAndClusterId(int state, String clusterId);
}
