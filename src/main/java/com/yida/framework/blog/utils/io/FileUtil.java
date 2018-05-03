package com.yida.framework.blog.utils.io;

import com.yida.framework.blog.utils.Constant;
import com.yida.framework.blog.utils.common.GerneralUtil;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import sun.nio.ch.FileChannelImpl;

import javax.sql.rowset.serial.SerialBlob;
import javax.sql.rowset.serial.SerialClob;
import javax.sql.rowset.serial.SerialException;
import java.io.*;
import java.lang.reflect.Method;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.channels.FileLock;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.security.AccessController;
import java.security.PrivilegedAction;
import java.sql.Blob;
import java.sql.Clob;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @Author Lanxiaowei
 * @Date 2018-01-14 22:12
 * @Description 文件IO操作工具类
 */
public class FileUtil {
    public static String PATH_SEPERATOR = File.separator;
    private static Logger log = LogManager.getLogger(FileUtil.class.getName());

    static {
        String property = System.getProperties().getProperty("file.separator");
        if (property != null) {
            PATH_SEPERATOR = property;
        }
    }

    /**
     * 文件复制
     *
     * @param srcpath      文件源路径
     * @param filename     文件名称
     * @param destpath     文件目标路径
     * @param deleteSource 复制完成之后是否删除源文件
     */
    public static void copyFile(String srcpath, String filename, String destpath, boolean deleteSource) {
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
            if (deleteSource) {
                //文件复制完成后，删除源文件
                source.getAbsoluteFile().delete();
            }
        }
    }

    /**
     * 文件复制
     *
     * @param srcpath  文件源路径
     * @param filename 文件名称
     * @param destpath 文件目标路径
     */
    public static void copyFile(String srcpath, String filename, String destpath) {
        copyFile(srcpath, filename, destpath, false);
    }

    /**
     * 目录复制
     *
     * @param srcPath        待复制的目录
     * @param targetPath     目录下的所有文件和文件夹复制到哪里
     * @param filenameFilter 文件名称过滤器
     * @param deleteSource   是否复制完成之后删除源文件
     * @param prefix         过滤掉指定后缀的文件或目录
     */
    public static void copyDirectory(String srcPath, String targetPath, FilenameFilter filenameFilter, boolean deleteSource, String prefix) {
        if (!targetPath.endsWith("/")) {
            targetPath = targetPath.replaceAll("\\\\", "/");
            targetPath += "/";
        }
        parseDir(srcPath, targetPath);
        File f = new File(srcPath);
        File[] fileList = f.listFiles(filenameFilter);
        if (null == fileList) {
            return;
        }
        String fileName = null;
        boolean checkPrefix = (null != prefix && !"".equals(prefix));
        for (File f1 : fileList) {
            if (checkPrefix) {
                fileName = f1.getName();
                if (prefix.startsWith(fileName)) {
                    continue;
                }
            }
            if (f1.isFile()) {
                copyFile(srcPath, f1.getName(), targetPath, deleteSource);
            }
            //判断是否是目录
            if (f1.isDirectory()) {
                copyDirectory(f1.getPath().toString(), targetPath + f1.getName(), filenameFilter, deleteSource, prefix);
            }
        }
    }

    /**
     * 目录复制
     *
     * @param srcPath        待复制的目录
     * @param targetPath     目录下的所有文件和文件夹复制到哪里
     * @param filenameFilter 文件名称过滤器
     * @param deleteSource   是否复制完成之后删除源文件
     */
    public static void copyDirectory(String srcPath, String targetPath, FilenameFilter filenameFilter, boolean deleteSource) {
        copyDirectory(srcPath, targetPath, filenameFilter, deleteSource, null);
    }

    /**
     * 目录复制
     *
     * @param srcPath        待复制的目录
     * @param targetPath     目录下的所有文件和文件夹复制到哪里
     * @param filenameFilter 文件名称过滤器
     */
    public static void copyDirectory(String srcPath, String targetPath, FilenameFilter filenameFilter) {
        copyDirectory(srcPath, targetPath, filenameFilter, false, null);
    }

    /**
     * 目录复制
     *
     * @param srcPath    待复制的目录
     * @param targetPath 目录下的所有文件和文件夹复制到哪里
     */
    public static void copyDirectory(String srcPath, String targetPath) {
        copyDirectory(srcPath, targetPath, null, false, null);
    }

    /**
     * 删除文件或者空目录
     *
     * @param path
     * @return
     */
    public static boolean deleteFileOrDirectory(String path) {
        try {
            return Files.deleteIfExists(Paths.get(path));
        } catch (IOException e) {
            log.error("deleting the file[{}] occured unexcepted exception:\n", path, e.getMessage(), e);
            return false;
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

    /**
     * 删除整个目录，包含所有子目录和文件
     *
     * @param path
     */
    public static void deleteDirs(String path) {
        File rootFile = new File(path);
        if (!rootFile.exists()) {
            return;
        }
        File[] files = rootFile.listFiles();
        if (null == files) {
            return;
        }
        for (int i = 0; i < files.length; i++) {
            File file = files[i];
            if (file.isDirectory()) {
                deleteDirs(file.getPath());
            } else {
                file.delete();
            }
        }
        rootFile.delete();
    }

    /**
     * 删除指定文件
     *
     * @param filePath
     * @return
     */
    public static boolean deleteFile(String filePath) {
        File file = new File(filePath);
        if (!file.exists()) {
            return false;
        }
        return file.delete();
    }

    /**
     * 文件复制
     *
     * @param is    输入流
     * @param os    输出流
     * @param close 写入之后是否需要关闭OutputStream
     * @throws IOException
     */
    public static int copy(InputStream is, OutputStream os, boolean close) {
        try {
            return IOUtil.copy(is, os);
        } catch (IOException e1) {
            e1.printStackTrace();
        } finally {
            if (close) {
                try {
                    is.close();
                    os.close();
                } catch (IOException e2) {
                    e2.printStackTrace();
                }
            }
        }
        return 0;
    }

    /**
     * 文件复制
     *
     * @param inputName
     * @param outputName
     * @return
     */
    public static boolean copyFile(String inputName, String outputName) {
        InputStream is = null;
        OutputStream os = null;
        int copyed = 0;
        try {
            is = new FileInputStream(inputName);
            os = new FileOutputStream(outputName);
            copyed = copy(is, os, true);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        return copyed > 0;
    }

    /**
     * 文件夹复制
     *
     * @param srcFolder
     * @param destFolder
     * @return
     */
    public static boolean copyDirctory(String srcFolder, String destFolder) {
        File srcFile = new File(srcFolder);
        File destFile = new File(destFolder);
        if (!srcFile.exists() || (srcFile.isDirectory() && destFile.isFile())) {
            return false;
        }
        //文件copy到文件
        if (srcFile.isFile() && destFile.isFile()) {
            return copyFile(srcFolder, destFolder);
        }
        //创建目标目录
        if (!destFile.exists() && !destFile.isFile()) {
            destFile.mkdir();
        }
        //文件copy到目录
        if (srcFile.isFile()) {
            String srcFileName = srcFile.getName();
            String destFilePath = wrapFilePath(getFullFilePath(srcFileName, destFolder));
            return copyFile(wrapFilePath(srcFolder), destFilePath);
        }

        //目录copy到目录
        File[] allFiles = srcFile.listFiles();
        String srcName = null;
        String desName = null;
        for (File file : allFiles) {
            srcName = file.getName();
            if (file.isFile()) {
                desName = wrapFilePath(getFullFilePath(srcName, destFolder));
                copyFile(wrapFilePath(file.getAbsolutePath()), desName);
            } else {
                copyDirctory(wrapFilePath(file.getAbsolutePath()), getFullFilePath(srcName, destFolder));
            }
        }
        return true;
    }

    /**
     * 得到某文件夹下的所有文件
     *
     * @param path
     * @return
     */
    public static List<File> getAllFile(String path) {
        File file = new File(path);
        File[] files = file.listFiles();
        List<File> fileList = new ArrayList<File>();
        for (File f : files) {
            if (f.isFile()) {
                fileList.add(f);
            }
        }
        return fileList;
    }

    /**
     * @param @param  path
     * @param @return
     * @return List<File>
     * @throws
     * @Title: getAllFolder
     * @Description: 得到某文件夹下的所有文件夹
     */
    public static List<File> getAllFolder(String path) {
        File file = new File(path);
        File[] files = file.listFiles();
        List<File> folderList = new ArrayList<File>();
        for (File f : files) {
            if (f.isDirectory()) {
                folderList.add(f);
            }
        }
        return folderList;
    }

    /**
     * 递归遍历过滤指定文件夹下的所有文件和目录(返回数组形式)
     *
     * @param directoryPath 指定目录路径
     * @param filter        文件名称过滤器
     * @param recurse       是否开启递归查找,默认不开启递归查找,即默认为false
     * @return
     */
    public static String[] listFilesAsArray(String directoryPath, FilenameFilter filter, boolean recurse) {
        List<String> files = listFiles(directoryPath, filter, recurse);
        return files.toArray(new String[]{});
    }

    /**
     * 递归遍历过滤指定文件夹下的所有文件和目录(返回数组形式)
     *
     * @param directoryPath 指定目录路径
     * @param filter        文件名称过滤器
     * @return
     */
    public static String[] listFilesAsArray(String directoryPath, FilenameFilter filter) {
        return listFilesAsArray(directoryPath, filter, false);
    }

    /**
     * 递归遍历过滤指定文件夹下的所有文件和目录(返回数组形式)
     *
     * @param directoryPath 指定目录路径
     * @return
     */
    public static String[] listFilesAsArray(String directoryPath) {
        return listFilesAsArray(directoryPath, null, false);
    }

    /**
     * 递归遍历过滤指定文件夹下的所有文件和目录(返回List集合形式)
     *
     * @param directoryPath 指定目录路径
     * @param filter        文件名称过滤器
     * @param recurse       是否开启递归查找,默认不开启递归查找,即默认为false
     * @return
     */
    public static List<String> listFiles(String directoryPath, FilenameFilter filter, boolean recurse) {
        List<String> files = new ArrayList<>();
        File directory = new File(directoryPath);
        File[] entries = directory.listFiles();
        if (null == entries) {
            return null;
        }

        List<String> flist = null;
        // Go over entries
        try {
            for (File entry : entries) {
                if (null == filter || filter.accept(directory, entry.getName())) {
                    files.add(entry.getCanonicalPath());
                }
                if (recurse && entry.isDirectory()) {
                    flist = listFiles(entry.getCanonicalPath(), filter, recurse);
                    if (null != flist && flist.size() > 0) {
                        files.addAll(flist);
                    }
                }
            }
        } catch (Exception e) {
            log.error("list files in the directory[{}] with fileNameFilter[{}] occur exception:\n{}", directoryPath,
                    filter.getClass().getName(), e.getMessage());
        }
        return files;
    }

    /**
     * 递归遍历过滤指定文件夹下的所有文件和目录(返回List集合形式)
     *
     * @param directoryPath 指定目录路径
     * @param filter        文件名称过滤器
     * @return
     */
    public static List<String> listFiles(String directoryPath, FilenameFilter filter) {
        return listFiles(directoryPath, filter, false);
    }

    /**
     * 递归遍历过滤指定文件夹下的所有文件和目录(返回List集合形式)
     *
     * @param directoryPath 指定目录路径
     * @return
     */
    public static List<String> listFiles(String directoryPath) {
        return listFiles(directoryPath, null, false);
    }

    /**
     * 文件重命名
     *
     * @param path    文件的存放路径
     * @param oldname 文件的原始名称
     * @param newname 文件的新名称
     * @return
     */
    public static boolean renameFile(String path, String oldname, String newname) {
        if (oldname.equals(newname)) {
            return false;
        }
        File oldfile = null;
        File newfile = null;
        if (path.endsWith("/")) {
            oldfile = new File(path + oldname);
            newfile = new File(path + newname);
        } else {
            oldfile = new File(path + "/" + oldname);
            newfile = new File(path + "/" + newname);
        }
        if (!oldfile.exists() || newfile.exists()) {
            return false;
        }
        try {
            return oldfile.renameTo(newfile);
        } catch (Exception e) {
            log.error("Rename the file[{?}] have occured unexcepted exception:\n{?}", oldfile.getAbsolutePath(), e.getMessage());
            return false;
        }
    }

    /**
     * 读取文件
     *
     * @param filePath
     * @return
     * @throws IOException
     */
    public static String readFile(String filePath) throws IOException {
        return readFile(filePath, "UTF-8");
    }

    /**
     * 读取文件
     *
     * @param filePath
     * @return
     * @throws IOException
     */
    public static String readFile(String filePath, String charset) throws IOException {
        InputStreamReader isr = new InputStreamReader(new FileInputStream(new File(filePath)), charset);
        BufferedReader in = new BufferedReader(isr);
        StringBuffer content = new StringBuffer();
        String line = null;
        while ((line = in.readLine()) != null) {
            content.append(line).append("\n");
        }
        return content.toString().replaceAll("\n$", "");
    }

    /**
     * 读取文件
     *
     * @param file
     * @return
     * @throws IOException
     */
    public static String readFile(File file, String charset) throws IOException {
        InputStreamReader isr = new InputStreamReader(new FileInputStream(file), charset);
        BufferedReader in = new BufferedReader(isr);
        StringBuffer content = new StringBuffer();
        String line = null;
        while ((line = in.readLine()) != null) {
            content.append(line).append("\n");
        }
        return content.toString().replaceAll("\n$", "");
    }

    /**
     * 读取文件
     *
     * @param file
     * @return
     * @throws IOException
     */
    public static String readFile(File file) throws IOException {
        return readFile(file, "UTF-8");
    }

    /**
     * 把字符串以指定编码写入文件，<br/>可以指定写入方式：追加/覆盖
     *
     * @param content  写入的字符串
     * @param filePath 文件保存路径
     * @param charset  写入编码
     * @param append   是否追加
     */
    public static void writeFile(String content, String filePath, String charset, boolean append) {
        BufferedWriter writer = null;
        OutputStream os = null;
        OutputStreamWriter osw = null;
        try {
            os = new FileOutputStream(filePath, append);
            osw = new OutputStreamWriter(os, charset);
            writer = new BufferedWriter(osw);
            writer.write(content);
            writer.flush();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                os.close();
                osw.close();
                writer.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 把字符串以指定编码写入文件，<br/>可以指定写入方式：追加/覆盖
     * <br/>默认写入方式为覆盖
     *
     * @param content  写入的字符串
     * @param filePath 文件保存路径
     * @param charset  写入编码
     */
    public static void writeFile(String content, String filePath, String charset) {
        writeFile(content, filePath, charset, false);
    }

    /**
     * 把字符串以指定编码写入文件，<br/>可以指定写入方式：追加/覆盖
     * <br/>默认写入编码为UTF-8
     *
     * @param content  写入的字符串
     * @param filePath 文件保存路径
     * @param append   是否追加
     */
    public static void writeFile(String content, String filePath, boolean append) {
        writeFile(content, filePath, "UTF-8", append);
    }

    /**
     * 把字符串以指定编码写入文件，<br/>可以指定写入方式：追加/覆盖
     * <br/>默认写入方式为覆盖,默认写入编码为UTF-8
     *
     * @param content  写入的字符串
     * @param filePath 文件保存路径
     */
    public static void writeFile(String content, String filePath) {
        writeFile(content, filePath, "UTF-8", false);
    }

    /**
     * 下载文件
     *
     * @param link
     * @param filePath
     * @throws IOException
     */
    public static void download(String link, String filePath)
            throws IOException {
        URL url = new URL(link);
        BufferedReader in = new BufferedReader(new InputStreamReader(
                url.openStream()));
        String inputLine = null;
        FileOutputStream fos = new FileOutputStream(filePath);
        while ((inputLine = in.readLine()) != null) {
            fos.write(inputLine.getBytes());
        }
        in.close();
        fos.close();
    }

    /**
     * 获取远程文件的输入流并以字节数组形式返回
     *
     * @return
     */
    public static byte[] getBinaryDataFromURL(String link) throws IOException {
        BufferedInputStream bis = null;
        HttpURLConnection httpUrl = null;
        URL url = null;
        try {
            url = new URL(link);
            httpUrl = (HttpURLConnection) url.openConnection();
            httpUrl.connect();
            bis = new BufferedInputStream(httpUrl.getInputStream());
            return inputStream2ByteArray(bis);
        } catch (IOException e) {
            return null;
        } finally {
            httpUrl.disconnect();
        }
    }

    /**
     * 获取远程文件的输入流并以输出流返回
     *
     * @return
     */
    public static OutputStream getOutputStreamFromURL(String link) throws IOException {
        BufferedInputStream bis = null;
        HttpURLConnection httpUrl = null;
        URL url = null;
        try {
            url = new URL(link);
            httpUrl = (HttpURLConnection) url.openConnection();
            httpUrl.connect();
            bis = new BufferedInputStream(httpUrl.getInputStream());
            OutputStream os = inputStream2OutputStream(bis);
            return os;
        } catch (IOException e) {
            return null;
        } finally {
            if (null != bis) {
                bis.close();
            }
            httpUrl.disconnect();
        }
    }

    /**
     * 字符串转换成Clob
     *
     * @param string
     * @return
     */
    public static Clob string2Clob(String string) {
        if (GerneralUtil.isEmptyString(string)) {
            return null;
        }
        try {
            return new SerialClob(string.toCharArray());
        } catch (SerialException e) {
            return null;
        } catch (SQLException e) {
            return null;
        }
    }

    /**
     * Clob转换成字符串
     *
     * @param clob
     * @return
     */
    public static String clob2String(Clob clob) {
        if (null == clob) {
            return null;
        }
        try {
            return clob.getSubString(1L, (int) clob.length());
        } catch (SQLException e) {
            return null;
        }
    }

    /**
     * 字节数组转换成Clob
     *
     * @param byteArray
     * @param charsetName
     * @return
     */
    public static Clob byteArray2Clob(byte[] byteArray, String charsetName) {
        if (null == byteArray) {
            return null;
        }
        try {
            String string = new String(byteArray,
                    charsetName == null ? Constant.DEFAULT_CHARSET_UTF8
                            : charsetName);
            return string2Clob(string);
        } catch (UnsupportedEncodingException e) {
            return null;
        }
    }

    /**
     * 字节数组转换成Clob(重载) 若不显式指定编码，默认编码为UTF-8
     *
     * @param byteArray
     * @return
     */
    public static Clob byteArray2Clob(byte[] byteArray) {
        return byteArray2Clob(byteArray, null);
    }

    /**
     * Clob转换成字节数组
     *
     * @param clob
     * @return
     */
    public static byte[] clob2ByteArray(Clob clob) {
        if (null == clob) {
            return null;
        }
        InputStream in = null;
        byte[] byteArray = null;
        int length = 0;
        try {
            length = (int) clob.length();
            byteArray = new byte[length];
            in = clob.getAsciiStream();
        } catch (SQLException e) {
            return null;
        }
        int offset = 0;
        int n = 0;
        try {
            do {
                n = in.read(byteArray, offset, length - offset);
            } while (n != -1);
        } catch (IOException e) {
            return null;
        } finally {
            try {
                in.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return byteArray;
    }

    /**
     * 字节数组转换成Blob
     *
     * @param byteArray
     * @return
     * @throws SQLException
     * @throws SerialException
     */
    public static Blob byteArray2Blob(byte[] byteArray) {
        if (null == byteArray) {
            return null;
        }
        try {
            return new SerialBlob(byteArray);
        } catch (SerialException e) {
            return null;
        } catch (SQLException e) {
            return null;
        }
    }

    /**
     * Blob转换成字节数组
     *
     * @param blob
     * @return
     */
    public static byte[] blob2ByteArray(Blob blob) {
        BufferedInputStream is = null;
        try {
            is = new BufferedInputStream(blob.getBinaryStream());
            byte[] bytes = new byte[(int) blob.length()];
            int len = bytes.length;
            int offset = 0;
            int read = 0;
            while (offset < len
                    && (read = is.read(bytes, offset, len - offset)) != -1) {
                offset += read;
            }
            return bytes;
        } catch (SQLException e) {
            return null;
        } catch (IOException e) {
            return null;
        } finally {
            try {
                is.close();
                is = null;
            } catch (IOException e) {
                return null;
            }
        }
    }

    /**
     * 字节数组转换成输入流
     *
     * @param byteArray 字节数组
     * @return
     */
    public static InputStream byteArray2InputStream(byte[] byteArray) {
        return new ByteArrayInputStream(byteArray);
    }

    /**
     * 输入流转换成字节数组
     *
     * @param is 输入流对象
     * @return
     * @throws IOException
     */
    public static byte[] inputStream2ByteArray(InputStream is)
            throws IOException {
        ByteArrayOutputStream bytestream = new ByteArrayOutputStream();
        int ch = 0;
        while ((ch = is.read()) > 0) {
            bytestream.write(ch);
        }
        byte imgdata[] = bytestream.toByteArray();
        bytestream.close();
        is.close();
        return imgdata;
    }

    /**
     * String转换成输入流
     *
     * @param text
     * @param charset
     * @return
     */
    public static InputStream string2InputStream(String text, String charset) {
        try {
            return new ByteArrayInputStream(text.getBytes(charset));
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 输入流转换成String(速度快但耗内存)
     * 此方法是一次读一行，所以100%不会出现中文乱码，除非你内容自身就是乱码
     *
     * @param is
     * @return
     * @throws IOException
     */
    public static String inputStream2String(InputStream is) throws IOException {
        if (null == is) {
            return null;
        }
        BufferedReader in = new BufferedReader(new InputStreamReader(is, Constant.DEFAULT_CHARSET_UTF8));
        StringBuffer buffer = new StringBuffer();
        String line = "";
        while ((line = in.readLine()) != null) {
            buffer.append(line).append("\n");
        }
        return buffer.toString();
    }

    /**
     * 输入流转换成String(可以指定编码)
     * 此方法是一次读一行，所以100%不会出现中文乱码，除非你内容自身就是乱码
     *
     * @param is
     * @return
     * @throws IOException
     */
    public static String inputStream2String(InputStream is, String charsetName) throws IOException {
        if (null == is) {
            return null;
        }
        BufferedReader in = new BufferedReader(new InputStreamReader(is, (null == charsetName || charsetName.length() == 0) ? Charset.defaultCharset() : Charset.forName(charsetName)));
        StringBuffer buffer = new StringBuffer();
        String line = "";
        while ((line = in.readLine()) != null) {
            buffer.append(line).append("\n");
        }
        return buffer.toString();
    }

    /**
     * 输入流转换成String(消耗资源少但速度慢)
     * 对于中文，可能出现中文乱码，因为一个中文汉字占3个字节，比如刚好读到4094个字节都正常，此时缓冲区只剩2个字节，
     * 而假如下一个字符刚好是汉字，那就杯具了，乱码就产生了。
     *
     * @param is
     * @return
     * @throws IOException
     */
    public static String inputStreamToString(InputStream is) throws IOException {
        if (null == is) {
            return null;
        }
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        byte[] bt = new byte[4096];
        int i = -1;
        while ((i = is.read(bt)) > 0) {
            bos.write(bt, 0, i);
        }
        return bos.toString();
    }

    /**
     * 文件转换成输入流
     *
     * @param file
     * @return
     */
    public static InputStream file2InputStream(File file) {
        try {
            return new FileInputStream(file);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 输入流写入文件
     *
     * @param is       输入流
     * @param filePath 文件保存目录路径
     * @throws IOException
     */
    public static void write2File(InputStream is, String filePath)
            throws IOException {
        OutputStream os = new FileOutputStream(filePath);
        int len = 8192;
        byte[] buffer = new byte[len];
        while ((len = is.read(buffer, 0, len)) != -1) {
            os.write(buffer, 0, len);
        }
        os.close();
        is.close();
    }

    /**
     * 输入流写入文件
     *
     * @param is       输入流
     * @param filePath 文件保存目录路径
     * @param append   是否追加
     * @throws IOException
     */
    public static void write2File(InputStream is, String filePath, boolean append)
            throws IOException {
        OutputStream os = new FileOutputStream(filePath, append);
        int len = 8192;
        byte[] buffer = new byte[len];
        while ((len = is.read(buffer, 0, len)) != -1) {
            os.write(buffer, 0, len);
        }
        os.close();
        is.close();
    }

    /**
     * 获取文件的完整路径
     *
     * @param fileName 文件名称
     * @param filePath 文件保存路径
     * @return
     */
    public static String getFullFilePath(String fileName, String filePath) {
        if (GerneralUtil.isEmptyString(filePath)
                || GerneralUtil.isEmptyString(fileName)) {
            return null;
        }
        filePath = wrapFilePath(filePath);
        return filePath + fileName;
    }

    /**
     * 转换文件路径中的\\为/
     *
     * @param filePath
     * @return
     */
    public static String wrapFilePath(String filePath) {
        if (filePath.split("\\\\").length > 1) {
            filePath = filePath.replace("\\", "/");
        }
        if (!filePath.endsWith("/")) {
            filePath = filePath + "/";
        }
        return filePath;
    }

    /**
     * 从文件路径中获取文件所在路径
     *
     * @param fullPath 文件全路径
     * @return 文件所在路径
     */
    public static String getFileDir(String fullPath) {
        int iPos1 = fullPath.lastIndexOf("/");
        int iPos2 = fullPath.lastIndexOf("\\");
        if (-1 == iPos1 && -1 == iPos2) {
            return fullPath;
        }
        iPos1 = (iPos1 > iPos2 ? iPos1 : iPos2);
        return fullPath.substring(0, iPos1 + 1);
    }

    /**
     * 从文件路径中获取文件名称(包含后缀名)
     *
     * @param fullPath
     * @return
     */
    public static String getFileName(String fullPath) {
        if (GerneralUtil.isEmptyString(fullPath)) {
            return "";
        }
        int iPos1 = fullPath.lastIndexOf("/");
        int iPos2 = fullPath.lastIndexOf("\\");
        if (-1 == iPos1 && -1 == iPos2) {
            return fullPath;
        }
        iPos1 = (iPos1 > iPos2 ? iPos1 : iPos2);
        return fullPath.substring(iPos1 + 1);
    }

    /**
     * 从URL链接中提取文件名称
     *
     * @param url
     * @return
     */
    public static String getFileNameFromUrl(String url) {
        if (GerneralUtil.isEmptyString(url)) {
            return "";
        }
        if (url.endsWith("/")) {
            url = url.substring(0, url.length() - 1);
        }
        url = url.replaceAll("(?:http|https)://www\\.([\\s\\S]*)", "$1");
        return getFileName(url);
    }

    /**
     * 从文件路径中获取文件名称(去除后缀名)
     *
     * @param fullPath
     * @return
     */
    public static String getPureFileName(String fullPath) {
        String fileFullName = getFileName(fullPath);
        int index = fileFullName.lastIndexOf(".");
        if (index != -1) {
            return fileFullName.substring(0, index);
        }
        return fileFullName;
    }

    /**
     * 获得文件名中的后缀名
     *
     * @param fileName 源文件名
     * @return String 后缀名
     */
    public static String getFileSuffix(String fileName) {
        int index = fileName.lastIndexOf(".");
        if (index != -1) {
            return fileName.substring(index + 1, fileName.length());
        }
        return fileName;
    }

    /**
     * 从CLASSPATH路径下加载指定文件
     *
     * @param fileName 文件名称
     * @return
     */
    public static String getFileFromClassPath(String fileName) {
        URL url = FileUtil.class.getClassLoader().getResource(fileName);
        String filepath = url.getFile();
        File file = new File(filepath);
        byte[] retBuffer = new byte[(int) file.length()];
        try {
            FileInputStream fis = new FileInputStream(filepath);
            fis.read(retBuffer);
            fis.close();
            return new String(retBuffer, "UTF-8");
        } catch (IOException e) {
            return null;
        }
    }

    /**
     * 从URL链接中猜测文件名称
     *
     * @param url
     * @return
     */
    public static String guessFileNameFromUrl(String url) {
        String reg = "(/|=)([^/&?]+\\.[a-zA-Z]+)";
        if (GerneralUtil.isEmptyString(url) || Pattern.compile(reg).matcher(url).find()) {
            return "UnknowName.temp";
        }
        Matcher matcher = Pattern.compile(reg).matcher(url);
        String s = "";
        while (matcher.find()) {
            s = matcher.group(2);
        }
        return s;
    }

    /**
     * 从Content-Disposition中提取文件名
     *
     * @param contentDisposition
     * @return
     */
    public static String getFileNameFromContentDisposition(String contentDisposition) {
        if (GerneralUtil.isEmptyString(contentDisposition)) {
            return "UnknowName.temp";
        }
        if (!contentDisposition.startsWith("attachment")) {
            return null;
        }
        return contentDisposition.substring(contentDisposition.indexOf("=") + 1);
    }

    /**
     * @param @param  is
     * @param @return
     * @return OutputStream
     * @throws
     * @Title: inputStream2OutputStream
     * @Description: 输入流写入到字节数组缓冲流
     */
    @SuppressWarnings("finally")
    public static OutputStream inputStream2OutputStream(InputStream is) {
        OutputStream os = new ByteArrayOutputStream();
        byte[] buf = new byte[Constant.BUFFER_SIZE];
        try {
            for (int readNum; (readNum = is.read(buf)) != -1; ) {
                os.write(buf, 0, readNum);
            }
        } catch (IOException ex) {
            ex.printStackTrace();
        } finally {
            try {
                is.close();
                os.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return os;
        }
    }

    /**
     * @param @param  is
     * @param @return
     * @return File
     * @throws
     * @Title: inputStream2File
     * @Description: 将输入流中数据写入文件
     */
    public static File inputStream2File(InputStream is, String fileSavePath) {
        OutputStream outputStream = null;
        File file = new File(fileSavePath);
        try {
            outputStream = new FileOutputStream(file);

            int read = 0;
            byte[] bytes = new byte[1024];

            while ((read = is.read(bytes)) != -1) {
                outputStream.write(bytes, 0, read);
            }

        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (is != null) {
                try {
                    is.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (outputStream != null) {
                try {
                    outputStream.flush();
                    outputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return file;
    }

    /**
     * @param @return
     * @return String
     * @throws
     * @Title: randomFileName
     * @Description: 生成随机文件名
     */
    public static String randomFileName() {
        String id = UUID.randomUUID().toString();
        id = id.replace("-", "");
        int num = GerneralUtil.generateRandomNumber(1000000, 0);
        if (num % 2 == 0) {
            id = id.toUpperCase();
        }
        return id;
    }

    /**
     * @param @param  filePath  文件的相对路径(相对于src)
     * @param @return
     * @return String
     * @throws
     * @Title: getAbsolutePath
     * @Description: 根据文件的相对路径获取该文件的硬盘绝对路径，
     * 如src目录下的xxxx.properties文件绝对路径为D:/projectName/bin/xxxx.properties
     */
    public static String getAbsolutePath(String filePath) {
        String path = FileUtil.class.getClassLoader().getResource(filePath).getPath();
        if (path.startsWith("/")) {
            path = path.substring(1);
        }
        if (-1 != path.indexOf("/") && path.lastIndexOf("/") != path.indexOf("/")) {
            path = path.substring(0, path.lastIndexOf("/"));
        }
        if (!path.endsWith("/")) {
            path += "/";
        }
        return path;
    }

    public static void checkAndMakeParentDirecotry(String fullName) {
        int index = fullName.lastIndexOf(PATH_SEPERATOR);
        if (index > 0) {
            String path = fullName.substring(0, index);
            File file = new File(path);
            if (!file.exists()) {
                file.mkdirs();
            }
        }
    }
}
