package org.hiphone.eureka.monitor.task;

import com.google.common.collect.Sets;
import lombok.extern.slf4j.Slf4j;
import org.hiphone.eureka.monitor.constants.Constant;
import org.hiphone.eureka.monitor.entitys.ApplicationHistoryDto;
import org.hiphone.eureka.monitor.entitys.ApplicationInstanceDto;
import org.hiphone.eureka.monitor.service.EurekaApplicationService;
import org.hiphone.eureka.monitor.service.EurekaHistoryService;
import org.hiphone.eureka.monitor.service.EurekaInstanceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;
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
            d.setCurrentState(Constant.STATE_DOWN);
        });

        instanceDifferenceSet.addAll(upInstancesSet);
        instanceDifferenceSet.addAll(downInstancesSet);
        return instanceDifferenceSet;
    }

    /**
     * 更新数据库的数据
     * @param clusterId 集群id
     * @param differentInstanceSet applicationInstances
     */
    public void updateDataToDatabase(String clusterId,  Set<ApplicationInstanceDto> differentInstanceSet) {

        Set<ApplicationInstanceDto> dbApplicationInstanceSet = eurekaInstanceService.queryInstancesByStateAndClusterId(Constant.STATE_UP, clusterId);
        Set<ApplicationInstanceDto> tmpApplicationInstances = new LinkedHashSet<>(dbApplicationInstanceSet);

        if (!dbApplicationInstanceSet.isEmpty()) {
            //eureka remove = eureka -db, db remove - db - eureka
            dbApplicationInstanceSet.removeAll(differentInstanceSet);
            differentInstanceSet.removeAll(tmpApplicationInstances);
        }

        //db已经有的数据，已经down, 更新state为down， 并记录down的历史记录
        if (!differentInstanceSet.isEmpty() && !dbApplicationInstanceSet.isEmpty()) {
            for (ApplicationInstanceDto dbInstance : dbApplicationInstanceSet) {
                for (ApplicationInstanceDto eurekaInstance : differentInstanceSet) {
                    if (dbInstance.getInstanceId().equals(eurekaInstance.getInstanceId())) {
                        eurekaInstance.setDownTime(new Date());
                        eurekaInstanceService.insertOrUpdateInstance(eurekaInstance);
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
                eurekaInstanceService.insertOrUpdateInstance(instance);
                ApplicationHistoryDto history = constructHistoryDto(instance, Constant.STATE_UP);
                eurekaHistoryService.insertInstanceHistory(history);
                log.info("Success to save a history of instance insert or update from DOWN to UP, which instance id is {}", instance.getInstanceId());
            }
        }

        //update application
        eurekaApplicationService.insertOrUpdateApplicationInfo(differentInstanceSet);

        log.info("Finished Update the database......");
    }

    /**
     * 更新缓存信息
     * @param clusterId 集群id
     * @param eurekaInstanceSet 从eureka中获取的最新信息
     */
    public void updateCache(String clusterId, Set<ApplicationInstanceDto> eurekaInstanceSet) {
        clusterInstanceCacheMap.put(clusterId, eurekaInstanceSet);
        log.info("Finished update the cache, cache size is {}", clusterInstanceCacheMap.get(clusterId).size());
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
