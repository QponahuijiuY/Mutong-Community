package com.mutong.jcommunity.mapper;

import com.mutong.jcommunity.model.Comment;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @description:
 * @Author: Mutong
 * @Date: 2020-03-04 0:54
 * @time_complexity: O()
 */
@Mapper
@Repository
public interface CommentMapper {
    /**
     * 根据类型查询评论
     * @param entityType
     * @param entityId
     * @param offset
     * @param limit
     * @return
     */
    List<Comment> selectCommentsByEntity(@Param("entityType") int entityType,@Param("entityId") int entityId, @Param("offset") int offset,@Param("limit") int limit);

    /**
     * 根据类型查询评论数
     * @param entityType
     * @param entityId
     * @return
     */
    int selectCommentByEntity(@Param("entityType") int entityType, @Param("entityId") int entityId);


    /**
     * 添加评论
     * @param comment
     * @return
     */
    int insertComment( Comment comment);


}
