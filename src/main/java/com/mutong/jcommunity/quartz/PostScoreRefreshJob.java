package com.mutong.jcommunity.quartz;

import com.mutong.jcommunity.model.DiscussPost;
import com.mutong.jcommunity.service.DiscussPostService;
import com.mutong.jcommunity.service.ElasticSearchService;
import com.mutong.jcommunity.service.LikeService;
import com.mutong.jcommunity.util.CommunityConstant;
import com.mutong.jcommunity.util.RedisKeyUtil;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.BoundSetOperations;
import org.springframework.data.redis.core.RedisTemplate;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * @description:
 * @Author: Mutong
 * @Date: 2020-03-18 13:55
 * @time_complexity: O()
 */
public class PostScoreRefreshJob implements Job, CommunityConstant {

    private static final Logger logger = LoggerFactory.getLogger(PostScoreRefreshJob.class);
    @Autowired
    private RedisTemplate redisTemplate;

    @Autowired
    private DiscussPostService discussPostService;

    @Autowired
    private LikeService likeService;

    @Autowired
    private ElasticSearchService elasticSearchService;

    private static final Date epoch;

    static {
        try {
            epoch = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse("2020-3-18 00:00:00");
        } catch (ParseException e) {
            throw new RuntimeException("失败" ,e);
        }
    }
    @Override
    public void execute(JobExecutionContext jobExecutionContext) throws JobExecutionException {
        String redisKey = RedisKeyUtil.getPostScoreKey();
        BoundSetOperations operations = redisTemplate.boundSetOps(redisKey);
        if (operations.size() == 0){
            logger.info("没有需要刷新的帖子");
            return;
        }
        logger.info("开始刷新帖子"+ operations.size());
        while (operations.size() > 0){
            this.refresh((Integer)operations.pop());
        }
        logger.info("刷新帖子结束");
    }



    private void refresh(Integer postId){
        DiscussPost post = discussPostService.findDiscussPostById(postId);
        if (post == null){
            logger.error("该帖子不存在, id = " + post);
            return;
        }
        //是否加精
        boolean wonderful = post.getScore() == 1;
        int commentCount = post.getCommentCount();
        long likeCount = likeService.findEntityLikeCount(ENTITY_TYPE_POST, postId);
        //计算权重
        double w = (wonderful ? 75 : 0) + commentCount * 10 + likeCount * 2;
        //分数 = 权重 + 距离
        double score = Math.log10(Math.max(w, 1)) + (post.getCreateTime().getTime() - epoch.getTime()) / (1000 * 3600 * 24);
        discussPostService.updateScore(postId,score);
        //同步搜索数据

        post.getScore();
        elasticSearchService.saveDiscussPost(post);
    }
}
