package org.hiphone.swagger.center.service.impl;

import lombok.extern.slf4j.Slf4j;
import org.hiphone.swagger.center.constants.ReturnCode;
import org.hiphone.swagger.center.entitys.ResultMessage;
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
        ResultMessage resultMessage;

        try {
            log.info("Starting to check login info which loginName is {}", loginName);

            resultMessage = new ResultMessage(ReturnCode.SUCCESS.getCode(),
                    ReturnCode.LOGIN_SUCCESS.getMessage(),
                    userMapper.getUserByLoginName(loginName));

        } catch (Exception e) {
            e.printStackTrace();
            log.error("database get error, message is {}", e.getMessage());
            resultMessage = new ResultMessage(ReturnCode.SQL_ERROR.getCode(),
                    ReturnCode.SQL_ERROR.getMessage(),
                    e.getMessage());
        }
        return resultMessage;
    }
}
