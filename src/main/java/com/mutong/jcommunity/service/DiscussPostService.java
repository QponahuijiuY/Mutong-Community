package com.mutong.jcommunity.service;

import com.mutong.jcommunity.mapper.DiscussPostMapper;
import com.mutong.jcommunity.model.DiscussPost;
import com.mutong.jcommunity.util.SensitiveFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.util.HtmlUtils;

import java.util.List;

/**
 * @description:
 * @Author: Mutong
 * @Date: 2020-02-29 15:21
 * @time_complexity: O()
 */
@Service
public class DiscussPostService {

    @Autowired
    private DiscussPostMapper discussPostMapper;
    @Autowired
    private SensitiveFilter sensitiveFilter;

    /**
     * 查询固定行数的讨论到页面上
     * @param userId
     * @param offset
     * @param limit
     * @return
     */
    public List<DiscussPost> findDiscussPosts( int userId,  int offset ,  int limit){
        return discussPostMapper.selectDiscussPosts(userId, offset, limit);
    }

    /**
     * 查询总的用户数目
     * @param userId
     * @return
     */
    public int findDiscussPostRows(int userId){
        return discussPostMapper.selectDiscussPostRows(userId);
    }

    /**
     * 添加帖子
     * @param post
     * @return
     */
    public int addDiscussPost(DiscussPost post){
        if (post == null){
            throw new IllegalArgumentException("参数不能为空");
        }
        //转移HTML标记
        post.setTitle(HtmlUtils.htmlEscape(post.getTitle()));
        post.setContent(HtmlUtils.htmlEscape(post.getContent()));
        //过滤敏感词
        post.setTitle(sensitiveFilter.filter(post.getTitle()));
        post.setContent(sensitiveFilter.filter(post.getContent()));

        return discussPostMapper.insertDiscussPost(post);
    }

    /**
     * 查询帖子
     * @param id
     * @return
     */
    public DiscussPost findDiscussPostById(int id){
        return discussPostMapper.selectDiscussPostById(id);
    }

    /**
     * 更新帖子评论数量
     * @param id 帖子id
     * @param commentCount 帖子数量
     * @return
     */
    public int updateCommentCount(int id,int commentCount){
        return discussPostMapper.updateCommentCount(id,commentCount);
    }
}
