package org.hiphone.eureka.monitor.service;

import org.apache.ibatis.annotations.Param;
import org.hiphone.eureka.monitor.entitys.ApplicationHistoryDto;

/**
 * @author HiPhone
 */
public interface EurekaHistoryService {

    /**
     * 向数据库中新增一条历史记录
     * @param history 历史记录封装
     */
    void insertInstanceHistory(@Param("history")ApplicationHistoryDto history);
}
