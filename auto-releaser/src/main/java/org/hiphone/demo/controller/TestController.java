package org.hiphone.demo.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.hiphone.demo.constants.ReturnMsg;
import org.hiphone.demo.entitys.ResultMessage;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author HiPhone
 */
@Slf4j
@RestController
@Api(value = "TestController", description = "用于测试服务是否存活的Controller")
public class TestController {

    @Value("${spring.application.name}")
    private String applicationName;

    @GetMapping("/echo-test")
    @ApiOperation(value = "用于确认服务是否存活的接口", notes = "返回自身状态")
    public ResultMessage test() {
        log.info("Receive a request for testing " + applicationName);

        return new ResultMessage(
                ReturnMsg.SUCCESS.getCode(),
                ReturnMsg.SUCCESS.getMessage(),
                applicationName + " is alive");
    }

}
