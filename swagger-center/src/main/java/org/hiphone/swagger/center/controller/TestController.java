package org.hiphone.swagger.center.controller;

import com.alibaba.fastjson.JSONObject;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.hiphone.swagger.center.constants.ReturnMsg;
import org.hiphone.swagger.center.entitys.ResultMessage;
import org.hiphone.swagger.center.entitys.SwaggerApiDocsDTO;
import org.hiphone.swagger.center.mapper.SwaggerCommonMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
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

    @PostMapping("/")
    @ApiOperation(value = "登陆成功返回页", notes = "返回登陆结果")
    public ResultMessage loginSuccess() {
        log.info("Login success");
        return new ResultMessage(
                ReturnMsg.LOGIN_SUCCESS.getCode(),
                ReturnMsg.LOGIN_SUCCESS.getMessage(),
                null
        );
    }

}
