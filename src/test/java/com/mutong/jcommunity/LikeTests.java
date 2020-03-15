package com.mutong.jcommunity;

import com.mutong.jcommunity.service.LikeService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;

/**
 * @description:
 * @Author: Mutong
 * @Date: 2020-03-15 10:58
 * @time_complexity: O()
 */
@RunWith(SpringRunner.class)
@SpringBootTest
@ContextConfiguration(classes = JcommunityApplication.class)
public class LikeTests {
    @Autowired
    private LikeService likeService;

    @Test
    public void testLike(){
        likeService.like(1,2,3,4);
    }
}
