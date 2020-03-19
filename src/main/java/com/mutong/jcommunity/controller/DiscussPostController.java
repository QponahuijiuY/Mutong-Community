package com.mutong.jcommunity.controller;

import com.mutong.jcommunity.event.EventProducer;
import com.mutong.jcommunity.model.Comment;
import com.mutong.jcommunity.model.DiscussPost;
import com.mutong.jcommunity.model.Event;
import com.mutong.jcommunity.model.User;
import com.mutong.jcommunity.provider.Page;
import com.mutong.jcommunity.service.CommentService;
import com.mutong.jcommunity.service.DiscussPostService;
import com.mutong.jcommunity.service.LikeService;
import com.mutong.jcommunity.service.UserService;
import com.mutong.jcommunity.util.CommunityConstant;
import com.mutong.jcommunity.util.CommunityUtil;
import com.mutong.jcommunity.util.HostHolder;
import com.mutong.jcommunity.util.RedisKeyUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.*;

/**
 * @description:
 * @Author: Mutong
 * @Date: 2020-03-03 18:24
 * @time_complexity: O()
 */
@Controller
@RequestMapping("/discuss")
public class DiscussPostController implements CommunityConstant {
    @Autowired
    private DiscussPostService discussPostService;

    @Autowired
    private UserService userService;
    @Autowired
    private HostHolder hostHolder;
    @Autowired
    private CommentService commentService;
    @Autowired
    private LikeService likeService;
    @Autowired
    private EventProducer eventProducer;
    @Autowired
    private RedisTemplate redisTemplate;
    @RequestMapping(value = "/add",method = RequestMethod.POST)
    @ResponseBody
    public String addDiscussPost(String title , String content){
        User user = hostHolder.getUser();
        if (user == null){
            return CommunityUtil.getJSONString(403,"你还没有登录o");
        }
        DiscussPost post = new DiscussPost();
        post.setUserId(user.getId());
        post.setTitle(title);
        post.setContent(content);
        post.setCreateTime(new Date());
        discussPostService.addDiscussPost(post);

        // 触发发帖事件
        Event event = new Event()
                .setTopic(TOPIC_PUBLISH)
                .setUserId(user.getId())
                .setEntityType(ENTITY_TYPE_POST)
                .setEntityId(post.getId());
        eventProducer.fireEvent(event);
        String redisKey = RedisKeyUtil.getPostScoreKey();
        redisTemplate.opsForSet().add(redisKey,post.getId());
        // 报错的情况,将来统一处理.
        return CommunityUtil.getJSONString(0, "发布成功!");
    }


    @RequestMapping(value = "/detail/{discussPostId}",method = RequestMethod.GET)
    public String getDiscussPost(@PathVariable("discussPostId") int discussPostId, Model model, Page page){
        //帖子
        DiscussPost post = discussPostService.findDiscussPostById(discussPostId);
        model.addAttribute("post",post);
        //作者
        User user = userService.findUserById(post.getUserId());
        model.addAttribute("user",user);
        //赞
        long likeCount = likeService.findEntityLikeCount(ENTITY_TYPE_POST,discussPostId);
        model.addAttribute("likeCount",likeCount);
        int likeStatus = hostHolder.getUser() == null ? 0 : likeService.findEntityLikeStatus(hostHolder.getUser().getId(),ENTITY_TYPE_POST,discussPostId);
        model.addAttribute("likeStatus",likeStatus);
        //评论分页信息
        page.setLimit(5);
        page.setPath("/discuss/detail/" + discussPostId);
        page.setRows(post.getCommentCount());

        //评论: 帖子的评论
        //回复: 给评论的评论
        //评论列表
        List<Comment> commentList = commentService.findCommentsByEntity(ENTITY_TYPE_POST, post.getId(), page.getOffset(), page.getLimit());

        //评论Vo列表
        List<Map<String,Object>> commentVoList = new ArrayList<>();

        if (commentList != null){
            for (Comment comment : commentList) {
                //评论Vo
                Map<String,Object> commentVo = new HashMap<>();
                //评论
                commentVo.put("comment",comment);
                //作者
                commentVo.put("user",userService.findUserById(comment.getUserId()));
                likeCount = likeService.findEntityLikeCount(ENTITY_TYPE_COMMENT,comment.getId());
                commentVo.put("likeCount",likeCount);
                likeStatus = hostHolder.getUser() == null ? 0 : likeService.findEntityLikeStatus(hostHolder.getUser().getId(),ENTITY_TYPE_COMMENT,comment.getId());
                commentVo.put("likeStatus",likeStatus);
                //回复列表
                List<Comment> replyList = commentService.findCommentsByEntity(
                        ENTITY_TYPE_COMMENT, comment.getId(), 0, Integer.MAX_VALUE);

                //回复的Vo列表
                List<Map<String,Object>> replyVoList = new ArrayList<>();
                if (replyList != null){
                    for (Comment reply : replyList) {
                        Map<String,Object> replyVo = new HashMap<>();
                        //回复内容
                        replyVo.put("reply",reply);
                        //回复作者
                        replyVo.put("user",userService.findUserById(reply.getUserId()));
                        //回复的目标
                        User targer = reply.getTargetId() == 0 ? null : userService.findUserById(reply.getTargetId());
                        replyVo.put("target",targer);

                        likeCount = likeService.findEntityLikeCount(ENTITY_TYPE_COMMENT,reply.getId());
                        replyVo.put("likeCount",likeCount);
                        likeStatus = hostHolder.getUser() == null ? 0 : likeService.findEntityLikeStatus(hostHolder.getUser().getId(),ENTITY_TYPE_COMMENT,reply.getId());
                        replyVo.put("likeStatus",likeStatus);
                        replyVoList.add(replyVo);
                    }
                }

                commentVo.put("replys",replyVoList);
                //回复数量
                int replyCount = commentService.findCommentCount(ENTITY_TYPE_COMMENT, comment.getId());
                commentVo.put("replyCount",replyCount);
                commentVoList.add(commentVo);

            }
        }
        model.addAttribute("comments",commentVoList);
        return "/site/discuss-detail";
    }


    //置顶
    @RequestMapping(value = "/top",method = RequestMethod.POST)
    @ResponseBody
    public String setTop(int id){
        discussPostService.updateType(id , 1);
        // 触发发帖事件
        Event event = new Event()
                .setTopic(TOPIC_PUBLISH)
                .setUserId(hostHolder.getUser().getId())
                .setEntityType(ENTITY_TYPE_POST)
                .setEntityId(id);
        eventProducer.fireEvent(event);
        return CommunityUtil.getJSONString(0);
    }

    //加精
    @RequestMapping(value = "/wonderful",method = RequestMethod.POST)
    @ResponseBody
    public String setWonderful(int id){
        discussPostService.updateStatus(id , 1);
        // 触发发帖事件
        Event event = new Event()
                .setTopic(TOPIC_PUBLISH)
                .setUserId(hostHolder.getUser().getId())
                .setEntityType(ENTITY_TYPE_POST)
                .setEntityId(id);
        eventProducer.fireEvent(event);
        String redisKey = RedisKeyUtil.getPostScoreKey();
        redisTemplate.opsForSet().add(redisKey,id);
        return CommunityUtil.getJSONString(0);
    }

    @RequestMapping(value = "/delete",method = RequestMethod.POST)
    @ResponseBody
    public String setDelete(int id){
        discussPostService.updateStatus(id , 2);
        // 触发发帖事件
        Event event = new Event()
                .setTopic(TOPIC_DELETE)
                .setUserId(hostHolder.getUser().getId())
                .setEntityType(ENTITY_TYPE_POST)
                .setEntityId(id);
        eventProducer.fireEvent(event);
        return CommunityUtil.getJSONString(0);
    }

}
