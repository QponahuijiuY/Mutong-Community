package com.mutong.jcommunity.event;

import com.alibaba.fastjson.JSONObject;
import com.mutong.jcommunity.model.DiscussPost;
import com.mutong.jcommunity.model.Event;
import com.mutong.jcommunity.model.Message;
import com.mutong.jcommunity.service.DiscussPostService;
import com.mutong.jcommunity.service.ElasticSearchService;
import com.mutong.jcommunity.service.MessageService;
import com.mutong.jcommunity.util.CommunityConstant;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * @description:
 * @Author: Mutong
 * @Date: 2020-03-15 14:12
 * @time_complexity: O()
 */
@Component
public class EventConsumer implements CommunityConstant {
    private static final Logger logger = LoggerFactory.getLogger(EventConsumer.class);
    @Autowired
    private MessageService messageService;
    @Autowired
    private DiscussPostService discussPostService;
    @Autowired
    private ElasticSearchService elasticSearchService;
    @KafkaListener(topics = {TOPIC_COMMENT,TOPIC_FOLLOW,TOPIC_LIKE})
    public void handlerCommentMessage(ConsumerRecord record){
        if (record == null || record.value() == null){
            logger.error("消息内容为空");
            return;
        }
        Event event = JSONObject.parseObject(record.value().toString(),Event.class);
        if (event == null){
            logger.error("消息格式错误");
            return;
        }
        //发送站内通知
        Message message = new Message();
        message.setFromId(SYSTEM_USER_ID);
        message.setToId(event.getEntityUserId());
        message.setConversationId(event.getTopic());
        message.setCreateTime(new Date());
        Map<String,Object> content = new HashMap<>();
        content.put("userId",event.getUserId());
        content.put("entityType",event.getEntityType());
        content.put("entityId",event.getEntityId());
        if (!event.getData().isEmpty()){
            for (Map.Entry<String,Object> entry : event.getData().entrySet()){
                content.put(entry.getKey(),entry.getValue());
            }
        }
        message.setContent(JSONObject.toJSONString(content));
        messageService.addMessage(message);
    }

    //消费发帖事件
    @KafkaListener(topics = {TOPIC_PUBLISH})
    public void handlerPublicMessage(ConsumerRecord record){
        if (record == null || record.value() == null){
            logger.error("消息内容为空");
            return;
        }
        Event event = JSONObject.parseObject(record.value().toString(),Event.class);
        if (event == null){
            logger.error("消息格式错误");
            return;
        }

        DiscussPost post = discussPostService.findDiscussPostById(event.getEntityId());
        elasticSearchService.saveDiscussPost(post);
    }

    //消费删除帖子事件
    @KafkaListener(topics = {TOPIC_DELETE})
    public void handlerDeleteMessage(ConsumerRecord record){
        if (record == null || record.value() == null){
            logger.error("消息内容为空");
            return;
        }
        Event event = JSONObject.parseObject(record.value().toString(),Event.class);
        if (event == null){
            logger.error("消息格式错误");
            return;
        }
        elasticSearchService.deleteDiscussPost(event.getEntityId());
    }
}
