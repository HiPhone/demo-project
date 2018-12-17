package org.hiphone.swagger.center.service;

import org.hiphone.swagger.center.entitys.ResultMessage;

/**
 * @author HiPhone
 */
public interface UserService {

    /**
     * 通过登陆名获取登陆该用户的密码
     * @param loginName 登陆名
     * @return resultMessage
     */
    ResultMessage getUserByLoginName(String loginName);
}
