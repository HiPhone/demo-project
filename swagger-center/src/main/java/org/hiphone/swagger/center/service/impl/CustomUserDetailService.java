package org.hiphone.swagger.center.service.impl;

import org.hiphone.swagger.center.constants.Constant;
import org.hiphone.swagger.center.entitys.UserDTO;
import org.hiphone.swagger.center.mapper.UserMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.ArrayList;
import java.util.List;

/**
 * @author HiPhone
 */
@Service
public class CustomUserDetailService implements UserDetailsService {

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private HttpServletRequest request;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        UserDTO user = userMapper.getUserByLoginName(username);
        if (user == null) {
            throw new UsernameNotFoundException("user is not found in database!");
        }

        HttpSession session = request.getSession();
        session.setAttribute("user", user);
        List<GrantedAuthority> authorities = new ArrayList<>();

        getRolesByRoleId(user.getRole()).forEach(role -> {
            authorities.add(new SimpleGrantedAuthority(role));
        });

        return new User(user.getLoginName(), user.getPassword(),authorities);
    }

    private List<String> getRolesByRoleId(int roleId) {
        //Spring security的坑： 角色必须以ROLE_开头，ROLE_USER认为是USER角色,直接填USER则认为是USER权限
        List<String> roleList = new ArrayList<>();

        if (roleId <= 2) {
            roleList.add(Constant.ROLE_USER);
        }

        if (roleId <= 1) {
            roleList.add(Constant.ROLE_DBA);
        }

        if (roleId == 0) {
            roleList.add(Constant.ROLE_ADMIN);
        }
        return roleList;
    }
}
