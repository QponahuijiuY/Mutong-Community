package com.mutong.jcommunity.service;

import com.mutong.jcommunity.mapper.MessageMapper;
import com.mutong.jcommunity.model.Message;
import com.mutong.jcommunity.util.SensitiveFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.util.HtmlUtils;

import java.util.List;

/**
 * @description:
 * @Author: Mutong
 * @Date: 2020-03-10 16:05
 * @time_complexity: O()
 */
@Service
public class MessageService {

    @Autowired
    private MessageMapper messageMapper;
    @Autowired
    private SensitiveFilter sensitiveFilter;

    public List<Message> findConversations(int userId, int offset,int limit){
        return messageMapper.selectConversations(userId,offset,limit);
    }

    public int findConversationCount(int userId){
        return messageMapper.selectConversationCount(userId);
    }

    public List<Message> findLetters(String conversationId,int offset,int limit){
        return messageMapper.selectLetters(conversationId,offset,limit);
    }

    public int findLetterCount(String conversationId){
        return messageMapper.selectLetterCount(conversationId);
    }

    public int findLetterUnreadCount(int userId, String conversationId){
        return messageMapper.selectLetterUnreadCount(userId,conversationId);
    }

    public int addMessage(Message message){
        message.setContent(HtmlUtils.htmlEscape(message.getContent()));
        message.setContent(sensitiveFilter.filter(message.getContent()));
        return messageMapper.insertMessage(message);
    }

    public int readMessage(List<Integer> ids){
        return messageMapper.updateStatus(ids,1);
    }

    public Message findLatesNotice(int userId , String topic){
        return messageMapper.selectLatesNotice(userId,topic);
    }

    public int findNoticeCount(int userId, String topic){
        return messageMapper.selectNoticeCount(userId,topic);
    }

    public int findNoticeUnreadCount(int userId,String topic){
        return messageMapper.selectNoticeUnreadCount(userId,topic);
    }

    public List<Message> findNotces(int userId, String topic, int offset, int limit){
        return messageMapper.selectNotices(userId,topic,offset,limit);
    }


}
