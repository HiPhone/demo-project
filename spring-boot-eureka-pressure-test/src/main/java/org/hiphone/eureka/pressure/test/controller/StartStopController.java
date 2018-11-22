package org.hiphone.eureka.pressure.test.controller;

import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.hiphone.eureka.pressure.test.entitys.ResultMessage;
import org.hiphone.eureka.pressure.test.service.StartStopService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(value = "/press")
public class StartStopController {

    private static final Logger LOGGER = LoggerFactory.getLogger(StartStopController.class);

    @Autowired
    private StartStopService startStopService;

    @ResponseBody
    @GetMapping("/start")
    @ApiOperation(value = "开始eureka的压力测试", notes = "使用多线程模拟eureka的注册及心跳包")
    public ResultMessage startPressure(
            @RequestParam(name = "appCount") @ApiParam(name = "appCount", value = "向eureka注册的应用数量") int appCount,
            @RequestParam(name = "instancePerApp") @ApiParam(name = "instancePerApp", value = "每个app的注册实例数") int instancePerApp,
            @RequestParam(name = "heartBeatInterval", required = false, defaultValue = "30") @ApiParam(name = "heartBeatInterval", value = "心跳包发送的时间间隔") int heartBeatInterval,
            @RequestParam(name = "randomRegister", required = false, defaultValue = "false") @ApiParam(name = "randomRegister", value = "App是否随机注册（集群压测使用）") boolean randomRegister,
            @RequestParam(name = "fluentRegister", required = false, defaultValue = "true") @ApiParam(name = "fluentRegister", value = "是否按时间间隔注册") boolean fluentRegister
    ) {
        LOGGER.info("Starting pressure test to eureka......");
        return startStopService.startPressureTest(appCount, instancePerApp, heartBeatInterval, randomRegister, fluentRegister);
    }

    @ResponseBody
    @GetMapping("/stop")
    @ApiOperation(value = "停止eureka的压力测试", notes = "停止所有压力测试线程")
    public ResultMessage stopPressure() {
        LOGGER.info("stopping the eureka pressure test......");
        return startStopService.stopPressureTest();
    }
}
