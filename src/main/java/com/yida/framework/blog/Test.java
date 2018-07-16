package com.yida.framework.blog;

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * @Author Lanxiaowei
 * @Date 2018-07-09 18:05
 * @Description 这里是类的描述信息
 */
public class Test {
    static boolean a = true;

    public static void main(String[] args) throws InterruptedException {
        Lock lock = new ReentrantLock();
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    System.out.println("Enter...");
                    System.out.println("" +
                            "begin to sleep 3000ms...");
                    Thread.sleep(3000);
                    System.out.println("" +
                            "have sleeped 3000ms...");


                    while (a) {
                        //System.out.println("Run...");
                        //lock.lock();

                        //lock.unlock();

                        /*synchronized (this) {

                        }*/
                    }
                    System.out.println("Leave...");
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }).start();
        Thread.sleep(2000);
        a = false;
        System.out.println("a:" + a);
    }

    private static void foo(boolean a) {
    }
}
