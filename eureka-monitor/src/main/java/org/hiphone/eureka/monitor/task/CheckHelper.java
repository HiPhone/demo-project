package org.hiphone.eureka.monitor.task;

import com.google.common.collect.Sets;
import lombok.extern.slf4j.Slf4j;
import org.hiphone.eureka.monitor.constants.Constant;
import org.hiphone.eureka.monitor.entitys.ApplicationDto;
import org.hiphone.eureka.monitor.entitys.ApplicationHistoryDto;
import org.hiphone.eureka.monitor.entitys.ApplicationInstanceDto;
import org.hiphone.eureka.monitor.service.EurekaApplicationService;
import org.hiphone.eureka.monitor.service.EurekaHistoryService;
import org.hiphone.eureka.monitor.service.EurekaInstanceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

/**
 * @author HiPhone
 */
@Slf4j
@Component
public class CheckHelper {

    /**
     * 存放instance缓存信息的map
     */
    private Map<String ,Set<ApplicationInstanceDto>> clusterInstanceCacheMap = new ConcurrentHashMap<>();

    /**
     * 存放Application缓存信息的map
     */
    private Map<String, Set<ApplicationDto>> clusterApplicationCacheMap = new ConcurrentHashMap<>();

    @Autowired
    private EurekaInstanceService eurekaInstanceService;

    @Autowired
    private EurekaApplicationService eurekaApplicationService;

    @Autowired
    private EurekaHistoryService eurekaHistoryService;

    /**
     * 对比instance新数据与旧数据，返回他们之间的不同
     * @param clusterId 集群id
     * @param eurekaInstanceSet eurekaInstanceSet
     * @return 新旧数据的不同
     */
    public Set<ApplicationInstanceDto> checkInstances(String clusterId, Set<ApplicationInstanceDto> eurekaInstanceSet) {
        Set<ApplicationInstanceDto> instanceDifferenceSet = new LinkedHashSet<>();

        Set<ApplicationInstanceDto> oldInstanceSet = clusterInstanceCacheMap.get(clusterId) == null ?
                eurekaInstanceService.queryInstancesByStateAndClusterId(Constant.STATE_UP, clusterId) : clusterInstanceCacheMap.get(clusterId);

        log.info("The old instances data of cluster {} initialize success! the size is {}", clusterId, oldInstanceSet.size());

        //eurekaInstances - oldInstances = upInstances
        Set<ApplicationInstanceDto> upInstancesSet = Sets.difference(eurekaInstanceSet, oldInstanceSet);

        //oldInstance - eurekaInstances = downInstances
        Set<ApplicationInstanceDto> downInstancesSet = Sets.difference(oldInstanceSet, eurekaInstanceSet);

        downInstancesSet.forEach(d -> {
            d.setCurrentState(1);
        });

        instanceDifferenceSet.addAll(upInstancesSet);
        instanceDifferenceSet.addAll(downInstancesSet);
        return instanceDifferenceSet;
    }

    /**
     * 对面application新旧数据，返回差异
     * @param clusterId 集群id
     * @param differentInstanceSet instance的差异集合
     * @return application的差异集合
     */
    public Set<ApplicationDto> checkApplications(String clusterId, Set<ApplicationInstanceDto> differentInstanceSet) {
        Set<ApplicationDto> newApplicationsSet = new LinkedHashSet<>();
        differentInstanceSet.forEach(instance -> {
            ApplicationDto application = new ApplicationDto();
            application.setClusterId(clusterId);
            application.setApplicationName(instance.getApplicationName());
            newApplicationsSet.add(application);
        });

        Set<ApplicationDto> oldApplicationsSet = clusterApplicationCacheMap.get(clusterId) == null ?
                eurekaApplicationService.queryApplicationsByClusterId(clusterId) : clusterApplicationCacheMap.get(clusterId);
        return Sets.difference(newApplicationsSet, oldApplicationsSet);
    }

    /**
     * 更新数据库的数据
     * @param clusterId 集群id
     * @param differentApplicationSet application
     * @param differentInstanceSet applicationInstances
     */
    public void updateDataToDatabase(String clusterId, Set<ApplicationDto> differentApplicationSet, Set<ApplicationInstanceDto> differentInstanceSet) {
        //eurekaApplicationSet不为空，有新的application注册，插入数据
        if (!differentApplicationSet.isEmpty()) {
            eurekaApplicationService.batchSaveApplications(differentApplicationSet);
        }

        Set<ApplicationInstanceDto> dbApplicationInstanceSet = eurekaInstanceService.queryInstancesByStateAndClusterId(Constant.STATE_UP, clusterId);
        Set<ApplicationInstanceDto> tmpApplicationInstances = new LinkedHashSet<>(dbApplicationInstanceSet);

        if (!dbApplicationInstanceSet.isEmpty()) {
            //eureka remove = eureka -db, db remove - db - eureka
            dbApplicationInstanceSet.removeAll(tmpApplicationInstances);
            differentInstanceSet.removeAll(tmpApplicationInstances);
        }

        //db已经有的数据，已经down, 更新state为down， 并记录down的历史记录
        if (!differentInstanceSet.isEmpty() && dbApplicationInstanceSet.isEmpty()) {
            for (ApplicationInstanceDto dbInstance : dbApplicationInstanceSet) {
                for (ApplicationInstanceDto eurekaInstance : differentInstanceSet) {
                    if (dbInstance.getInstanceId().equals(eurekaInstance.getInstanceId())) {
                        eurekaInstance.setDownTime(new Date());
                        eurekaInstanceService.updateInstanceState(eurekaInstance, Constant.STATE_DOWN);
                        eurekaHistoryService.insertInstanceHistory(constructHistoryDto(eurekaInstance, Constant.STATE_DOWN));
                        log.info("Save a history of instance with state UP to DOWN, instanceId is {}, clusterId is {}", eurekaInstance.getInstanceId(), eurekaInstance.getClusterId());
                    }
                }
            }
        }
        //获取state 为 up的instance
        Set<ApplicationInstanceDto> upInstanceSet = differentInstanceSet.stream()
                .filter(instance -> instance.getCurrentState().equals(Constant.STATE_UP))
                .collect(Collectors.toSet());

        //db没有的数据， 插入数据库或者更新数据库
        if (!upInstanceSet.isEmpty()) {
            for (ApplicationInstanceDto instance : upInstanceSet) {
                eurekaInstanceService.insertOrUpdateDownInstance(instance);
                ApplicationHistoryDto history = constructHistoryDto(instance, Constant.STATE_UP);
                eurekaHistoryService.insertInstanceHistory(history);
                log.info("Success to save a history of instance insert or update from DOWN to UP, which instance id is {}", instance.getInstanceId());
            }
        }

        //update application
    }

    /**
     * 构造历史记录信息
     * @param instance 待处理的applicationInstance
     * @param state instance当前的状态
     * @return history
     */
    private ApplicationHistoryDto constructHistoryDto(ApplicationInstanceDto instance, Integer state) {
        ApplicationHistoryDto history = new ApplicationHistoryDto();
        history.setClusterId(instance.getClusterId());
        history.setApplicationName(instance.getApplicationName());
        history.setIpAddress(instance.getIpAddress() + ":" + instance.getServicePort());
        history.setState(state);
        return history;
    }
}
