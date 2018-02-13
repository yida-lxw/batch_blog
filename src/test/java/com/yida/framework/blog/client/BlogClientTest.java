package com.yida.framework.blog.client;

/**
 * @Author Lanxiaowei
 * @Date 2018-02-13 18:26
 * @Description 博客发布工具的整体功能测试
 */
public class BlogClientTest {
    public static void main(String[] args) {
        BlogClient blogClient = new DefaultBlogClient();
        blogClient.go();
    }
}
