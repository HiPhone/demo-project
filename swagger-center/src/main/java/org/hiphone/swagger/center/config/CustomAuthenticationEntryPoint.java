package org.hiphone.swagger.center.config;

import org.hiphone.swagger.center.constants.ReturnMsg;
import org.hiphone.swagger.center.entitys.ResultMessage;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * 自定义403的返回，返回json数据，而不是登陆原生登陆页
 * @author HiPhone
 */
public class CustomAuthenticationEntryPoint implements AuthenticationEntryPoint {

    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException exception) throws IOException, ServletException {
        response.setContentType("application/json;charset=UTF-8");
        response.setStatus(403);
        response.getWriter().print(new ResultMessage(
                ReturnMsg.FORBIDDEN.getCode(),
                ReturnMsg.FORBIDDEN.getMessage(),
                null
        ));
    }
}
