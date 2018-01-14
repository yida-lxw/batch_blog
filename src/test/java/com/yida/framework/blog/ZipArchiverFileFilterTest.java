package com.yida.framework.blog;

import com.yida.framework.blog.utils.io.ZipArchiverFileFilter;

import java.io.File;
import java.io.IOException;

/**
 * @Author Lanxiaowei
 * @Date 2018-01-14 21:49
 * @Description 解压缩docx文件测试
 */
public class ZipArchiverFileFilterTest {
    public static void main(String[] args) throws IOException {
        String srcfile = "C:/a.zip";
        String outputFilePath = "C:/zip/";
        ZipArchiverFileFilter zipArchiverFileFilter = new ZipArchiverFileFilter();
        zipArchiverFileFilter.doUnArchiver(new File(srcfile), outputFilePath);
    }
}
