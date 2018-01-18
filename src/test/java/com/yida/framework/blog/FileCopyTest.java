package com.yida.framework.blog;

import com.yida.framework.blog.utils.io.FileUtil;
import com.yida.framework.blog.utils.io.ImageFilenameFilter;

/**
 * @Author Lanxiaowei
 * @Date 2018-01-14 22:40
 * @Description 文件夹复制测试
 */
public class FileCopyTest {
    public static void main(String[] args) {
        String srcpath = "G:\\zip\\word\\media\\";
        String targetpath = "G:\\zip\\images\\";
        FileUtil.copyDirectory(srcpath, targetpath, new ImageFilenameFilter());
    }
}
