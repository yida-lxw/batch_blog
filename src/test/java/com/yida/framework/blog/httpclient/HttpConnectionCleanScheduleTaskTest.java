package com.yida.framework.blog.httpclient;

import com.yida.framework.blog.utils.httpclient.HttpConnectionCleanScheduleTask;

/**
 * @Author Lanxiaowei
 * @Date 2018-02-12 15:03
 * @Description Http空闲和过期链接清理定时任务的测试用例
 */
public class HttpConnectionCleanScheduleTaskTest {
    public static void main(String[] args) {
        HttpConnectionCleanScheduleTask httpConnectionCleanScheduleTask =
                HttpConnectionCleanScheduleTask.getInstance();
        httpConnectionCleanScheduleTask.start();
    }
}
