package com.mutong.jcommunity.util;

import com.mutong.jcommunity.model.User;
import org.springframework.stereotype.Component;

/**
 * @description:持有用户信息,用于代替session对象
 * @Author: Mutong
 * @Date: 2020-06-12 13:50
 * @time_complexity: O()
 */
@Component
public class HostHolder {

    private ThreadLocal<User> users = new ThreadLocal<>();

    public void setUser(User user){
        users.set(user);
    }

    public User getUser(){
        return users.get();
    }

    public void clear(){
        users.remove();
    }


}
