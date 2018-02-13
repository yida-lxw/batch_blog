package com.yida.framework.blog.utils.io;

import java.io.File;
import java.io.FilenameFilter;

/**
 * @Author Lanxiaowei
 * @Date 2018-01-14 21:59
 * @Description 图片文件过滤器
 */
public class ImageFilenameFilter implements FilenameFilter {
    @Override
    public boolean accept(File dir, String name) {
        String orignalName = name;
        name = name.toLowerCase();
        if (name.endsWith(".png") || name.endsWith(".PNG") ||
                name.endsWith(".jpg") || name.endsWith(".JPG") ||
                name.endsWith(".jpeg") || name.endsWith(".JPEG") ||
                name.endsWith(".gif") || name.endsWith(".GIF")) {
            return new File(dir + "/" + orignalName).isFile();
        }
        return false;
    }
}
