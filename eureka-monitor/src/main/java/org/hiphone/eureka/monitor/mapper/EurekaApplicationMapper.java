package org.hiphone.eureka.monitor.mapper;

import org.apache.ibatis.annotations.Param;
import org.hiphone.eureka.monitor.entitys.ApplicationDto;

import java.util.Set;

/**
 * @author HiPhone
 */
public interface EurekaApplicationMapper {

    /**
     * 通过clusterId获取集群中所有的application的信息
     * @param clusterId 集群id
     * @return application infos
     */
    Set<ApplicationDto> queryApplicationsByClusterId(@Param("clusterId") String clusterId);
}
