package org.hiphone.eureka.pressure.test.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiParam;
import org.hiphone.eureka.pressure.test.entitys.ResultMessage;
import org.hiphone.eureka.pressure.test.service.GetAppPressService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author HiPhone
 */
@RestController
@Api(value = "GetAppPressController", description = "模拟eureka客户端同步注册服务信息的controller")
public class GetAppPressController {

    private static final Logger LOGGER = LoggerFactory.getLogger(GetAppPressController.class);

    @Autowired
    private GetAppPressService appPressService;

    @ResponseBody
    @GetMapping("/get-applications")
    public ResultMessage getApplicationsPress(
            @RequestParam(name = "randomRegister") @ApiParam(name = "randomRegister", value = "是否随机向eureka节点请求") boolean randomRegister,
            @RequestParam(name = "intervalMillis") @ApiParam(name = "intervalMillis", value = "请求eureka的时间间隔") int intervalMillis,
            @RequestParam(name = "totalCount") @ApiParam(name = "totalCount", value = "请求的总次数") int totalCount
    ) {
        LOGGER.info("get a task to start get application press to eureka......");
        return appPressService.getApplicationPress(randomRegister, intervalMillis, totalCount);
    }

}
