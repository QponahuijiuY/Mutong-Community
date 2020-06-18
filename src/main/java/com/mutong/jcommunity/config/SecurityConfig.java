package com.mutong.jcommunity.config;

import com.mutong.jcommunity.util.CommunityConstant;
import com.mutong.jcommunity.util.CommunityUtil;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.access.AccessDeniedHandler;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

/**
 * @description:
 * @Author: Mutong
 * @Date: 2020-06-17 11:48
 * @time_complexity: O()
 */
@Configuration
public class SecurityConfig extends WebSecurityConfigurerAdapter implements CommunityConstant {

    @Override
    public void configure(WebSecurity webSecurity) throws Exception{
        webSecurity.ignoring().antMatchers("/resources/**");
    }
    @Override
    protected void configure(HttpSecurity httpSecurity) throws Exception{
        //授权
        httpSecurity.authorizeRequests()
                .antMatchers(
                        "/user/setting",
                        "/user/upload",
                        "/discuss/add",
                        "/comment/add/**",
                        "/letter/**",
                        "notice/**",
                        "/like",
                        "/follow",
                        "unfollow"
                )
                .hasAnyAuthority(
                        AUTGORITY_USER,
                        AUTGORITY_ADMIN,
                        AUTGORITY_MODERATOR
                )
                .antMatchers(
                        "/discuss/top",
                        "/discuss/wonderful"
                )
                .hasAnyAuthority(
                        AUTGORITY_MODERATOR
                )
                .antMatchers(
                        "/discuss/delete",
                        "/data/**"
                )
                .hasAnyAuthority(
                        AUTGORITY_ADMIN
                )
                .anyRequest().permitAll()
                .and().csrf().disable();

        //权限不够时的处理
        httpSecurity.exceptionHandling()
                .authenticationEntryPoint(new AuthenticationEntryPoint() {
                    @Override
                    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException e) throws IOException, ServletException {
                    //没有登陆
                        String xRequestedWith = request.getHeader("x-requested-with");
                        if ("XMLHttpRequest".equals(xRequestedWith)){
                            response.setContentType("application/plain;charset=utf-8");
                            PrintWriter writer = response.getWriter();
                            writer.write(CommunityUtil.getJSONString(403,"你还没有登陆"));
                        }else {
                            response.sendRedirect(request.getContextPath() + "/login");
                        }
                    }
                })
                .accessDeniedHandler(new AccessDeniedHandler() {
                    @Override
                    public void handle(HttpServletRequest request, HttpServletResponse response, AccessDeniedException e) throws IOException, ServletException {
                    //权限不足
                        String xRequestedWith = request.getHeader("x-requested-with");
                        if ("XMLHttpRequest".equals(xRequestedWith)){
                            response.setContentType("application/plain;charset=utf-8");
                            PrintWriter writer = response.getWriter();
                            writer.write(CommunityUtil.getJSONString(403,"没有权限"));
                        }else {
                            response.sendRedirect(request.getContextPath() + "/denied");
                        }
                    }
                });
        httpSecurity.logout().logoutUrl("/securitylogout");
    }
}
