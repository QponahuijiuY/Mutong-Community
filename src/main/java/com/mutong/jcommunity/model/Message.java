package com.mutong.jcommunity.model;

import lombok.Data;

import java.util.Date;

/**
 * @description:
 * @Author: Mutong
 * @Date: 2020-06-14 16:54
 * @time_complexity: O()
 */
@Data
public class Message {
    private int id;
    private int fromId;
    private int toId;
    private String conversationId;
    private String content;
    //已读,未读,删除
    private int status;
    private Date createTime;
}
