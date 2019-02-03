package org.hiphone.eureka.monitor.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.extern.slf4j.Slf4j;
import org.hiphone.eureka.monitor.entitys.ResultMessage;
import org.hiphone.eureka.monitor.service.EurekaInstanceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author HiPhone
 */
@RestController
@RequestMapping("/instances")
@Api(value = "ApplicationInstanceController", description = "获取application instance信息的controller")
public class ApplicationInstanceController {

    @Autowired
    private EurekaInstanceService eurekaInstanceService;

    @GetMapping("/infos")
    @ApiOperation(value = "通过applicationName或者clusterId和state请求instance数据的接口", notes = "返回对应的数据")
    public ResultMessage getInstancesByClusterIdOrApplicationName(@ApiParam(name = "clusterId", value = "集群id") @RequestParam(value = "clusterId", required = false) String clusterId,
                                                                  @ApiParam(name = "applicationName", value = "应用的名称") @RequestParam(value = "applicationName", required = false) String applicationName,
                                                                  @ApiParam(name = "state", value = "instance的状态") @RequestParam(value = "state", required = false) Integer state) {
        ResultMessage resultMessage;
        if (clusterId == null && applicationName == null) {
            throw new IllegalArgumentException("参数clusterId与applicationName不能同时为空");
        } else {
            resultMessage = eurekaInstanceService.queryInstancesByClusterIdOrApplicationNameOrState(clusterId, applicationName, state);
        }
        return resultMessage;
    }
}
