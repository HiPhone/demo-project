package org.hiphone.eureka.monitor.mapper;

import org.apache.ibatis.annotations.Param;
import org.hiphone.eureka.monitor.entitys.ApplicationHistoryDto;

import java.util.List;

/**
 * @author HiPhone
 */
public interface InstanceHistoryMapper {

    /**
     * 向数据库中插入一条历史信息
     * @param history 历史信息封装类
     * @return 操作的结果
     */
    Integer insertInstanceHistory(@Param("history") ApplicationHistoryDto history);

    /**
     * 通过页数和一页的个数查询历史记录
     * @param page 页数
     * @param limit 一页的数据量
     * @return history的set
     */
    List<ApplicationHistoryDto> queryInstanceHistoryByPageAndLimit(@Param("page") Integer page, @Param("limit") Integer limit);
}
