package com.mutong.jcommunity.service;

import com.mutong.jcommunity.mapper.DiscussPostMapper;
import com.mutong.jcommunity.model.DiscussPost;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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
}
