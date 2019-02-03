package org.hiphone.eureka.monitor.service.impl;

import lombok.extern.slf4j.Slf4j;
import org.hiphone.eureka.monitor.constants.Constant;
import org.hiphone.eureka.monitor.entitys.ApplicationHistoryDto;
import org.hiphone.eureka.monitor.entitys.ResultMessage;
import org.hiphone.eureka.monitor.exception.ReturnMsg;
import org.hiphone.eureka.monitor.mapper.InstanceHistoryMapper;
import org.hiphone.eureka.monitor.service.EurekaHistoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.List;

/**
 * @author HiPhone
 */
@Slf4j
@Service
public class EurekaHistoryServiceImpl implements EurekaHistoryService {

    @Autowired
    private InstanceHistoryMapper instanceHistoryMapper;

    @Override
    public void insertInstanceHistory(ApplicationHistoryDto history) {
        log.info("insert a history info: {}", history);
        instanceHistoryMapper.insertInstanceHistory(history);
    }

    @Override
    public ResultMessage queryInstanceHistoryByPageAndLimit(Integer page, Integer limit) {
        log.info("query instance history by page and limit, page = {}, limit = {}", page, limit);
        page = (page - 1) * limit;
        return new ResultMessage(ReturnMsg.SUCCESS.getCode(),
                ReturnMsg.SUCCESS.getMessage(),
                fillHistoryMessageField(instanceHistoryMapper.queryInstanceHistoryByPageAndLimit(page, limit)));
    }

    /**
     * 填充历史记录的message字段
     * @param applicationHistoryList history的list
     * @return 带message的list
     */
    private List<ApplicationHistoryDto> fillHistoryMessageField(List<ApplicationHistoryDto> applicationHistoryList) {
        DateFormat dateFormat = new SimpleDateFormat("yyyy年MM月dd日 HH:mm:ss");
        applicationHistoryList.forEach(h -> {
            if (h.getState().equals(Constant.STATE_UP)) {
                h.setMessage("应用" + h.getApplicationName() + "于" + dateFormat.format(h.getLogTime()) + "上线");
            } else if (h.getState().equals(Constant.STATE_DOWN)) {
                h.setMessage("应用" + h.getApplicationName() + "于" + dateFormat.format(h.getLogTime()) + "下线");
            }
        });
        return applicationHistoryList;
    }

}
