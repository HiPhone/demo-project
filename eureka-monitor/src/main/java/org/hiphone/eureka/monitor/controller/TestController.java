package org.hiphone.eureka.monitor.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.hiphone.eureka.monitor.constants.ReturnMsg;
import org.hiphone.eureka.monitor.entitys.ApplicationInstanceDto;
import org.hiphone.eureka.monitor.entitys.ResultMessage;
import org.hiphone.eureka.monitor.service.EurekaInstanceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

/**
 * @author HiPhone
 */
@Slf4j
@RestController
@Api(value = "TestController", description = "用于测试服务是否存活的Controller")
public class TestController {

    @Value("${spring.application.name}")
    private String applicationName;

    @Autowired
    private EurekaInstanceService eurekaInstanceService;

    @ResponseBody
    @GetMapping("/echo-test")
    @ApiOperation(value = "用于确认服务是否存活的接口", notes = "返回自身状态")
    public ResultMessage echoTest() {
        log.info("Receive a request for testing " + applicationName);

        return new ResultMessage(
                ReturnMsg.SUCCESS.getCode(),
                ReturnMsg.SUCCESS.getMessage(),
                applicationName + " is alive");
    }

    @GetMapping("/test")
    public void test1() {
        ApplicationInstanceDto a = new ApplicationInstanceDto();
        a.setApplicationName("1");
        a.setClusterId("1");
        a.setCurrentState(0);
        a.setHostname("1");
        a.setInstanceId("te");
        a.setIpAddress("3232");
        a.setRegisterTime(new Date());
        a.setServicePort(212);
        a.setDownTime(new Date(0));
//        Set<ApplicationInstanceDto> set = new HashSet<>();
//        set.add(a);
        eurekaInstanceService.insertOrUpdateDownInstance(a);
    }

}
