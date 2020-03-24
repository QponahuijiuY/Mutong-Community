package com.mutong.jcommunity;

/**
 * @description:
 * @Author: Mutong
 * @Date: 2020-03-21 18:46
 * @time_complexity: O()
 */
public class PC {
    public static void main(String[] args) {
        Data data = new Data();
        new Thread(()->{
            for (int i = 0 ; i < 10 ; i++) {
                try {
                    data.increament();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        },"A").start();
        new Thread(()->{
            for (int i = 0 ; i < 10 ; i++) {
                try {
                    data.decreament();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        },"B").start();
        new Thread(()->{
            for (int i = 0 ; i < 10 ; i++) {
                try {
                    data.increament();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        },"C").start();
        new Thread(()->{
            for (int i = 0 ; i < 10 ; i++) {
                try {
                    data.decreament();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        },"D").start();
    }
}
class Data{
    private static int i = 0;
    synchronized void increament() throws InterruptedException {
        while (i != 0){
            this.wait();
        }
        i++;
        System.out.println(i);
        this.notify();
    }
    synchronized void decreament() throws InterruptedException {
        while (i == 0){
            this.wait();
        }
        i--;
        System.out.println(i);
        this.notify();
    }
}
