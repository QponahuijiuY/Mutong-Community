package com.mutong.jcommunity.model;

import lombok.Data;

import java.util.Date;

/**
 * @description:
 * @Author: Mutong
 * @Date: 2020-06-11 15:37
 * @time_complexity: O()
 */
@Data
public class LoginTicket {
    private int id;
    private int userId;
    private String ticket;
    private int status;
    private Date expired;
}
