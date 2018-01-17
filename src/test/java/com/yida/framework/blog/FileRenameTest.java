package com.yida.framework.blog;

import com.yida.framework.blog.utils.io.FileUtil;
import com.yida.framework.blog.utils.io.ZipArchiverFileFilter;

import java.io.File;

/**
 * @Author Lanxiaowei
 * @Date 2018-01-17 21:56
 * @Description 文件的重命名测试
 */
public class FileRenameTest {
    public static void main(String[] args) {
        String path = "C:/";
        String oldName = "使用Java提交代码至Github.docx";
        String newName = "使用Java提交代码至Github.zip";
        boolean result = FileUtil.renameFile(path, oldName, newName);
        //如果文件重命名成功之后
        if (result) {
            //对文件进行解压
            ZipArchiverFileFilter zipArchiverFileFilter = new ZipArchiverFileFilter();
            boolean unzipResult = zipArchiverFileFilter.doUnArchiver(new File(path + newName), path + oldName.replace(".docx", ""));
            System.out.println(unzipResult ? "unzip successful" : "unzip failure");
        }
    }
}
