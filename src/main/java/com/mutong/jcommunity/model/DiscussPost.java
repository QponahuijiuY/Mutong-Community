package com.mutong.jcommunity.model;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

import java.util.Date;

/**
 * @description:
 * @Author: Mutong
 * @Date: 2020-06-10 14:45
 * @time_complexity: O()
 */
@Document(indexName = "discusspost",type = "_doc",shards = 6,replicas = 3)
@Data
public class DiscussPost {
    @Id
    private int id;
    @Field(type = FieldType.Integer)
    private int userId;
    @Field(type = FieldType.Text,analyzer = "ik_max_word",searchAnalyzer = "ik_smart")
    private String title;
    @Field(type = FieldType.Text,analyzer = "ik_max_word",searchAnalyzer = "ik_smart")
    private String content;
    @Field(type = FieldType.Integer)
    private int type;//0 普通 1 置顶
    @Field(type = FieldType.Integer)
    private int status;//0 正常 1 精华 2 删除
    @Field(type = FieldType.Date)
    private Date createTime;
    @Field(type = FieldType.Integer)
    private int commentCount;
    @Field(type = FieldType.Double)
    private double score;

}
