package com.yida.framework.blog;

import com.yida.framework.blog.utils.common.DateUtil;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

/**
 * @Author Lanxiaowei
 * @Date 2018-01-16 23:34
 * @Description DateUtil工具类功能测试
 */
public class DateUtilTest {
    public static void main(String[] args) {
        final String patten1 = "yyyy-MM-dd";
        final String patten2 = "yyyy-MM";

        Thread t1 = new Thread() {
            @Override
            public void run() {
                DateUtil.parse("1992-09-13", patten1);
            }
        };

        Thread t2 = new Thread() {
            @Override
            public void run() {
                DateUtil.parse("2000-09", patten2);
            }
        };

        Thread t3 = new Thread() {
            @Override
            public void run() {
                DateUtil.parse("1992-09-13", patten1);
            }
        };

        Thread t4 = new Thread() {
            @Override
            public void run() {
                DateUtil.parse("2000-09", patten2);
            }
        };

        Thread t5 = new Thread() {
            @Override
            public void run() {
                DateUtil.parse("2000-09-13", patten1);
            }
        };

        Thread t6 = new Thread() {
            @Override
            public void run() {
                DateUtil.parse("2000-09", patten2);
            }
        };

        System.out.println("单线程执行: ");
        ExecutorService exec = Executors.newFixedThreadPool(1);
        exec.execute(t1);
        exec.execute(t2);
        exec.execute(t3);
        exec.execute(t4);
        exec.execute(t5);
        exec.execute(t6);
        exec.shutdown();

        sleep(1000);

        System.out.println("双线程执行: ");
        ExecutorService exec2 = Executors.newFixedThreadPool(2);
        exec2.execute(t1);
        exec2.execute(t2);
        exec2.execute(t3);
        exec2.execute(t4);
        exec2.execute(t5);
        exec2.execute(t6);
        exec2.shutdown();
    }

    private static void sleep(long millSec) {
        try {
            TimeUnit.MILLISECONDS.sleep(millSec);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
