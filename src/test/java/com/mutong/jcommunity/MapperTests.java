package com.mutong.jcommunity;

import com.mutong.jcommunity.mapper.DiscussPostMapper;
import com.mutong.jcommunity.mapper.LoginTicketMapper;
import com.mutong.jcommunity.mapper.MessageMapper;
import com.mutong.jcommunity.mapper.UserMapper;
import com.mutong.jcommunity.model.DiscussPost;
import com.mutong.jcommunity.model.LoginTicket;
import com.mutong.jcommunity.model.Message;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Date;
import java.util.List;

/**
 * @description:
 * @Author: Mutong
 * @Date: 2020-02-29 14:58
 * @time_complexity: O()
 */
@RunWith(SpringRunner.class)
@SpringBootTest
@ContextConfiguration(classes = JcommunityApplication.class)
public class MapperTests {
    @Autowired
    private UserMapper userMapper;

    @Autowired
    private DiscussPostMapper discussPostMapper;

    @Autowired
    private LoginTicketMapper loginTicketMapper;

    @Autowired
    private MessageMapper messageMapper;
    @Test
    public void testSelectPosts() {
        List<DiscussPost> list = discussPostMapper.selectDiscussPosts(0, 0, 10);
        for(DiscussPost post : list) {
            System.out.println(post);
        }

        int rows = discussPostMapper.selectDiscussPostRows(0);
        System.out.println(rows);
    }

    @Test
    public void testInsertLoginTicket(){
        LoginTicket loginTicket = new LoginTicket();
        loginTicket.setUserId(1);
        loginTicket.setTicket("123");
        loginTicket.setStatus(0);
        loginTicket.setExpired(new Date(System.currentTimeMillis()));
        loginTicketMapper.insertLoginTicket(loginTicket);
    }

    @Test
    public void testSelectLoginTicket(){
        LoginTicket loginTicket = loginTicketMapper.selectByTicket("123");
        System.out.println(loginTicket);


        loginTicketMapper.updateStatus("123",1);
        System.out.println(loginTicket);
    }



    @Test
    public void testSelectLetters(){
        List<Message> list1 = messageMapper.selectConversations(111, 0, 20);
        for (Message m : list1) {
            System.out.println(m);
        }


        int i = messageMapper.selectConversationCount(111);
        System.out.println(i);

        List<Message> list2 = messageMapper.selectLetters("111_112", 0, 10);
        for (Message m : list2) {
            System.out.println(m);
        }


        int i1 = messageMapper.selectLetterCount("111_112");
        System.out.println(i1);

        int i2 = messageMapper.selectLetterUnreadCount(131, "111_131");
        System.out.println(i2);
    }
}
