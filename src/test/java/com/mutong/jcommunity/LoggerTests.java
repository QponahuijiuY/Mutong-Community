package com.mutong.jcommunity;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;

/**
 * @description:
 * @Author: Mutong
 * @Date: 2020-02-29 22:11
 * @time_complexity: O()
 */
@RunWith(SpringRunner.class)
@SpringBootTest
@ContextConfiguration(classes = JcommunityApplication.class)
public class LoggerTests {
    private static final Logger logger = LoggerFactory.getLogger(LoggerTests.class);

    @Test
    public void testLogger(){
        System.out.println(logger.getName());
        logger.debug("debuglog");
        logger.warn("warnlog");
        logger.error("errorlog");
    }
}
