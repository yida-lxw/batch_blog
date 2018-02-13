package com.yida.framework.blog.utils.io;

import com.yida.framework.blog.utils.Constant;

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
        String orignalName = name;
        name = name.toLowerCase();
        if (name.endsWith(".docx")) {
            if (name.startsWith(Constant.IGNORE_MARK)) {
                return false;
            }
            return new File(dir + "/" + orignalName).isFile();
        }
        return false;
    }
}
