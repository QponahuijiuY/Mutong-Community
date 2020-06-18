package com.mutong.jcommunity.event;

import com.alibaba.fastjson.JSONObject;
import com.mutong.jcommunity.model.Event;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

/**
 * @description: 生产者
 * @Author: Mutong
 * @Date: 2020-06-15 14:10
 * @time_complexity: O()
 */
@Component
public class EventProducer {
    @Autowired
    private KafkaTemplate kafkaTemplate;

    //处理事件->发送消息
    public void fireEvent(Event event){
        //将事件发送到指定的主题:
        kafkaTemplate.send(event.getTopic(), JSONObject.toJSONString(event));
    }
}
