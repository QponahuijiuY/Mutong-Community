package com.mutong.jcommunity.interceptor;

import com.mutong.jcommunity.model.User;
import com.mutong.jcommunity.service.DateService;
import com.mutong.jcommunity.util.HostHolder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @description:
 * @Author: Mutong
 * @Date: 2020-06-18 10:55
 * @time_complexity: O()
 */
@Component
public class DateInterceptor implements HandlerInterceptor {
    @Autowired
    private DateService dateService;
    @Autowired
    private HostHolder hostHolder;
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        String ip = request.getRemoteHost();
        dateService.recordUV(ip);

        User user = hostHolder.getUser();
        if (user != null){
            dateService.recordDAU(user.getId());
        }
        return true;
    }

}
