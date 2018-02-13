package com.yida.framework.blog;

import com.yida.framework.blog.utils.io.FileUtil;
import com.yida.framework.blog.utils.io.MarkdownFilenameFilter;

import java.util.List;

/**
 * @Author Lanxiaowei
 * @Date 2018-02-13 12:06
 * @Description 文件夹递归过滤测试用例
 */
public class FileRecursiveFilterTest {
    public static void main(String[] args) {
        String basePath = "C:/myblog/20180207";
        List<String> files = FileUtil.listFiles(basePath, new MarkdownFilenameFilter(), true);
        for (String fileName : files) {
            System.out.println(fileName);
        }
    }

}
