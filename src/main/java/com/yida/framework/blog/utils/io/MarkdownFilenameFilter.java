package com.yida.framework.blog.utils.io;

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
        name = name.toLowerCase();
        if (name.endsWith(".md") || name.endsWith(".markdown")
                || name.endsWith(".MD") || name.endsWith(".MARKDOWN")) {
            return new File(dir + "/" + name).isFile();
        }
        return false;
    }
}
