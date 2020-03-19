package com.mutong.jcommunity;

import com.mutong.jcommunity.model.DiscussPost;
import com.mutong.jcommunity.service.DiscussPostService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Date;

/**
 * @description:
 * @Author: Mutong
 * @Date: 2020-03-18 15:52
 * @time_complexity: O()
 */
@RunWith(SpringRunner.class)
@SpringBootTest
@ContextConfiguration(classes = JcommunityApplication.class)
public class CaffeineTests {
    @Autowired
    private DiscussPostService discussPostService;

    @Test
    public void initDataForTest(){
        for (int i = 0; i < 200000; i ++){
            DiscussPost post = new DiscussPost();
            post.setUserId(1000);
            post.setTitle("我是管理员");
            post.setContent("你们老实一点");
            post.setCreateTime(new Date());
            post.setScore(Math.random() * 20000);
            discussPostService.addDiscussPost(post);
        }
    }

    @Test
    public void testCache(){
        System.out.println(discussPostService.findDiscussPosts(0,0,10,1));
        System.out.println(discussPostService.findDiscussPosts(0,0,10,1));
        System.out.println(discussPostService.findDiscussPosts(0,0,10,1));
        System.out.println(discussPostService.findDiscussPosts(0,0,10,0));
    }
}
