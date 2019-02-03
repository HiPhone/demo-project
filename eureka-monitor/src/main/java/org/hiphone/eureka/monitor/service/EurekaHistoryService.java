package org.hiphone.eureka.monitor.service;

import org.hiphone.eureka.monitor.entitys.ApplicationHistoryDto;
import org.hiphone.eureka.monitor.entitys.ResultMessage;

/**
 * @author HiPhone
 */
public interface EurekaHistoryService {

    /**
     * 向数据库中新增一条历史记录
     * @param history 历史记录封装
     */
    void insertInstanceHistory(ApplicationHistoryDto history);

    /**
     * 通过page和limit到数据库查询历史记录
     * @param page 页数
     * @param limit 一页的数据量
     * @return 历史记录的List
     */
    ResultMessage queryInstanceHistoryByPageAndLimit(Integer page, Integer limit);
}
