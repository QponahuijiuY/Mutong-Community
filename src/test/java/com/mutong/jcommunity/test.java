package com.mutong.jcommunity;

import org.junit.Test;

import java.util.concurrent.ArrayBlockingQueue;

/**
 * @description:
 * @Author: Mutong
 * @Date: 2020-03-19 17:54
 * @time_complexity: O()
 */
public class test {
    public static void main(String[] args) throws InterruptedException {
        ArrayBlockingQueue arrayBlockingQueue2 = new ArrayBlockingQueue(5 );
        arrayBlockingQueue2.put("1");
        arrayBlockingQueue2.put("2");
        arrayBlockingQueue2.put("3");
        arrayBlockingQueue2.put("4");
        arrayBlockingQueue2.put("5");
        arrayBlockingQueue2.take();
        arrayBlockingQueue2.take();
        arrayBlockingQueue2.take();
        arrayBlockingQueue2.take();
        arrayBlockingQueue2.take();
        int i = arrayBlockingQueue2.remainingCapacity();
        System.out.println(i);
    }
    @Test
    public void test() throws InterruptedException {
        ArrayBlockingQueue arrayBlockingQueue = new ArrayBlockingQueue(5 ,true);
        arrayBlockingQueue.put("1");
        arrayBlockingQueue.put("2");
        arrayBlockingQueue.put("3");
        arrayBlockingQueue.put("4");
        arrayBlockingQueue.put("5");
        arrayBlockingQueue.poll();
        arrayBlockingQueue.poll();
        arrayBlockingQueue.poll();
        arrayBlockingQueue.poll();
        Object poll1 = arrayBlockingQueue.poll();
        System.out.println(poll1);
        Object poll = arrayBlockingQueue.poll();
        System.out.println(poll);

        int i = arrayBlockingQueue.remainingCapacity();
        System.out.println(i);
    }


}
