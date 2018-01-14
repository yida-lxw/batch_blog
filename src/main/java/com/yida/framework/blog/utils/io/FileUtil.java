package com.yida.framework.blog.utils.io;

import sun.nio.ch.FileChannelImpl;

import java.io.*;
import java.lang.reflect.Method;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.channels.FileLock;
import java.nio.file.Files;
import java.nio.file.Paths;
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
        RandomAccessFile randomAccessFile = null;
        FileChannel outChannel = null;
        FileOutputStream fos = null;
        MappedByteBuffer mappedByteBuffer = null;
        FileLock lock = null;
        try {
            randomAccessFile = new RandomAccessFile(source, "rw");
            fileChannel = randomAccessFile.getChannel();
            lock = fileChannel.tryLock();
            //in = new FileInputStream(source).getChannel();
            if (null != lock) {
                fos = new FileOutputStream(dest);
                outChannel = fos.getChannel();
                long size = fileChannel.size();
                mappedByteBuffer = fileChannel.map(FileChannel.MapMode.READ_ONLY, 0, size);
                outChannel.write(mappedByteBuffer);
                mappedByteBuffer.clear();
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                lock.release();
            } catch (IOException e) {
                e.printStackTrace();
            }
            try {
                outChannel.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
            try {
                fos.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
            try {
                fileChannel.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
            try {
                randomAccessFile.close();
            } catch (IOException e) {
                e.printStackTrace();
            }

            //删除之前需要先回收ByteBuffer占用的资源,否则文件无法被删除
            cleanByteBuffer(mappedByteBuffer);
            //文件复制完成后，删除源文件
            source.getAbsoluteFile().delete();
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

    /**
     * 删除空目录
     *
     * @param dir 待删除的目录路径
     */
    private static void doDeleteEmptyDir(String dir) {
        boolean success = (new File(dir)).delete();
        if (success) {
            System.out.println("Successfully deleted empty directory: " + dir);
        } else {
            System.out.println("Failed to delete empty directory: " + dir);
        }
    }

    /**
     * 递归删除目录下的所有文件及子目录下所有文件
     *
     * @param dir 将要删除的文件目录
     * @return boolean    只有全部删除成功，才会返回true
     */
    private static boolean deleteDir(File dir) {
        if (dir.isDirectory()) {
            String[] children = dir.list();
            //递归删除目录中的子目录下
            for (int i = 0; i < children.length; i++) {
                boolean success = deleteDir(new File(dir, children[i]));
                if (!success) {
                    return false;
                }
            }
        }
        // 目录此时为空，可以删除
        return dir.delete();
    }

    /**
     * 手动回收ByteBuffer占用的资源,因为DirectByteBuffer分配的是直接堆外内存，
     * 而堆外内存是不受JVM GC管辖范围内
     *
     * @param mappedByteBuffer
     */
    public static void cleanByteBuffer(MappedByteBuffer mappedByteBuffer) {
        AccessController.doPrivileged(new PrivilegedAction() {
            public Object run() {
                try {
                    Method m = FileChannelImpl.class.getDeclaredMethod("unmap",
                            MappedByteBuffer.class);
                    m.setAccessible(true);
                    m.invoke(FileChannelImpl.class, mappedByteBuffer);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                return null;
            }
        });
    }
}