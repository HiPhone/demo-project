package org.hiphone.auto.publisher.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.hiphone.auto.publisher.constants.ParamGroup;
import org.hiphone.auto.publisher.entitys.BaseParam;
import org.hiphone.auto.publisher.entitys.ParamFactory;
import org.hiphone.auto.publisher.entitys.ResultMessage;
import org.hiphone.auto.publisher.exception.ReturnMsg;
import org.hiphone.auto.publisher.service.ShellExcuteService;
import org.hiphone.auto.publisher.utils.LoadParamUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

/**
 * @author HiPhone
 */
@Slf4j
@RestController
@RequestMapping("/shell")
@Api(value = "ShellExecuteController", description = "控制shell脚本执行的controller")
public class ShellExecuteController {

    @Autowired
    private ShellExcuteService shellExcuteService;

    @PostMapping("/ips")
    @ApiOperation(value = "配置ip信息的接口", notes = "解析参数运行配置ip的脚本")
    public ResultMessage configDeployIps(HttpServletRequest request) throws Exception {
        log.info("/ips interface get called, param: {}", LoadParamUtils.loadParam(request));

        BaseParam param = ParamFactory.createParamInstance(request, ParamGroup.IPS);

        String result = shellExcuteService.runConfigIpsShell(param);

        return new ResultMessage(ReturnMsg.SUCCESS.getCode(),
                ReturnMsg.SUCCESS.getMessage(),
                result);
    }

    @PostMapping("/auth")
    @ApiOperation(value = "配置认证信息的接口", notes = "解析参数并运行auth的脚本")
    public ResultMessage configAuth(HttpServletRequest request) throws Exception {
        log.info("/auth interface get called, param: {}", LoadParamUtils.loadParam(request));

        BaseParam param = ParamFactory.createParamInstance(request, ParamGroup.AUTH);
        String result = shellExcuteService.runConfigAuthShell(param);
        log.info("The operation of auth's result is {}", result);
        return new ResultMessage(ReturnMsg.SUCCESS.getCode(),
                ReturnMsg.SUCCESS.getMessage(),
                result);
    }

    @PostMapping("/deploy")
    @ApiOperation(value = "部署应用包的接口", notes = "根据参数运行deploy的脚本")
    public ResultMessage deploy(HttpServletRequest request) throws Exception {
        log.info("/deploy interface get called, param is {}", LoadParamUtils.loadParam(request));

        BaseParam param = ParamFactory.createParamInstance(request, ParamGroup.DEPLOY);

        String result = shellExcuteService.runDeployShell(param);

        log.info("Deploy shell run success, the result is {}", result);

        return new ResultMessage(ReturnMsg.SUCCESS.getCode(),
                ReturnMsg.SUCCESS.getMessage(),
                result);
    }
}
