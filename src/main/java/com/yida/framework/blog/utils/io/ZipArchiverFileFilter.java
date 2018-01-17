package com.yida.framework.blog.utils.io;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.*;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

/**
 * @Author Lanxiaowei
 * @Date 2018-01-14 21:43
 * @Description ZIP文件的压缩与解压缩
 */
public class ZipArchiverFileFilter extends DefaultArchiverFileFilter {
    private Logger log = LogManager.getLogger(ZipArchiverFileFilter.class.getName());

    @Override
    public boolean doUnArchiver(File srcfile, String destpath) {
        byte[] buf = new byte[1024];
        FileInputStream fis = null;
        try {
            fis = new FileInputStream(srcfile);
        } catch (FileNotFoundException e) {
            log.error("We can't found the file[{?}],thrown error exception:\n[{?}]", srcfile.getAbsolutePath(), e.getMessage(), e);
            return false;
        }
        BufferedInputStream bis = new BufferedInputStream(fis);
        ZipInputStream zis = new ZipInputStream(bis);
        ZipEntry zn = null;
        try {
            if (destpath.contains("\\\\")) {
                destpath = destpath.replaceAll("\\\\", "/");
            }
            File outputFile = null;
            while ((zn = zis.getNextEntry()) != null) {
                outputFile = new File(destpath.endsWith("/") ? destpath + zn.getName() : destpath + "/" + zn.getName());
                if (zn.isDirectory()) {
                    outputFile.mkdirs();
                } else {
                    //父目录不存在则创建
                    File parent = outputFile.getParentFile();
                    if (!parent.exists()) {
                        parent.mkdirs();
                    }
                    FileOutputStream fos = null;
                    BufferedOutputStream bos = null;
                    try {
                        fos = new FileOutputStream(outputFile);
                        bos = new BufferedOutputStream(fos);
                        int len;
                        while ((len = zis.read(buf)) != -1) {
                            bos.write(buf, 0, len);
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    } finally {
                        bos.flush();
                        bos.close();
                        fos.close();
                    }
                }
                zis.closeEntry();
            }
            return true;
        } catch (Exception e) {
            log.error("UnZip the DOCX File[{?}] have thrown error exception:\n[{?}]", srcfile.getAbsolutePath(), e.getMessage());
            return false;
        } finally {
            try {
                fis.close();
            } catch (IOException e) {
                log.error("Closing the FileInputStream have thrown error exception:\n[{?}]", e.getMessage());
            }
            try {
                bis.close();
            } catch (IOException e) {
                log.error("Closing the BufferedInputStream have thrown error exception:\n[{?}]", e.getMessage());
            }
            try {
                zis.close();
            } catch (IOException e) {
                log.error("Closing the ZipInputStream have thrown error exception:\n[{?}]", e.getMessage());
            }
        }
    }
}
