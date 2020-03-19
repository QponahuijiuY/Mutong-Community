package com.mutong.jcommunity;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.dao.DataAccessException;
import org.springframework.data.redis.connection.RedisConnection;
import org.springframework.data.redis.core.RedisCallback;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;

/**
 * @description:
 * @Author: Mutong
 * @Date: 2020-03-11 13:30
 * @time_complexity: O()
 */
@RunWith(SpringRunner.class)
@SpringBootTest
@ContextConfiguration(classes = JcommunityApplication.class)
public class RedisTests {
    @Autowired
    private RedisTemplate redisTemplate;

    @Test
    public void testStrings(){
        String redisKey = "test:count";
        redisTemplate.opsForValue().set(redisKey,1);

        System.out.println(redisTemplate.opsForValue().get(redisKey));
        System.out.println(redisTemplate.opsForValue().increment(redisKey));
        System.out.println(redisTemplate.opsForValue().decrement(redisKey));
    }

    @Test
    public void testHyperLogLog(){
        String redisKey = "test:h11:01";
        for (int i = 1; i <= 100000; i ++){
            redisTemplate.opsForHyperLogLog().add(redisKey,i);
        }
        for (int i = 1; i <= 100000; i++){
            int r = (int)(Math.random() * 100000 + 1);
            redisTemplate.opsForHyperLogLog().add(redisKey,r);
        }
        long size = redisTemplate.opsForHyperLogLog().size(redisKey);
        System.out.println(size);
    }

    //统计一组数据的布尔值
    @Test
    public void testBitMap(){
        String rediskey = "test:bm:01";
        redisTemplate.opsForValue().setBit(rediskey,1, true);
        redisTemplate.opsForValue().setBit(rediskey,4, true);
        redisTemplate.opsForValue().setBit(rediskey,7, true);

        //查
        System.out.println(redisTemplate.opsForValue().getBit(rediskey,0));
        System.out.println(redisTemplate.opsForValue().getBit(rediskey,1));
        System.out.println(redisTemplate.opsForValue().getBit(rediskey,2));
        System.out.println(redisTemplate.opsForValue().getBit(rediskey,3));
        System.out.println(redisTemplate.opsForValue().getBit(rediskey,4));
        //统计
        Object object = redisTemplate.execute(new RedisCallback() {
            @Override
            public Object doInRedis(RedisConnection connection) throws DataAccessException {
                return connection.bitCount(rediskey.getBytes());
            }
        });
        System.out.println(object);
    }

}
