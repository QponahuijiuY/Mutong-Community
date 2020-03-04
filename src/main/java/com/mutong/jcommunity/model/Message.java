package com.mutong.jcommunity.model;

import lombok.Data;

import java.util.Date;

/**
 * @description:
 * @Author: Mutong
 * @Date: 2020-03-04 16:54
 * @time_complexity: O()
 */
@Data
public class Message {
    private int id;
    private int fromId;
    private int toId;
    private String conversationId;
    private String content;
    private int status;
    private Date createTime;
}
