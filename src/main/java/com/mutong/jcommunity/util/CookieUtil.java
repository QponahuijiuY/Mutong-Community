package com.mutong.jcommunity.util;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;

/**
 * @description:
 * @Author: Mutong
 * @Date: 2020-06-12 13:37
 * @time_complexity: O()
 */
public class CookieUtil {
    public static String getValue(HttpServletRequest request,String name){
        if (request == null || name == null){
            throw new IllegalArgumentException("参数为空");
        }
        Cookie[] cookies = request.getCookies();
        if (cookies != null){
            for (Cookie cookie : cookies) {
                if (cookie.getName().equals(name)){
                    return cookie.getValue();
                }
            }
        }
        return null;
    }
}
