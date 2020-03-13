package com.mutong.jcommunity;

import com.mutong.jcommunity.model.Comment;
import com.mutong.jcommunity.service.CommentService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;

/**
 * @description:
 * @Author: Mutong
 * @Date: 2020-03-13 20:58
 * @time_complexity: O()
 */
@RunWith(SpringRunner.class)
@SpringBootTest
@ContextConfiguration(classes = JcommunityApplication.class)
public class CommentTests {
    @Autowired
    private CommentService commentService;


    @Test
    public void  test(Comment comment){

    }
}
