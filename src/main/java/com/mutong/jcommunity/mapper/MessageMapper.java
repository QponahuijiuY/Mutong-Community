package com.mutong.jcommunity.mapper;

import com.mutong.jcommunity.model.Message;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @description:
 * @Author: Mutong
 * @Date: 2020-03-04 16:58
 * @time_complexity: O()
 */
@Mapper
@Repository
public interface MessageMapper {


    /**
     *     查询当前用户的会话列表,针对每一个会话只返回一条最新的私信
     */
    List<Message> selectConversations(@Param("userId") int userId, @Param("offset") int offset, @Param("limit") int limit);

    /**
     * 查询当前用户的会话数量
     * @param userId
     * @return
     */
    int selectConversationCount(@Param("userId") int userId);

    /**
     * 查询某个会话所包含的私信列表
     * @param conversationId
     * @param offset
     * @param limit
     * @return
     */
    List<Message> selectLetters(@Param("conversationId") String conversationId,@Param("offset") int offset, @Param("limit") int limit);

    /**
     * 查询某个会话所包含的私信数量
     * @param conversationId
     * @return
     */
    int selectLetterCount(@Param("conversationId") String conversationId);

    /**
     * 查询未读私信的数量
     * @param userId
     * @param conversationId
     * @return
     */
    int selectLetterUnreadCount(@Param("userId") int userId,@Param("conversationId") String conversationId);

    /**
     * 新增消息
     * @param message
     * @return
     */
    int insertMessage(@Param("message") Message message);

    /**
     * 修改消息的状态
     * @param ids
     * @param status
     * @return
     */
    int updateStatus(@Param("ids") List<Integer> ids, @Param("status") int status);

}
