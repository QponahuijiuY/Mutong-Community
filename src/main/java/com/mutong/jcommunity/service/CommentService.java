package com.mutong.jcommunity.service;

import com.mutong.jcommunity.mapper.CommentMapper;
import com.mutong.jcommunity.model.Comment;
import com.mutong.jcommunity.util.CommunityConstant;
import com.mutong.jcommunity.util.SensitiveFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.util.HtmlUtils;

import java.util.List;

/**
 * @description:
 * @Author: Mutong
 * @Date: 2020-03-04 1:03
 * @time_complexity: O()
 */
@Service
public class CommentService implements CommunityConstant {
    @Autowired
    private CommentMapper commentMapper;
    @Autowired
    private SensitiveFilter sensitiveFilter;
    @Autowired
    private DiscussPostService discussPostService;

    public List<Comment> findCommentsByEntity(int entityType,int entityId,int offset, int limit){
        return commentMapper.selectCommentsByEntity(entityType,entityId,offset,limit);

    }
    public int findCommentCount(int entityType , int entityId){
        return commentMapper.selectCommentByEntity(entityType,entityId);
    }

    /**
     * 添加评论,且增加评论数量
     * @param comment
     * @return
     */
    @Transactional(isolation = Isolation.READ_COMMITTED, propagation = Propagation.REQUIRED)
    public int addComment(Comment comment){
        if (comment == null){
            throw new IllegalArgumentException("参数不能为空");
        }
        //添加评论
        comment.setContent(HtmlUtils.htmlEscape(comment.getContent()));
        comment.setContent(sensitiveFilter.filter(comment.getContent()));
        int rows = commentMapper.insertComment(comment);

        //更新帖子的评论数量
        if (comment.getEntityType() == ENTITY_TYPE_POST){
            //查询帖子评论数量
            int count = commentMapper.selectCommentByEntity(comment.getEntityType(),comment.getEntityId());
            //根据id更新帖子的评论的数量
            discussPostService.updateCommentCount(comment.getEntityId() , count);
        }

        return -1;
    }
}
