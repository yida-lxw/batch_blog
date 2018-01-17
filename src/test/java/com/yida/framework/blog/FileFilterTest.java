package com.yida.framework.blog;

import com.yida.framework.blog.utils.io.DefaultArchiverFileFilter;

import java.io.File;

/**
 * @Author Lanxiaowei
 * @Date 2018-01-14 21:31
 * @Description 测试文件过滤器
 */
public class FileFilterTest {
    public static void main(String[] args) {
        String basePath = "C:/使用Java提交代码至Github";
        File file = new File(basePath);
        String[] files = file.list(new DefaultArchiverFileFilter());
        for (String fileName : files) {
            System.out.println(fileName);
        }
    }
}
