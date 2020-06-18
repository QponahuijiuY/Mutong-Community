package com.mutong.jcommunity.controller;

import com.mutong.jcommunity.event.EventProducer;
import com.mutong.jcommunity.model.Event;
import com.mutong.jcommunity.model.User;
import com.mutong.jcommunity.service.LikeService;
import com.mutong.jcommunity.util.CommunityConstant;
import com.mutong.jcommunity.util.CommunityUtil;
import com.mutong.jcommunity.util.HostHolder;
import com.mutong.jcommunity.util.RedisKeyUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.HashMap;
import java.util.Map;

/**
 * @description:
 * @Author: Mutong
 * @Date: 2020-06-11 15:28
 * @time_complexity: O()
 */
@Controller
public class LikeController implements CommunityConstant {
    @Autowired
    private LikeService likeService;
    @Autowired
    private HostHolder hostHolder;
    @Autowired
    private EventProducer eventProducer;
    @Autowired
    private RedisTemplate redisTemplate;
    @RequestMapping(value = "/like",method = RequestMethod.POST)
    @ResponseBody
    public String like(int entityType ,int entityId,int entityUserId, int postId){
        User user = hostHolder.getUser();

        //点赞
        likeService.like(user.getId(),entityType,entityId,entityUserId);
        //数量
        long likeCount = likeService.findEntityLikeCount(entityType,entityId);
        //状态
        int likeStatus = likeService.findEntityLikeStatus(user.getId(),entityType,entityId);

        Map<String,Object> map = new HashMap<>();
        map.put("likeCount",likeCount);
        map.put("likeStatus",likeStatus);

        //触发点赞事件
        if (likeStatus == 1){
            Event event = new Event()
                    .setTopic(TOPIC_LIKE)
                    .setUserId(hostHolder.getUser().getId())
                    .setEntityType(entityType)
                    .setEntityId(entityId)
                    .setEntityUserId(entityUserId)
                    .setData("postId",postId);
            eventProducer.fireEvent(event);
        }
        if (entityType == ENTITY_TYPE_POST){
            String redisKey = RedisKeyUtil.getPostScoreKey();
            redisTemplate.opsForSet().add(redisKey,postId);
        }

        return CommunityUtil.getJSONString(0,null,map);
    }




}
