package com.mutong.jcommunity;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Date;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * @description:
 * @Author: Mutong
 * @Date: 2020-03-18 11:48
 * @time_complexity: O()
 */
@RunWith(SpringRunner.class)
@SpringBootTest
@ContextConfiguration(classes = JcommunityApplication.class)
public class ThreadPoolTests {
    private static final Logger logger = LoggerFactory.getLogger(ThreadPoolTests.class);
    //jdk普通的线程池
    private ExecutorService executorService = Executors.newFixedThreadPool(5);
    //jdk可以执行定时任务的线程池
    private ScheduledExecutorService scheduledExecutorService = Executors.newScheduledThreadPool(5);

    //spring普通线程池
    @Autowired
    private ThreadPoolTaskExecutor threadPoolTaskExecutor;

    //spring可执行定时任务的线程池
    @Autowired
    private ThreadPoolTaskScheduler threadPoolTaskScheduler;
    private void sleep(long m){
        try {
            Thread.sleep(m);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
    @Test
    public void testExecutorService(){
        Runnable task = new Runnable() {
            @Override
            public void run() {
                logger.debug("hello");
            }
        };
        for (int i = 0 ; i< 10 ; i++){
            executorService.submit(task);
        }
        sleep(10000);
    }

    //jdk定时任务线程池
    @Test
    public void testScheduledExecutorService(){
        Runnable task = new Runnable() {
            @Override
            public void run() {
                logger.debug("hello");
            }
        };
        for  (int i = 0 ; i< 10 ; i++){
            scheduledExecutorService.scheduleAtFixedRate(task, 10000, 1000, TimeUnit.MILLISECONDS);
        }
        sleep(30000);
    }

    //spring普通线程池,比jdk更灵活
    @Test
    public void testThreadPoolTaskExecutor(){
        Runnable task = new Runnable() {
            @Override
            public void run() {
                logger.debug("hello");
            }
        };
        for (int i = 0;i < 10 ; i++){
            threadPoolTaskExecutor.submit(task);
        }
        sleep(1000);
    }

    //spring可以执行定时任务的线程池
    @Test
    public void testThreadPoolTaskScheduler(){
        Runnable task = new Runnable() {
            @Override
            public void run() {
                logger.debug("hello");
            }
        };
        Date startTime = new Date(System.currentTimeMillis() + 10000);
        threadPoolTaskScheduler.scheduleAtFixedRate(task,startTime,1000);
        sleep(30000);
    }
}
