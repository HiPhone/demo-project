package org.hiphone.eureka.monitor.task;

import com.alibaba.fastjson.JSONObject;
import lombok.extern.slf4j.Slf4j;
import org.hiphone.eureka.monitor.config.EurekaDataCenterConfig;
import org.hiphone.eureka.monitor.utils.EurekaClientUtil;
import org.quartz.JobExecutionContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Map;

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

    @Override
    public void execute(JobExecutionContext context) {
        log.info("Starting to check the eureka status......");

        Map<String, String> clustersMap = eurekaDataCenterConfig.getClusters();

        clustersMap.forEach((clusterId, clusterUrlString) -> {
            String[] clusterUrls = clusterUrlString.split(",");
            log.info(">>>>>>>>>>>>>>>>>>>>>>>>>>>>>>> Starting to check cluster: {} >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>", clusterId);
            JSONObject eurekaJson = eurekaClient.getEurekaDataJson(clusterId, clusterUrls);

            if (eurekaJson != null) {

            }

        });
    }
}
