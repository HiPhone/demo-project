package org.hiphone.eureka.monitor.service.impl;

import lombok.extern.slf4j.Slf4j;
import org.hiphone.eureka.monitor.constants.Constant;
import org.hiphone.eureka.monitor.entitys.ApplicationInstanceDto;
import org.hiphone.eureka.monitor.entitys.ResultMessage;
import org.hiphone.eureka.monitor.exception.ReturnMsg;
import org.hiphone.eureka.monitor.mapper.EurekaInstanceMapper;
import org.hiphone.eureka.monitor.service.EurekaInstanceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;

/**
 * @author HiPhone
 */
@Slf4j
@Service
public class EurekaInstanceServiceImpl implements EurekaInstanceService {

    @Autowired
    private EurekaInstanceMapper eurekaInstanceMapper;

    @Override
    public Set<ApplicationInstanceDto> queryInstancesByStateAndClusterId(int state, String clusterId) {
        log.info("query instance which clusterId is {} and state is {}", clusterId, state);
        return eurekaInstanceMapper.queryInstancesByStateAndClusterId(state, clusterId);
    }

    @Override
    public void insertOrUpdateInstance(ApplicationInstanceDto instance) {
        log.info("try to insert a application instance: {}", instance);
        eurekaInstanceMapper.insertOrUpdateDownInstance(instance);
    }

    @Override
    public ResultMessage queryInstancesByClusterIdOrApplicationNameOrState(String clusterId, String applicationName, Integer state) {
        log.info("try to query instances which clusterId is {}, applicationName is {}, state is {}", clusterId, applicationName, state);
        return new ResultMessage(ReturnMsg.SUCCESS.getCode(),
                ReturnMsg.SUCCESS.getMessage(),
                fillDurationField(eurekaInstanceMapper.queryInstancesByClusterIdOrApplicationNameOrState(clusterId, applicationName, state)));
    }

    /**
     * 填充duration字段
     * @param applicationInstanceList instance的集合
     * @return 填充之后的instance集合
     */
    private List<ApplicationInstanceDto> fillDurationField(List<ApplicationInstanceDto> applicationInstanceList) {

        applicationInstanceList.forEach(app -> {
            long duration;
            if (app.getCurrentState().equals(Constant.STATE_UP)) {
                app.setDownDuration(Constant.NOT_AVAILABLE);
                duration = (System.currentTimeMillis() - app.getRegisterTime().getTime());
                app.setUpDuration(conventDurationToString(duration));
            } else if (app.getCurrentState().equals(Constant.STATE_DOWN)) {
                app.setUpDuration(Constant.NOT_AVAILABLE);
                duration = (System.currentTimeMillis() - app.getDownTime().getTime());
                app.setDownDuration(conventDurationToString(duration));
            }
        });

        return applicationInstanceList;
    }

    /**
     * 转换duration的long 为字符串
     * @param duration 时间间隔
     * @return 时间间隔的字符串
     */
    private String conventDurationToString(long duration) {
        StringBuilder durationString = new StringBuilder();
        long day = duration / (24 * 60 * 60 * 1000);
        long hour = (duration / (60 * 60 * 1000) - day * 24);
        long minutes = ((duration / (60 * 1000)) - day * 24 * 60 - hour * 60);

        durationString.append(day + "days ");

        if (hour == 0) {
            durationString.append("00:");
        } else {
            if (hour < 10) {
                durationString.append("0" + hour + ":");
            } else {
                durationString.append(hour + ":");
            }
        }

        if (minutes > 0 && minutes < 10) {
            durationString.append("0" + minutes);
        } else if (minutes <= 0) {
            durationString.append("00");
        } else {
            durationString.append(minutes);
        }
        return durationString.toString();
    }
}
