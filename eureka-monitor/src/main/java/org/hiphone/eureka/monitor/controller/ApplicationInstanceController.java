package org.hiphone.eureka.monitor.controller;

import lombok.extern.slf4j.Slf4j;
import org.hiphone.eureka.monitor.constants.ReturnMsg;
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
@Slf4j
@RestController
@RequestMapping("/instances")
public class ApplicationInstanceController {

    @Autowired
    private EurekaInstanceService eurekaInstanceService;

    @GetMapping("/infos")
    public ResultMessage getInstancesByClusterIdOrApplicationName(@RequestParam(value = "clusterId", required = false) String clusterId,
                                                                  @RequestParam(value = "applicationName", required = false) String applicationName,
                                                                  @RequestParam(value = "state", required = false) Integer state) {
        ResultMessage resultMessage;
        if (clusterId == null && applicationName == null) {
            resultMessage = new ResultMessage(ReturnMsg.PARAM_ERROR.getCode(),
                    ReturnMsg.PARAM_ERROR.getMessage(),
                    "clusterId与applicationName参数不能同时为空");
        } else {
            resultMessage = new ResultMessage(ReturnMsg.SUCCESS.getCode(),
                    ReturnMsg.SUCCESS.getMessage(),
                    eurekaInstanceService.queryInstancesByClusterIdOrApplicationNameOrState(clusterId, applicationName, state));
        }

        return resultMessage;
    }
}
