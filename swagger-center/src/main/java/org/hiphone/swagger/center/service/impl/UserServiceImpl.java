package org.hiphone.swagger.center.service.impl;

import lombok.extern.slf4j.Slf4j;
import org.hiphone.swagger.center.entitys.ResultMessage;
import org.hiphone.swagger.center.exception.ReturnMsg;
import org.hiphone.swagger.center.mapper.UserMapper;
import org.hiphone.swagger.center.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author HiPhone
 */
@Slf4j
@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserMapper userMapper;

    @Override
    public ResultMessage getUserByLoginName(String loginName) {
        return new ResultMessage(
                ReturnMsg.SUCCESS.getCode(),
                ReturnMsg.LOGIN_SUCCESS.getMessage(),
                userMapper.getUserByLoginName(loginName)
        );
    }
}
