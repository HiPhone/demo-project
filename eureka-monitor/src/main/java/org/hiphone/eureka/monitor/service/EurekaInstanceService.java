package org.hiphone.eureka.monitor.service;

import org.hiphone.eureka.monitor.entitys.ApplicationInstanceDto;
import org.hiphone.eureka.monitor.entitys.ResultMessage;

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
     * 插入或者更新已经down的实例
     * @param instance 实例信息
     */
    void insertOrUpdateInstance(ApplicationInstanceDto instance);

    /**
     * 通过clusterId 或者applicationName或者state获取instance实例
     * @param clusterId 集群id
     * @param applicationName 应用名称
     * @param state 状态
     * @return resultMessage
     */
    ResultMessage queryInstancesByClusterIdOrApplicationNameOrState(String clusterId, String applicationName, Integer state);
}
