package com.mutong.jcommunity.model;

import lombok.Data;

import java.util.Date;

/**
 * @description:
 * @Author: Mutong
 * @Date: 2020-02-29 14:45
 * @time_complexity: O()
 */
@Data
public class DiscussPost {
    private int id;
    private int userId;
    private String title;
    private String content;
    private int type;
    private int status;
    private Date createTime;
    private int commentCount;
    private double score;

}
