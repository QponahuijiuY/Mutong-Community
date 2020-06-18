package com.mutong.jcommunity.model;

import lombok.Data;

import java.util.Date;

/**
 * @description:
 * @Author: Mutong
 * @Date: 2020-06-09 14:27
 * @time_complexity: O()
 */
@Data
public class User {
    private int id;
    private String username;
    private String password;
    private String salt;
    private String email;
    private int type;
    private int status;
    private String activationCode;
    private String headerUrl;
    private Date createTime;
}
