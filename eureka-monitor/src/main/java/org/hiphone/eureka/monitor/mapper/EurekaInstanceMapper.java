package org.hiphone.eureka.monitor.mapper;

import org.apache.ibatis.annotations.Param;
import org.hiphone.eureka.monitor.entitys.ServiceInstanceDto;

import java.util.Set;

/**
 * @author HiPone
 */
public interface EurekaInstanceMapper {

    /**
     * 根据传入的状态参数获取该状态下所有的instances
     * @param state 状态
     * @param clusterId 集群id
     * @return instances集合
     */
    Set<ServiceInstanceDto> queryInstancesByState(@Param("state") int state, String clusterId);
}
