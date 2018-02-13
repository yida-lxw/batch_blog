package com.yida.framework.blog;

import com.yida.framework.blog.utils.io.FileUtil;

/**
 * @Author Lanxiaowei
 * @Date 2018-02-13 14:56
 * @Description 为文件名称添加[ignore]前缀的测试用例
 */
public class FileRename2IgnoreTest {
    public static void main(String[] args) {
        String path1 = "C:\\myblog\\20180207\\欢迎使用有道云笔记.docx";
        String path2 = "C:/myblog/20180207/使用Java提交代码至Github.docx";
        String fileName1 = FileUtil.getFileName(path1);
        String fileName2 = FileUtil.getFileName(path2);

        System.out.println(fileName1);
        System.out.println(fileName2);

        String fileDir1 = FileUtil.getFileDir(path1);
        String fileDir2 = FileUtil.getFileDir(path2);
        System.out.println(fileDir1);
        System.out.println(fileDir2);
        //rename file
        FileUtil.renameFile(fileDir1, fileName1, "[ignore]" + fileName1);
        FileUtil.renameFile(fileDir2, fileName2, "[ignore]" + fileName2);
    }
}
