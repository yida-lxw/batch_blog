package com.yida.framework.blog.utils.io;

import java.io.File;
import java.io.IOException;

/**
 * @Author Lanxiaowei
 * @Date 2018-01-14 21:18
 * @Description 这里是类的描述信息
 */
public interface Archiver {
    /**
     * 压缩文件
     *
     * @param srcFiles 待压缩的源文件
     * @param destpath 目标输出路径
     * @throws IOException
     */
    boolean doArchiver(File[] srcFiles, String destpath) throws IOException;

    /**
     * 解压文件
     *
     * @param srcfile  需要解压的源文件
     * @param destpath 目标输出路径
     */
    boolean doUnArchiver(File srcfile, String destpath) throws IOException;
}
