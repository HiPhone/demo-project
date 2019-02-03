package org.hiphone.eureka.monitor.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.hiphone.eureka.monitor.entitys.ResultMessage;
import org.hiphone.eureka.monitor.service.EurekaHistoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author HiPhone
 */
@RestController
@Api(value = "ApplicationHistoryController", description = "获取历史信息的controller")
public class ApplicationHistoryController {

    @Autowired
    private EurekaHistoryService eurekaHistoryService;

    @GetMapping("/history")
    @ApiOperation(value = "分页获取instance的历史记录", notes = "url参数为page和limit")
    public ResultMessage getInstanceHistory(@ApiParam(name = "page", value = "历史数据的页数") @RequestParam("page") Integer page,
                                            @ApiParam(name = "limit", value = "返回的数据的数目") @RequestParam("limit") Integer limit) {
        if (page < 1 || limit < 1) {
            throw new IllegalArgumentException("参数page或者limit不能小于1");
        }
        return eurekaHistoryService.queryInstanceHistoryByPageAndLimit(page, limit);
    }
}
