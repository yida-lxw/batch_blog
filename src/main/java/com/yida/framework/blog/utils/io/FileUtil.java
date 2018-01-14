package com.yida.framework.blog.utils.io;

import java.io.*;
import java.lang.reflect.Method;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;
import java.security.AccessController;
import java.security.PrivilegedAction;

/**
 * @Author Lanxiaowei
 * @Date 2018-01-14 22:12
 * @Description 文件IO操作工具类
 */
public class FileUtil {
    public static void copyFile(String srcpath, String filename, String destpath) {
        if (!srcpath.endsWith("/")) {
            srcpath = srcpath.replaceAll("\\\\", "/");
            srcpath += "/";
        }
        if (!destpath.endsWith("/")) {
            destpath = destpath.replaceAll("\\\\", "/");
            destpath += "/";
        }
        File source = new File(srcpath + filename);
        FileChannel fileChannel = null;
        File dest = new File(destpath + filename);
        FileChannel out = null;
        try {
            fileChannel = new RandomAccessFile(source, "rw").getChannel();
            //in = new FileInputStream(source).getChannel();
            out = new FileOutputStream(dest).getChannel();
            long size = fileChannel.size();
            MappedByteBuffer buf = fileChannel.map(FileChannel.MapMode.READ_ONLY, 0, size);
            out.write(buf);

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                fileChannel.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
            try {
                out.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
            //文件复制完成后，删除源文件
            source.delete();
        }
    }

    /**
     * 目录复制
     *
     * @param srcPath    待复制的目录
     * @param targetPath 目录下的所有文件和文件夹复制到哪里
     */
    public static void copyDirectory(String srcPath, String targetPath) {
        if (!targetPath.endsWith("/")) {
            targetPath = targetPath.replaceAll("\\\\", "/");
            targetPath += "/";
        }
        parseDir(srcPath, targetPath);
        File f = new File(srcPath);
        File[] fileList = f.listFiles(new ImageFilenameFilter());
        for (File f1 : fileList) {
            if (f1.isFile()) {
                copyFile(srcPath, f1.getName(), targetPath);
            }
            //判断是否是目录
            if (f1.isDirectory()) {
                copyDirectory(f1.getPath().toString(), targetPath + f1.getName());
            }
        }
    }

    /**
     * @param srcPath    需要复制的目标目录
     * @param targetPath 生成的目标文件目录
     * @Description: 复制一个目录下的所有目录文件(只复制目录结构)
     */
    public static void parseDir(String srcPath, String targetPath) {
        //创建一个新的目录
        File targetFile = new File(targetPath);
        if (!targetFile.exists()) {
            targetFile.mkdirs();
        }

        //创建一个抽象路径
        File file = new File(srcPath);
        if (file.isDirectory()) {
            File[] files = file.listFiles();
            for (File f : files) {
                if (f.isDirectory()) {
                    if (!targetPath.endsWith("/")) {
                        targetPath = targetPath.replaceAll("\\\\", "/");
                        targetPath += "/";
                    }
                    parseDir(f.getPath(), targetPath + f.getName());
                }
            }
        }
    }

    public static void clean(final Object buffer) throws Exception {
        AccessController.doPrivileged(new PrivilegedAction() {
            public Object run() {
                try {
                    Method getCleanerMethod = buffer.getClass().getMethod("cleaner", new Class[0]);
                    getCleanerMethod.setAccessible(true);
                    sun.misc.Cleaner cleaner = (sun.misc.Cleaner) getCleanerMethod.invoke(buffer, new Object[0]);
                    cleaner.clean();
                } catch (Exception e) {
                    e.printStackTrace();
                }
                return null;
            }
        });
    }
}
