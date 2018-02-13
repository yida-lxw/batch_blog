package com.yida.framework.blog;

import com.yida.framework.blog.utils.io.FileUtil;

/**
 * @Author Lanxiaowei
 * @Date 2018-01-14 22:40
 * @Description 文件夹复制测试
 */
public class FileCopyTest {
    public static void main(String[] args) {
        String srcpath = "C:/myblog/20180207/";
        String targetpath = "G:/test4/20180207/";
        FileUtil.copyDirectory(srcpath, targetpath, null, false);
    }
}
