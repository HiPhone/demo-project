package org.hiphone.eureka.monitor.service;

import org.hiphone.eureka.monitor.entitys.ApplicationInstanceDto;

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
    Set<ApplicationInstanceDto> queryInstancesByStateAndClusterId(int state, String clusterId);

    /**
     * 更新instance的状态
     * @param instance instance的信息
     * @param newState 新的状态
     */
    void updateInstanceState(ApplicationInstanceDto instance, Integer newState);

    /**
     * 插入或者更新已经down的实例
     * @param instance 实例信息
     */
    void insertOrUpdateDownInstance(ApplicationInstanceDto instance);
}
