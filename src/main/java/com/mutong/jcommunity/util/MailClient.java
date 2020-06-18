package com.mutong.jcommunity.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;

import javax.mail.internet.MimeMessage;

/**
 * @description:
 * @Author: Mutong
 * @Date: 2020-06-11 11:49
 * @time_complexity: O()
 */
@Component
@Repository
public class MailClient {
    private static final Logger logger = LoggerFactory.getLogger(MailClient.class);

    @Autowired
    private JavaMailSender mailSender;

    @Value("${spring.mail.username}")
    private String from;

    public void sendMail(String to , String subject , String context){
        try {
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message);
            helper.setFrom(from);
            helper.setTo(to);
            helper.setSubject(subject);
            helper.setText(context, true);
            mailSender.send(helper.getMimeMessage());
        }catch (Throwable e){
            logger.error("发送邮件失败:" + e.getMessage());
        }

    }
}
