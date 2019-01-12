package org.hiphone.eureka.monitor.task;

import com.alibaba.fastjson.JSONObject;
import com.google.common.collect.Sets;
import lombok.extern.slf4j.Slf4j;
import org.hiphone.eureka.monitor.config.EurekaDataCenterConfig;
import org.hiphone.eureka.monitor.constants.Constant;
import org.hiphone.eureka.monitor.entitys.ServiceInstanceDto;
import org.hiphone.eureka.monitor.service.EurekaInstanceService;
import org.hiphone.eureka.monitor.utils.EurekaClientUtil;
import org.quartz.JobExecutionContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author HiPhone
 */
@Slf4j
@Component
public class EurekaStatusChecker implements BaseJob {

    /**
     * 存放缓存信息的map
     */
    private Map<String ,Set<ServiceInstanceDto>> clusterInstanceCacheMap = new ConcurrentHashMap<>();

    @Autowired
    private EurekaDataCenterConfig eurekaDataCenterConfig;

    @Autowired
    private EurekaClientUtil eurekaClient;

    @Autowired
    private EurekaInstanceService eurekaInstanceService;

    @Override
    public void execute(JobExecutionContext context) {
        log.info("Starting to check the eureka status......");

        Map<String, String> clustersMap = eurekaDataCenterConfig.getClusters();

        clustersMap.forEach((clusterId, clusterUrlString) -> {
            String[] clusterUrls = clusterUrlString.split(",");
            log.info(">>>>>>>>>>>>>>>>>>>>>>>>>>>>>>> Starting to check cluster: {} >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>", clusterId);
            JSONObject eurekaJson = eurekaClient.getEurekaDataJson(clusterId, clusterUrls);

            if (eurekaJson != null) {
                Set<ServiceInstanceDto> eurekaInstanceSet = eurekaClient.getEurekaServiceInstanceSet(clusterId, eurekaJson);
                //比较缓存与eurekaInstanceSet的差异
                Set<ServiceInstanceDto> instanceDifferenceSet = checkInstances(clusterId, eurekaInstanceSet);

                //TODO application
            } else {
                log.warn("One eureka cluster is down!  cluster id is {}, please check it!", clusterId);
            }

        });
    }

    /**
     * 对比新数据与旧数据，返回他们之间的不同
     * @param clusterId 集群id
     * @param eurekaInstanceSet eurekaInstanceSet
     * @return 新旧数据的不同
     */
    private Set<ServiceInstanceDto> checkInstances(String clusterId, Set<ServiceInstanceDto> eurekaInstanceSet) {
        Set<ServiceInstanceDto> instanceDifferenceSet = new LinkedHashSet<>();
        Set<ServiceInstanceDto> oldInstanceSet = clusterInstanceCacheMap.get(clusterId);

        if (oldInstanceSet == null) {
            oldInstanceSet = eurekaInstanceService.queryInstancesByStateAndClusterId(Constant.STATE_UP, clusterId);
        }
        log.info("The old instances data of cluster {} initialize success! the size is {}", clusterId, oldInstanceSet.size());

        //eurekaInstances - oldInstances = upInstances
        Set<ServiceInstanceDto> upInstancesSet = Sets.difference(eurekaInstanceSet, oldInstanceSet);

        //oldInstance - eurekaInstances = downInstances
        Set<ServiceInstanceDto> downInstancesSet = Sets.difference(oldInstanceSet, eurekaInstanceSet);

        downInstancesSet.forEach(d -> {
            d.setCurrentState(1);
        });

        instanceDifferenceSet.addAll(upInstancesSet);
        instanceDifferenceSet.addAll(downInstancesSet);
        return instanceDifferenceSet;
    }
}
