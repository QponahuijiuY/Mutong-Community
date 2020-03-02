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

    //根据用户id查询
    List<DiscussPost> selectDiscussPosts(@Param("userId") int userId,@Param("offset")  int offset ,@Param("limit")  int limit);

    //@Param用于给参数取别名,如果只有一个参数,并且在<if>里面使用,则必须使用别名
    //查询所有帖子数量
    int selectDiscussPostRows(@Param("userId") int userId);
}
