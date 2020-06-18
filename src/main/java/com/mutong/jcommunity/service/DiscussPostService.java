package com.mutong.jcommunity.service;

import com.github.benmanes.caffeine.cache.CacheLoader;
import com.github.benmanes.caffeine.cache.Caffeine;
import com.github.benmanes.caffeine.cache.LoadingCache;
import com.mutong.jcommunity.mapper.DiscussPostMapper;
import com.mutong.jcommunity.model.DiscussPost;
import com.mutong.jcommunity.util.SensitiveFilter;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.checkerframework.checker.nullness.qual.Nullable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.util.HtmlUtils;

import javax.annotation.PostConstruct;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * @description:
 * @Author: Mutong
 * @Date: 2020-06-11 15:21
 * @time_complexity: O()
 */
@Service
public class DiscussPostService {

    private static final Logger logger = LoggerFactory.getLogger(DiscussPostService.class);
    @Autowired
    private DiscussPostMapper discussPostMapper;
    @Autowired
    private SensitiveFilter sensitiveFilter;

    @Value("${caffeine.posts.max-size}")
    private int maxSize;
    @Value("${caffeine.posts.expire-seconds}")
    private int expireSecond;

    //帖子列表缓存
    private LoadingCache<String,List<DiscussPost>> postListCache;

    //帖子总数患处
    private LoadingCache<Integer,Integer> postRowsCache;

    @PostConstruct
    public void init(){

        postListCache = Caffeine.newBuilder()
                .maximumSize(maxSize)
                .expireAfterWrite(expireSecond, TimeUnit.SECONDS)
                .build(new CacheLoader<String, List<DiscussPost>>() {
                    @Nullable
                    @Override
                    public List<DiscussPost> load(@NonNull String key) throws Exception {
                        if (key == null || key.length() == 0){
                            throw new IllegalArgumentException("参数错误!");
                        }
                        String[] split = key.split(":");
                        if (split == null || split.length != 2){
                            throw new IllegalArgumentException("参数错误!");
                        }
                        int offset = Integer.valueOf(split[0]);
                        int limit = Integer.valueOf(split[1]);
                        logger.debug("从数据库里面取帖子数据");

                        return discussPostMapper.selectDiscussPosts(0,offset,limit,1);
                    }
                });

        //初始化总行数的缓存
        postRowsCache = Caffeine.newBuilder()
                .maximumSize(maxSize)
                .expireAfterWrite(expireSecond,TimeUnit.SECONDS)
                .build(new CacheLoader<Integer, Integer>() {
                    @Nullable
                    @Override
                    public Integer load(@NonNull Integer key) throws Exception {
                        logger.debug("从数据库里面取总行数数据");
                        return discussPostMapper.selectDiscussPostRows(key);
                    }
                });
    }





    /**
     * 查询固定行数的讨论到页面上
     * @param userId
     * @param offset
     * @param limit
     * @return
     */
    public List<DiscussPost> findDiscussPosts( int userId,  int offset ,  int limit, int orderModel){
        if (userId == 0 && orderModel == 1){
            return postListCache.get(offset + ":" + limit);
        }
        logger.debug("从数据库里面取帖子数据");
        return discussPostMapper.selectDiscussPosts(userId, offset, limit,orderModel);
    }

    /**
     * 查询总的用户数目
     * @param userId
     * @return
     */
    public int findDiscussPostRows(int userId){
        if (userId == 0){
            return postRowsCache.get(userId);
        }
        logger.debug("从数据库里面取总行数数据");
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

    public int updateType(int id, int type){
        return discussPostMapper.updateType(id,type);
    }

    public int updateStatus(int id ,int status){
        return discussPostMapper.updateStatus(id,status);
    }

    public int updateScore(int id ,double score){
        return discussPostMapper.updateScore(id,score);
    }

}