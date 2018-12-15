package org.hiphone.swagger.center.schedule;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * @author HiPhone
 */
@Slf4j
@Component
public class ApiAutoUpdater {

    @Autowired
    private AutoScanHelper autoScanHelper;

    @Scheduled(cron = "0 0/10 * * * ?")
    public void autoScan() {

        log.info("Starting to scan the swagger api-doc by eureka......");

        List<String> eurekaServiceNames = autoScanHelper.getAppNameFromEureka();

        if (!eurekaServiceNames.isEmpty()) {

        } else {
            log.warn("can't get application names from eureka......");
        }
    }

}
