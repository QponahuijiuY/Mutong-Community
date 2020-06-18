package com.mutong.jcommunity.controller;

import com.mutong.jcommunity.model.DiscussPost;
import com.mutong.jcommunity.provider.Page;
import com.mutong.jcommunity.service.ElasticSearchService;
import com.mutong.jcommunity.service.LikeService;
import com.mutong.jcommunity.service.UserService;
import com.mutong.jcommunity.util.CommunityConstant;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @description:
 * @Author: Mutong
 * @Date: 2020-06-16 14:18
 * @time_complexity: O()
 */
@Controller
public class SearchController implements CommunityConstant {

    @Autowired
    private ElasticSearchService elasticSearchService;
    @Autowired
    private UserService userService;
    @Autowired
    private LikeService likeService;

    @RequestMapping(value = "/search",method = RequestMethod.GET)
    public String search(String keyword, Page page, Model model){
        //搜索帖子:
        org.springframework.data.domain.Page<DiscussPost> searchResult = elasticSearchService.searchDiscussPost(keyword, page.getCurrent()-1,page.getLimit());
        List<Map<String, Object>> discussPosts = new ArrayList<>();
        if (searchResult != null){
            for (DiscussPost post : searchResult){
                Map<String,Object> map = new HashMap<>();
                //帖子
                map.put("post",post);
                //作者
                map.put("user",userService.findUserById(post.getUserId()));
                //点赞数量
                map.put("likeCount",likeService.findEntityLikeCount(ENTITY_TYPE_POST,post.getId()));

                discussPosts.add(map);
            }
        }
        model.addAttribute("discussPosts",discussPosts);
        model.addAttribute("keyword",keyword);
        page.setPath("/search?keyword=" + keyword);
        page.setRows(searchResult == null ? 0 : (int) searchResult.getTotalElements());
        return "/site/search";

    }
}
