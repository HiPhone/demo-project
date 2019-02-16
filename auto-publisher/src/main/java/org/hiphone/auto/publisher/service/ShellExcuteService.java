package org.hiphone.auto.publisher.service;

import org.hiphone.auto.publisher.entitys.BaseParam;

/**
 * @author HiPhone
 */
public interface ShellExcuteService {

    /**
     * 配置部署的ip地址信息，构造shell语句并执行
     * @param param ip地址参数列表
     * @return 运行结果
     * @throws Exception 异常
     */
    String runConfigIpsShell(BaseParam param) throws Exception;

    /**
     * 配置认证信息
     * @param param 参数
     * @return 配置结果
     * @throws Exception 抛出的异常
     */
    String runConfigAuthShell(BaseParam param) throws Exception;

    /**
     * 运行部署脚本的方法
     * @param param 脚本参数
     * @return 运行结果
     * @throws Exception 异常信息
     */
    String runDeployShell(BaseParam param) throws Exception;
}
