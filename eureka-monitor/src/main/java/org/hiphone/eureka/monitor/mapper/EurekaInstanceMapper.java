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
    Set<ApplicationInstanceDto> queryInstancesByState(@Param("state") int state, @Param("clusterId") String clusterId);

    /**
     * 更新instance的状态
     * @param instance instance信息的封装
     * @param state 更新后的状态
     */
    void updateInstanceState(@Param("instance") ApplicationInstanceDto instance, @Param("state") Integer state);

    /**
     * 插入一条instance数据或者更新一条instance数据
     * @param instance instance信息封装
     */
    void insertOrUpdateDownInstance(@Param("instance") ApplicationInstanceDto instance);
}
