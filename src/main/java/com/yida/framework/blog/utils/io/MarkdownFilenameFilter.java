package com.yida.framework.blog.utils.io;

import com.yida.framework.blog.utils.Constant;

import java.io.File;
import java.io.FilenameFilter;

/**
 * @Author Lanxiaowei
 * @Date 2018-01-19 00:56
 * @Description Markdown文件过滤器
 */
public class MarkdownFilenameFilter implements FilenameFilter {
    @Override
    public boolean accept(File dir, String name) {
        String orignalName = name;
        name = name.toLowerCase();
        if (name.endsWith(".md") || name.endsWith(".markdown")) {
            if (name.startsWith(Constant.IGNORE_MARK)) {
                return false;
            }
            return new File(dir + "/" + orignalName).isFile();
        }
        return false;
    }
}
