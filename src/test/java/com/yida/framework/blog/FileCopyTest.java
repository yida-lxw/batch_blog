package com.yida.framework.blog;

import com.yida.framework.blog.utils.io.FileUtil;

/**
 * @Author Lanxiaowei
 * @Date 2018-01-14 22:40
 * @Description 文件夹复制测试
 */
public class FileCopyTest {
    public static void main(String[] args) {
        String srcpath = "C:\\zip\\word\\media\\";
        String targetpath = "C:\\zip\\images\\";
        FileUtil.copyDirectory(srcpath, targetpath);
    }
}
