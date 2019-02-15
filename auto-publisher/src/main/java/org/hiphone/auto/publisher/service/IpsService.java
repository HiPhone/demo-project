package org.hiphone.auto.publisher.service;

import org.hiphone.auto.publisher.entitys.BaseParam;

public interface IpsService {

    /**
     * 配置部署的ip地址信息，构造shell语句并执行
     * @param param ip地址参数列表
     * @return 运行结果
     * @throws Exception 异常
     */
    String configIps(BaseParam param) throws Exception;
}
