package org.hiphone.eureka.monitor.task;

import com.alibaba.fastjson.JSONObject;
import lombok.extern.slf4j.Slf4j;
import org.hiphone.eureka.monitor.config.EurekaDataCenterConfig;
import org.hiphone.eureka.monitor.entitys.ApplicationDto;
import org.hiphone.eureka.monitor.entitys.ApplicationInstanceDto;
import org.hiphone.eureka.monitor.utils.EurekaClientUtil;
import org.quartz.JobExecutionContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.Set;

/**
 * @author HiPhone
 */
@Slf4j
@Component
public class EurekaStatusChecker implements BaseJob {

    @Autowired
    private EurekaDataCenterConfig eurekaDataCenterConfig;

    @Autowired
    private EurekaClientUtil eurekaClient;

    @Autowired
    private CheckHelper checkHelper;

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
                Set<ApplicationInstanceDto> instanceDifferenceSet = checkHelper.checkInstances(clusterId, eurekaInstanceSet);
                Set<ApplicationDto> applicationDifferenceSet = checkHelper.checkApplications(clusterId, instanceDifferenceSet);

                //TODO application
            } else {
                log.warn("One eureka cluster is down!  cluster id is {}, please check it!", clusterId);
            }

        });
    }

}
