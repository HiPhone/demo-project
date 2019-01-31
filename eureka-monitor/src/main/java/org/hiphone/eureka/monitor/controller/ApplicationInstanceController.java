package org.hiphone.eureka.monitor.controller;

import lombok.extern.slf4j.Slf4j;
import org.hiphone.eureka.monitor.service.EurekaInstanceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author HiPhone
 */
@Slf4j
@RestController
@RequestMapping("/instance")
public class ApplicationInstanceController {

    @Autowired
    private EurekaInstanceService eurekaInstanceService;


}
