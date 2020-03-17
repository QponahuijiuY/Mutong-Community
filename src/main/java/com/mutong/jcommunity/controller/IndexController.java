package com.mutong.jcommunity.controller;

import com.mutong.jcommunity.model.DiscussPost;
import com.mutong.jcommunity.model.User;
import com.mutong.jcommunity.provider.Page;
import com.mutong.jcommunity.service.DiscussPostService;
import com.mutong.jcommunity.service.LikeService;
import com.mutong.jcommunity.service.UserService;
import com.mutong.jcommunity.util.CommunityConstant;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @description:
 * @Author: Mutong
 * @Date: 2020-02-29 13:24
 * @time_complexity: O()
 */
@Controller
public class IndexController implements CommunityConstant {

    @Autowired
    private DiscussPostService discussPostService;
    @Autowired
    private UserService userService;
    @Autowired
    private LikeService likeService;
    //去/index目录下
    @GetMapping("/index")
    //返回一个字符串
    //@ResponseBody
    public String getIndexPage(Model model , Page page){
        //方法调用之前,SpringMVC会自动实例化Model和Page,并且将page自动注入给Model
        //所以在thymeleaf中可以直接访问Page对象中的数据.
        page.setRows(discussPostService.findDiscussPostRows(0));
        page.setPath("/index");
        //list返回一个查询列表
        List<DiscussPost> list = discussPostService.findDiscussPosts(0, page.getOffset(), page.getLimit());
        List<Map<String, Object>> discussPosts = new ArrayList<>();
        if (list != null){
            //遍历得到每一个discusspost
            for (DiscussPost post : list){
                Map<String,Object> map = new HashMap<>();
                map.put("post",post);
                //根据每一个discusspost得到userid,然后根据userid得到整个user
                User user = userService.findUserById(post.getUserId());
                map.put("user",user);
                long likeCount = likeService.findEntityLikeCount(ENTITY_TYPE_POST,post.getId());
                map.put("likeCount",likeCount);
                discussPosts.add(map);
            }
        }
        model.addAttribute("discussPosts",discussPosts);

        return "/index";
    }

    @GetMapping("/error")
    public String getErrorPage(){
        return "/error/500";
    }
    @RequestMapping(value = "/denied", method = RequestMethod.GET)
    public String getDeniedPage(){
        return "/error/404";
    }
}
