package com.mutong.jcommunity;

import com.mutong.jcommunity.util.MailClient;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

/**
 * @description:
 * @Author: Mutong
 * @Date: 2020-03-01 11:57
 * @time_complexity: O()
 */
@RunWith(SpringRunner.class)
@SpringBootTest
@ContextConfiguration(classes = JcommunityApplication.class)
public class MailTests {
    @Autowired
    private MailClient mailClient;

    @Autowired
    private TemplateEngine templateEngine;


    @Test
    public void testTestMail(){
        mailClient.sendMail("1530201142@qq.com", "test", "hello world");
    }

    @Test
    public void testHtmlMail(){
        Context context = new Context();
        context.setVariable("username", "sunday");
        String content = templateEngine.process("/mail/demo", context);
        System.out.println(content);


        mailClient.sendMail("1530201142@qq.com", "test", content);
    }
}
