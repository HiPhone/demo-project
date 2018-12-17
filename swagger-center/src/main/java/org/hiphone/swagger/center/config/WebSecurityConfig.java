package org.hiphone.swagger.center.config;

import org.hiphone.swagger.center.service.impl.CustomUserDetailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.AuthenticationEntryPoint;

/**
 * spring security配置类
 * @author HiPhone
 */
@Configuration
@EnableWebSecurity
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

    @Bean
    public UserDetailsService customUserService() {
        return new CustomUserDetailService();
    }

    @Bean
    public AuthenticationEntryPoint authenticationEntryPoint() {
        return new CustomAuthenticationEntryPoint();
    }

    /**
     * 配置上下文,/echo-test不需要验证身份即可调用,身份验证失败跳转到/login接口,/login默认为登陆界面
     * @param httpSecurity
     * @throws Exception
     */
    @Override
    protected void configure(HttpSecurity httpSecurity) throws Exception {
        httpSecurity
                .authorizeRequests()
                .antMatchers("/echo-test").permitAll()
                //指定能访问接口的单个角色
                .antMatchers( "/admin/**").hasRole("ADMIN")
                //指定能访问接口的多个角色
                .antMatchers("/dba/**").hasAnyRole("DBA", "ADMIN")
                .antMatchers("/user/**").hasAnyRole("USER", "DBA", "ADMIN")
                .anyRequest().authenticated()
                .and()
                .formLogin()
                .successForwardUrl("/")
                .permitAll()
                .and()
                .exceptionHandling().authenticationEntryPoint(authenticationEntryPoint())  //将原生返回的登陆界面以json形式返回，方便前端后分离，注释这行变回原生
                .and()
                .logout()
                .permitAll()
                .and()
                .rememberMe()
                .tokenValiditySeconds(6000000) //记住我的时间
                .and()
                .csrf().disable()
        ;
    }

    /**
     * 使用自定义的数据库进行用户名密码及权限的问题
     * @param auth
     * @throws Exception
     */
    @Autowired
    public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {

        auth.userDetailsService(customUserService()).passwordEncoder(new PasswordEncode());
    }
}
