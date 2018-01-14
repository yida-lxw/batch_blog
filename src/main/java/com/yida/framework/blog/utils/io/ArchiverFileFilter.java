package com.yida.framework.blog.utils.io;

import java.io.File;
import java.io.FilenameFilter;

/**
 * @Author Lanxiaowei
 * @Date 2018-01-14 21:21
 * @Description 压缩文件过滤器
 */
public abstract class ArchiverFileFilter implements Archiver, FilenameFilter {
    @Override
    public boolean accept(File dir, String name) {
        if (name.endsWith(".zip") || name.endsWith(".ZIP")) {
            return new File(dir + "/" + name).isFile();
        }
        return false;
    }
}
