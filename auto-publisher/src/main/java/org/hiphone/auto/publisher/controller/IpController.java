package org.hiphone.auto.publisher.controller;

import lombok.extern.slf4j.Slf4j;
import org.hiphone.auto.publisher.entitys.BaseParam;
import org.hiphone.auto.publisher.entitys.ResultMessage;
import org.hiphone.auto.publisher.service.IpsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

/**
 * @author HiPhone
 */
@Slf4j
@RestController
public class IpController {

    @Autowired
    private IpsService ipsService;

    @PostMapping("/ips")
    public ResultMessage configDeployIps(HttpServletRequest request) {

        BaseParam
    }

}
