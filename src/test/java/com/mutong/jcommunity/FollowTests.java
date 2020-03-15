package com.mutong.jcommunity;

import com.mutong.jcommunity.service.FollowService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;

/**
 * @description:
 * @Author: Mutong
 * @Date: 2020-03-15 10:46
 * @time_complexity: O()
 */
@RunWith(SpringRunner.class)
@SpringBootTest
@ContextConfiguration(classes = JcommunityApplication.class)
public class FollowTests {
    @Autowired
    private FollowService followService;

    @Test
    public void testFollow(){
        followService.follow(1,2,3);
        System.out.println("成功");
    }
}
