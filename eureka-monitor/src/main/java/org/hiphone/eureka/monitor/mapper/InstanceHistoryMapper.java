package org.hiphone.eureka.monitor.mapper;

import org.apache.ibatis.annotations.Param;
import org.hiphone.eureka.monitor.entitys.ApplicationHistoryDto;

public interface InstanceHistoryMapper {

    /**
     * 向数据库中插入一条历史信息
     * @param history 历史信息封装类
     */
    Integer insertInstanceHistory(@Param("history") ApplicationHistoryDto history);
}
