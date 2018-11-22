package org.hiphone.eureka.pressure.test.service;

import org.hiphone.eureka.pressure.test.entitys.ResultMessage;

public interface StartStopService {

    /**
     * 开始进行eureka的压力测试
     * @param appCount   向eureka注册的应用数量
     * @param instancePerApp   每个app的注册实例数
     * @param heartBeatInterval   心跳包发送的时间间隔
     * @param randomRegister   App是否随机注册
     * @param fluentRegister   是否按时间间隔注册
     * @return
     */
    ResultMessage startPressureTest(int appCount, int instancePerApp, int heartBeatInterval, boolean randomRegister, boolean fluentRegister);

    /**
     * 停止压力测试
     * @return 停止结果
     */
    ResultMessage stopPressureTest();
}
