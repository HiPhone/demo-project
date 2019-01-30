package org.hiphone.eureka.monitor.mapper;

import org.apache.ibatis.annotations.Param;
import org.hiphone.eureka.monitor.entitys.ApplicationInstanceDto;

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
    Set<ApplicationInstanceDto> queryInstancesByStateAndClusterId(@Param("state") int state, @Param("clusterId") String clusterId);


    /**
     * 插入一条instance数据或者更新一条instance数据
     * @param instance instance信息封装
     */
    Integer insertOrUpdateDownInstance(@Param("instance") ApplicationInstanceDto instance);


}
