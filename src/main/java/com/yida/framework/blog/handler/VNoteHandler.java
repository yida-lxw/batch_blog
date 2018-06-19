package com.yida.framework.blog.handler;

import com.yida.framework.blog.handler.input.VNoteHandlerInput;
import com.yida.framework.blog.handler.output.VNoteHandlerOutput;
import com.yida.framework.blog.utils.Constant;
import com.yida.framework.blog.utils.common.StringUtil;
import com.yida.framework.blog.utils.io.FileUtil;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.File;
import java.io.FilenameFilter;

/**
 * @Author Lanxiaowei
 * @Date 2018-02-07 23:11
 * @Description 把博客从G:\VNote_Data\GO目录复制到C:/myblog的某一个日期的目录里
 */
public class VNoteHandler implements Handler<VNoteHandlerInput, VNoteHandlerOutput> {
    private Logger log = LogManager.getLogger(VNoteHandler.class.getName());

    @Override
    public void handle(VNoteHandlerInput input, VNoteHandlerOutput output) {
        String vnoteBlogpath = input.getVnoteBlogpath();
        if (StringUtil.isEmpty(vnoteBlogpath)) {
            return;
        }
        String markdownBasePath = input.getMarkdownBasePath();
        if (StringUtil.isEmpty(markdownBasePath)) {
            return;
        }
        if (StringUtil.isNotEmpty(markdownBasePath)) {
            if (!markdownBasePath.endsWith("/") && !markdownBasePath.endsWith("\\")) {
                markdownBasePath += "/";
            }
        }
        String blogSendDate = input.getBlogSendDate();
        if (StringUtil.isEmpty(blogSendDate)) {
            return;
        }
        String targetPath = markdownBasePath + blogSendDate;
        if (!targetPath.endsWith("/")) {
            if (!targetPath.endsWith("\\")) {
                targetPath = targetPath + "/";
            }
        }

        try {
            FileUtil.copyDirectory(vnoteBlogpath, targetPath, null, false, Constant.IGNORE_MARK);
            FileUtil.deleteFile(targetPath + "_vnote.json");
            File file = new File(targetPath);
            String[] vswps = file.list(new FilenameFilter() {
                @Override
                public boolean accept(File dir, String name) {
                    File tempFile = new File(dir.getAbsolutePath() + "/" + name);
                    return tempFile.isFile() && name.endsWith(".vswp");
                }
            });
            if (null != vswps && vswps.length > 0) {
                for (String vswp : vswps) {
                    FileUtil.deleteFile(targetPath + vswp);
                }
            }
        } catch (Exception e) {
            log.error("Moving the file from [{}] to [{}] occured IOException:\n{}", vnoteBlogpath, targetPath, e.getMessage());
        }
    }
}
