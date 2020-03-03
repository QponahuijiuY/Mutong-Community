package com.mutong.jcommunity;

import com.mutong.jcommunity.util.SensitiveFilter;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;

/**
 * @description:
 * @Author: Mutong
 * @Date: 2020-03-03 14:44
 * @time_complexity: O()
 */
@RunWith(SpringRunner.class)
@SpringBootTest
@ContextConfiguration(classes = JcommunityApplication.class)
public class SensitiveTests {
    @Autowired
    private SensitiveFilter sensitiveFilter;
    @Test
    public void testSensitiveFilter(){
        String text = "这里可以赌*博,可以吸*毒,可以嫖*娼,哈哈哈哈哈";
        text = sensitiveFilter.filter(text);
        System.out.println(text);
    }
}
