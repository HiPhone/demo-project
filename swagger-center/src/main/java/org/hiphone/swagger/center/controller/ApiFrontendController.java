package org.hiphone.swagger.center.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.extern.slf4j.Slf4j;
import org.hiphone.swagger.center.entitys.ResultMessage;
import org.hiphone.swagger.center.service.ApiFrontendService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author HiPhone
 */
@Slf4j
@RestController
@RequestMapping(value = "/front")
@Api(value = "ApiFrontController", description = "前端获取数据使用的controller")
public class ApiFrontendController {

    @Autowired
    private ApiFrontendService apiFrontendService;

    @GetMapping("/service-names")
    @ApiOperation(value = "获取已经入库的所有服务的名称", notes = "返回服务名称的封装")
    public ResultMessage queryAllServiceNames() {
        return apiFrontendService.queryAllServiceNames();
    }

    @GetMapping("/api-docs/{serviceName}")
    @ApiOperation(value = "通过服务名称获取api-docs", notes = "返回数据库中的api-docs")
    public ResultMessage queryApiDocsByServiceName(@ApiParam(name = "serviceName", value = "服务名称") @PathVariable String serviceName) {
        return apiFrontendService.queryApiDocsByServiceName(serviceName);
    }

}
