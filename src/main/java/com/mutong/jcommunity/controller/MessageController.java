package com.mutong.jcommunity.controller;

import com.alibaba.fastjson.JSONObject;
import com.mutong.jcommunity.model.Message;
import com.mutong.jcommunity.model.User;
import com.mutong.jcommunity.provider.Page;
import com.mutong.jcommunity.service.MessageService;
import com.mutong.jcommunity.service.UserService;
import com.mutong.jcommunity.util.CommunityConstant;
import com.mutong.jcommunity.util.CommunityUtil;
import com.mutong.jcommunity.util.HostHolder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.util.HtmlUtils;

import java.util.*;

/**
 * @description:
 * @Author: Mutong
 * @Date: 2020-03-10 16:11
 * @time_complexity: O()
 */
@Controller
public class MessageController implements CommunityConstant {

    @Autowired
    private MessageService messageService;
    @Autowired
    private HostHolder hostHolder;
    @Autowired
    private UserService userService;

    /**
     * 私信列表
     * @param model
     * @param page
     * @return
     */
    @RequestMapping(value = "/letter/list", method = RequestMethod.GET)
    public String getLetterList(Model model, Page page){
        User user = hostHolder.getUser();
        //分页信息
        page.setLimit(5);
        page.setPath("/letter/list");
        page.setRows(messageService.findConversationCount(user.getId()));
        //会话列表
        List<Message> conversationList = messageService.findConversations(user.getId(), page.getOffset(), page.getLimit());

        List<Map<String,Object>> conversations = new ArrayList<>();
        if (conversationList != null){
            for (Message message : conversationList){
                Map<String,Object> map = new HashMap<>();
                map.put("conversation",message);
                map.put("letterCount",messageService.findLetterCount(message.getConversationId()));
                map.put("unreadCount",messageService.findLetterUnreadCount(user.getId(),message.getConversationId()));
                int targetId = user.getId() == message.getFromId() ? message.getToId() : message.getFromId();
                map.put("target",userService.findUserById(targetId));
                conversations.add(map);
            }
        }
        model.addAttribute("conversations",conversations);
        //查询未读消息数量
        int letterUnreadCount = messageService.findLetterUnreadCount(user.getId(),null);
        model.addAttribute("letterUnreadCount",letterUnreadCount);
        int noticeUnreadCount = messageService.findNoticeUnreadCount(user.getId(),null);
        model.addAttribute("noticeUnreadCount",noticeUnreadCount);
        return "/site/letter";
    }

    /**
     * 私信详情页
     * @param conversationId
     * @param page
     * @param model
     * @return
     */
    @RequestMapping(value = "/letter/detail/{conversationId}",method = RequestMethod.GET)
    public String getLetterDetail(@PathVariable("conversationId")String conversationId,Page page,Model model){
        //分页信息
        page.setLimit(5);
        page.setPath("/letter/detail/" + conversationId);
        page.setRows(messageService.findLetterCount(conversationId));

        List<Message> letterList = messageService.findLetters(conversationId, page.getOffset(), page.getLimit());
        List<Map<String,Object>> letters = new ArrayList<>();
        if (letterList != null){
            for (Message message : letterList){
                Map<String,Object> map = new HashMap<>();
                map.put("letter",message);
                map.put("fromUser",userService.findUserById(message.getFromId()));
                letters.add(map);
            }
        }
        model.addAttribute("letters",letters);
        //私信的目标
        model.addAttribute("target",getLetterTarget(conversationId));

        //设置已读
        List<Integer> ids = getLetterIds(letterList);
        if (!ids.isEmpty()){
            messageService.readMessage(ids);
        }


        return "/site/letter-detail";
    }

    /**
     * 发送私信
     * @param toName
     * @param content
     * @return
     */
    @RequestMapping(value = "/letter/send", method = RequestMethod.POST)
    @ResponseBody
    public String sendLetter(String toName, String content){
        User target = userService.findUserByName(toName);
        if (target == null){
            return CommunityUtil.getJSONString(1,"目标用户不存在");
        }
        Message message = new Message();
        message.setFromId(hostHolder.getUser().getId());
        message.setToId(target.getId());
        if (message.getFromId() < message.getToId()){
            message.setConversationId(message.getFromId() + "_" + message.getToId());
        }else{
            message.setConversationId(message.getToId() + "_" + message.getFromId());
        }
        message.setContent(content);
        message.setCreateTime(new Date());
        messageService.addMessage(message);

        return CommunityUtil.getJSONString(0);
    }

    /**
     * 消息通知列表
     * @param model
     * @return
     */
    @RequestMapping(path = "/notice/list", method = RequestMethod.GET)
    public String getNoticeList(Model model) {
        User user = hostHolder.getUser();

        // 查询评论类通知
        Message commentMessage = messageService.findLatesNotice(user.getId(), TOPIC_COMMENT);
        Map<String, Object> commentVo = new HashMap<>();
        if (commentMessage != null) {
            commentVo.put("message", commentMessage);

            String content = HtmlUtils.htmlUnescape(commentMessage.getContent());
            Map<String, Object> data = JSONObject.parseObject(content, HashMap.class);

            commentVo.put("user", userService.findUserById((Integer) data.get("userId")));
            commentVo.put("entityType", data.get("entityType"));
            commentVo.put("entityId", data.get("entityId"));
            commentVo.put("postId", data.get("postId"));

            int count = messageService.findNoticeCount(user.getId(), TOPIC_COMMENT);
            commentVo.put("count", count);

            int unread = messageService.findNoticeUnreadCount(user.getId(), TOPIC_COMMENT);
            commentVo.put("unread", unread);
        }
        model.addAttribute("commentNotice", commentVo);

        // 查询点赞类通知
        Message likeMessage = messageService.findLatesNotice(user.getId(), TOPIC_LIKE);
        Map<String, Object> likeVo = new HashMap<>();
        if (likeMessage != null) {
            likeVo.put("message", likeMessage);

            String content = HtmlUtils.htmlUnescape(likeMessage.getContent());
            Map<String, Object> data = JSONObject.parseObject(content, HashMap.class);

            likeVo.put("user", userService.findUserById((Integer) data.get("userId")));
            likeVo.put("entityType", data.get("entityType"));
            likeVo.put("entityId", data.get("entityId"));
            likeVo.put("postId", data.get("postId"));

            int count = messageService.findNoticeCount(user.getId(), TOPIC_LIKE);
            likeVo.put("count", count);

            int unread = messageService.findNoticeUnreadCount(user.getId(), TOPIC_LIKE);
            likeVo.put("unread", unread);
        }
        model.addAttribute("likeNotice", likeVo);

        // 查询关注类通知
        Message followMessage = messageService.findLatesNotice(user.getId(), TOPIC_FOLLOW);
        Map<String, Object> followVo = new HashMap<>();
        if (followMessage != null) {
            followVo.put("message", followMessage);

            String content = HtmlUtils.htmlUnescape(followMessage.getContent());
            Map<String, Object> data = JSONObject.parseObject(content, HashMap.class);

            followVo.put("user", userService.findUserById((Integer) data.get("userId")));
            followVo.put("entityType", data.get("entityType"));
            followVo.put("entityId", data.get("entityId"));

            int count = messageService.findNoticeCount(user.getId(), TOPIC_FOLLOW);
            followVo.put("count", count);

            int unread = messageService.findNoticeUnreadCount(user.getId(), TOPIC_FOLLOW);
            followVo.put("unread", unread);
        }
        model.addAttribute("followNotice", followVo);

        // 查询未读消息数量
        int letterUnreadCount = messageService.findLetterUnreadCount(user.getId(), null);
        model.addAttribute("letterUnreadCount", letterUnreadCount);
        int noticeUnreadCount = messageService.findNoticeUnreadCount(user.getId(), null);
        model.addAttribute("noticeUnreadCount", noticeUnreadCount);

        return "/site/notice";
    }

    /**
     * 消息详情页
     * @param topic
     * @param page
     * @param model
     * @return
     */
    @RequestMapping(value = "/notice/detail/{topic}",method = RequestMethod.GET)
    public String getNoticeDetail(@PathVariable("topic")String topic,Page page,Model model){
        User user = hostHolder.getUser();
        page.setLimit(5);
        page.setPath("/notice/detail/"+topic);
        page.setRows(messageService.findNoticeCount(user.getId(),topic));

        List<Message> notiesList = messageService.findNotces(user.getId(), topic, page.getOffset(), page.getLimit());
        List<Map<String,Object>> noticeVoList = new ArrayList<>();
        if (notiesList != null){
            for (Message notice : notiesList) {
                Map<String, Object> map = new HashMap<>();
                //通知
                map.put("notice", notice);
                String content = HtmlUtils.htmlUnescape(notice.getContent());
                Map<String, Object> data = JSONObject.parseObject(content, HashMap.class);
                map.put("user", userService.findUserById((Integer) data.get("userId")));
                map.put("entityType", data.get("entityType"));
                map.put("entityId", data.get("entityId"));
                map.put("postId", data.get("postId"));
                //通知的作者
                map.put("fromUser", userService.findUserById(notice.getFromId()));
                noticeVoList.add(map);
            }
        }
        model.addAttribute("notices",noticeVoList);
        //设置已读
        List<Integer> ids = getLetterIds(notiesList);
        if (!ids.isEmpty()){
            messageService.readMessage(ids);
        }
        return "site/notice-detail";
    }

    private List<Integer> getLetterIds(List<Message> letterList){
        List<Integer> ids = new ArrayList<>();

        if (letterList != null){
            for (Message message : letterList){
                if (hostHolder.getUser().getId() == message.getToId() && message.getStatus() == 0){
                    ids.add(message.getId());
                }
            }
        }
        return ids;
    }

    private User getLetterTarget(String conversationId){
        String[] ids = conversationId.split("_");
        int id0 = Integer.parseInt(ids[0]);
        int id1 = Integer.parseInt(ids[1]);

        if (hostHolder.getUser().getId() == id0){
            return userService.findUserById(id1);
        }else {
            return userService.findUserById(id0);
        }
    }
}
