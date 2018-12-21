package org.hiphone.swagger.center.schedule;

import com.alibaba.fastjson.JSONObject;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

/**
 * @author HiPhone
 */
@Slf4j
@Component
public class ApiAutoUpdater {

    @Autowired
    private AutoScanHelper autoScanHelper;

    @Scheduled(cron = "0 0/60 * * * ?")
    public void autoScan() {

        log.info("Starting to scan the swagger api-doc by eureka......");

        List<String> eurekaServiceNames = autoScanHelper.getApplicationNamesFromEureka();

        if (!eurekaServiceNames.isEmpty()) {
            Map<String, JSONObject> swaggerApiDocsMap = autoScanHelper.getSwaggerApiMap(eurekaServiceNames);


        } else {
            log.warn("can't get application names from eureka......");
        }
    }

}
