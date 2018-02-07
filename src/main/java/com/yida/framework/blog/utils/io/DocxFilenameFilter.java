package com.yida.framework.blog.utils.io;

import java.io.File;
import java.io.FilenameFilter;

/**
 * @Author Lanxiaowei
 * @Date 2018-02-07 16:22
 * @Description Office的Docx文件过滤器
 */
public class DocxFilenameFilter implements FilenameFilter {
    @Override
    public boolean accept(File dir, String name) {
        name = name.toLowerCase();
        if (name.endsWith(".docx") || name.endsWith(".DOCX")) {
            return new File(dir + "/" + name).isFile();
        }
        return false;
    }
}
