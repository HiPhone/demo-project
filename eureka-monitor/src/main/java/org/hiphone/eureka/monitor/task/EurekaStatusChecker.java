package org.hiphone.eureka.monitor.task;

import com.alibaba.fastjson.JSONObject;
import com.google.common.collect.Sets;
import lombok.extern.slf4j.Slf4j;
import org.hiphone.eureka.monitor.config.EurekaDataCenterConfig;
import org.hiphone.eureka.monitor.constants.Constant;
import org.hiphone.eureka.monitor.entitys.ApplicationDto;
import org.hiphone.eureka.monitor.entitys.ApplicationInstanceDto;
import org.hiphone.eureka.monitor.service.EurekaApplicationService;
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
    private Map<String ,Set<ApplicationInstanceDto>> clusterInstanceCacheMap = new ConcurrentHashMap<>();

    @Autowired
    private EurekaDataCenterConfig eurekaDataCenterConfig;

    @Autowired
    private EurekaClientUtil eurekaClient;

    @Autowired
    private EurekaInstanceService eurekaInstanceService;

    @Autowired
    private EurekaApplicationService eurekaApplicationService;

    @Override
    public void execute(JobExecutionContext context) {
        log.info("Starting to check the eureka status......");

        Map<String, String> clustersMap = eurekaDataCenterConfig.getClusters();

        clustersMap.forEach((clusterId, clusterUrlString) -> {
            String[] clusterUrls = clusterUrlString.split(",");
            log.info(">>>>>>>>>>>>>>>>>>>>>>>>>>>>>>> Starting to check cluster: {} >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>", clusterId);
            JSONObject eurekaJson = eurekaClient.getEurekaDataJson(clusterId, clusterUrls);

            if (eurekaJson != null) {
                Set<ApplicationInstanceDto> eurekaInstanceSet = eurekaClient.getEurekaServiceInstanceSet(clusterId, eurekaJson);
                //比较缓存与eurekaInstanceSet的差异
                Set<ApplicationInstanceDto> instanceDifferenceSet = checkInstances(clusterId, eurekaInstanceSet);
                Set<ApplicationDto> applicationDifferenceSet = checkApplications(clusterId, instanceDifferenceSet);

                //TODO application
            } else {
                log.warn("One eureka cluster is down!  cluster id is {}, please check it!", clusterId);
            }

        });
    }

    /**
     * 对比instance新数据与旧数据，返回他们之间的不同
     * @param clusterId 集群id
     * @param eurekaInstanceSet eurekaInstanceSet
     * @return 新旧数据的不同
     */
    private Set<ApplicationInstanceDto> checkInstances(String clusterId, Set<ApplicationInstanceDto> eurekaInstanceSet) {
        Set<ApplicationInstanceDto> instanceDifferenceSet = new LinkedHashSet<>();
        Set<ApplicationInstanceDto> oldInstanceSet = clusterInstanceCacheMap.get(clusterId);

        if (oldInstanceSet == null) {
            oldInstanceSet = eurekaInstanceService.queryInstancesByStateAndClusterId(Constant.STATE_UP, clusterId);
        }
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
    private Set<ApplicationDto> checkApplications(String clusterId, Set<ApplicationInstanceDto> differentInstanceSet) {
        Set<ApplicationDto> newApplicationsSet = new LinkedHashSet<>();
        differentInstanceSet.forEach(instance -> {
            ApplicationDto application = new ApplicationDto();
            application.setClusterId(clusterId);
            application.setApplicationName(instance.getApplicationName());
            newApplicationsSet.add(application);
        });

        Set<ApplicationDto> oldApplicationsSet = eurekaApplicationService.queryApplicationsByClusterId(clusterId);
        return Sets.difference(newApplicationsSet, oldApplicationsSet);
    }

    private void updateDatabase(String clusterId, Set<ApplicationDto> )
}
