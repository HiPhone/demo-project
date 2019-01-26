package org.hiphone.eureka.monitor.mapper;

import org.apache.ibatis.annotations.Param;
import org.hiphone.eureka.monitor.entitys.ApplicationDto;
import org.hiphone.eureka.monitor.entitys.ApplicationInstanceDto;

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

    /**
     * 将applicationDtoSet插入数据库中
     * @param applicationDtoSet applicationDto Set
     */
    Integer batchInsertApplications(Set<ApplicationDto> applicationDtoSet);

    /**
     * 通过instanceSet 插入或者更新对应的application数据
     * @param instanceSets instance的set
     */
    Integer insertOrUpdateApplicationInfo(Set<ApplicationInstanceDto> instanceSets);
}
