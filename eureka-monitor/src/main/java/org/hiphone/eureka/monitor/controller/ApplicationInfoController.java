package org.hiphone.eureka.monitor.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.hiphone.eureka.monitor.constants.ReturnMsg;
import org.hiphone.eureka.monitor.entitys.ResultMessage;
import org.hiphone.eureka.monitor.service.EurekaApplicationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author HiPhone
 */
@Slf4j
@RestController
@RequestMapping("/applications")
@Api(value = "ApplicationInfoController", description = "有关application信息访问的controller")
public class ApplicationInfoController {

    @Autowired
    private EurekaApplicationService eurekaApplicationService;

    @GetMapping("/clusters")
    @ApiOperation(value = "获取集群的id", notes = "查询application的表获取唯一的clusterId集合")
    public ResultMessage getClustersInfo() {
        return eurekaApplicationService.queryDistinctClusterId();
    }

    @GetMapping("/infos")
    @ApiOperation(value = "通过集群id获取集群中所有application的信息", notes = "通过clusterId到数据库中查询相关信息")
    public ResultMessage getApplicationInfosByClusterId(@RequestParam("clusterId") String clusterId) {
        return new ResultMessage(ReturnMsg.SUCCESS.getCode(),
                ReturnMsg.SUCCESS.getMessage(),
                eurekaApplicationService.queryApplicationsByClusterId(clusterId));
    }
}
