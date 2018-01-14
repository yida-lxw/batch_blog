package com.yida.framework.blog.utils.io;

import java.io.*;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

/**
 * @Author Lanxiaowei
 * @Date 2018-01-14 21:43
 * @Description ZIP文件的压缩与解压缩
 */
public class ZipArchiverFileFilter extends DefaultArchiverFileFilter {
    @Override
    public void doUnArchiver(File srcfile, String destpath) throws IOException {
        byte[] buf = new byte[1024];
        FileInputStream fis = new FileInputStream(srcfile);
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

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            fis.close();
            bis.close();
            zis.close();
        }
    }
}
