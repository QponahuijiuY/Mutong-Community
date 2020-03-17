package com.mutong.jcommunity.mapper;

import com.mutong.jcommunity.model.DiscussPost;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @description:
 * @Author: Mutong
 * @Date: 2020-02-29 14:46
 * @time_complexity: O()
 */
@Mapper
@Repository
public interface DiscussPostMapper {

    /**
     * 根据用户id查询
     * @param userId 用户id
     * @param offset 起始页码
     * @param limit 终止页码
     * @return 一个页面存放在list集合里面
     */
    List<DiscussPost> selectDiscussPosts(@Param("userId") int userId,@Param("offset")  int offset ,@Param("limit")  int limit);

    //@Param用于给参数取别名,如果只有一个参数,并且在<if>里面使用,则必须使用别名

    /**
     * 查询总的帖子行数,注意关注底层sql实现
     * @param userId 用户id
     * @return 帖子的个数
     */
    int selectDiscussPostRows(@Param("userId") int userId);

    /**
     * 发帖
     * @param discussPost
     * @return
     */
    int insertDiscussPost(DiscussPost discussPost);


    /**
     * 查询帖子详情
     * @param id
     * @return 帖子内容
     */
    DiscussPost selectDiscussPostById(@Param("id") int id);

    /**
     * 更新评论数量
     * @param id
     * @param commentCount
     * @return
     */
    int updateCommentCount(@Param("id") int id , @Param("commentCount") int commentCount);


    /**
     *
     * @param id
     * @param type
     * @return
     */
    int updateType(@Param("id") int id, @Param("type")int type);

    /**
     * 设置加精
     * @param id
     * @param status
     * @return
     */
    int updateStatus(@Param("id") int id, @Param("status")int status);
 }
