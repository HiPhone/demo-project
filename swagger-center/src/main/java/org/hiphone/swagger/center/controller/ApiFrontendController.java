package org.hiphone.swagger.center.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.extern.slf4j.Slf4j;
import org.hiphone.swagger.center.exception.ReturnMsg;
import org.hiphone.swagger.center.entitys.ResultMessage;
import org.hiphone.swagger.center.service.ApiFrontendService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * @author HiPhone
 */
@Slf4j
@RestController
@RequestMapping(value = "/front")
@Api(value = "ApiFrontController", description = "前端获取数据使用的controller")
public class ApiFrontendController {

    private static final String MODE_SIMPLIFY = "simplify";
    private static final String MODE_ORIGIN = "origin";

    @Autowired
    private ApiFrontendService apiFrontendService;

    @GetMapping("/service-names")
    @ApiOperation(value = "获取已经入库的所有服务的名称", notes = "返回服务名称的封装")
    public ResultMessage queryAllServiceNames() {
        return apiFrontendService.queryAllServiceNames();
    }

    @GetMapping("/api-docs/{serviceName}")
    @ApiOperation(value = "通过服务名称获取api-docs", notes = "返回数据库中的api-docs")
    public ResultMessage queryApiDocsByServiceName(@ApiParam(name = "serviceName", value = "服务名称") @PathVariable String serviceName,
                                                   @ApiParam(name = "mode", value = "模式") @RequestParam(name = "mode", required = false, defaultValue = "simplify") String mode) {
        ResultMessage resultMessage;
        if (mode.equals(MODE_SIMPLIFY)) {
            resultMessage = apiFrontendService.querySimplifyApiDocs(serviceName);
        } else if (mode.equals(MODE_ORIGIN)) {
            resultMessage = apiFrontendService.queryApiDocsByServiceName(serviceName);
        } else {
            resultMessage = new ResultMessage(ReturnMsg.PARAM_ERROR.getCode(),
                    ReturnMsg.PARAM_ERROR.getMessage(),
                    null);
        }
        return resultMessage;
    }
}
