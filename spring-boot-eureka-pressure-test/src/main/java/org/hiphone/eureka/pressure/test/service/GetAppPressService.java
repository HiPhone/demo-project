package org.hiphone.eureka.pressure.test.service;

import org.hiphone.eureka.pressure.test.entitys.ResultMessage;

/**
 * @author HiPhone
 */
public interface GetAppPressService {

    /**
     * 开始获取eureka服务列表的压力测试
     * @param randomRegister 是否随机给eureka节点压力
     * @param intervalMillis 请求的时间间隔
     * @param totalCount 请求的总次数
     * @return resultMessage
     */
    ResultMessage getApplicationPress(boolean randomRegister, int intervalMillis, int totalCount);
}
