package com.yida.framework.blog.handler;

import com.yida.framework.blog.handler.input.HexoHandlerInput;
import com.yida.framework.blog.handler.output.HexoHandlerOutput;
import com.yida.framework.blog.utils.common.StringUtil;
import com.yida.framework.blog.utils.io.FileUtil;
import com.yida.framework.blog.utils.io.MarkdownFilenameFilter;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.File;

/**
 * @Author Lanxiaowei
 * @Date 2018-02-07 23:11
 * @Description 将修正后的md文件复制到G:\hexo\source\_posts目录下
 */
public class HexoHandler implements Handler<HexoHandlerInput, HexoHandlerOutput> {
    private Logger log = LogManager.getLogger(HexoHandler.class.getName());
    private MarkdownFilenameFilter markdownFilenameFilter;

    public HexoHandler(MarkdownFilenameFilter markdownFilenameFilter) {
        this.markdownFilenameFilter = markdownFilenameFilter;
    }

    @Override
    public void handle(HexoHandlerInput input, HexoHandlerOutput output) {
        String hexoBasePath = input.getHexoBasePath();
        if (StringUtil.isEmpty(hexoBasePath)) {
            return;
        }
        String markdownBasePath = input.getMarkdownBasePath();
        if (StringUtil.isEmpty(markdownBasePath)) {
            return;
        }
        String blogSendDate = input.getBlogSendDate();
        if (StringUtil.isEmpty(blogSendDate)) {
            return;
        }
        String orignalPath = markdownBasePath + blogSendDate;
        if (!orignalPath.endsWith("/")) {
            if (!orignalPath.endsWith("\\")) {
                orignalPath = orignalPath + "/";
            }
        }
        File file = new File(orignalPath);
        String[] markdowns = file.list(this.markdownFilenameFilter);
        if (null == markdowns || markdowns.length <= 0) {
            return;
        }
        try {
            String targetPath = hexoBasePath + "source/_posts/";
            for (String mdFileName : markdowns) {
                FileUtil.copyFile(orignalPath, mdFileName, targetPath, false, true);
            }
        } catch (Exception e) {
            log.error("Moving the file from [{}] to [{}] occured IOException:\n{}", orignalPath, hexoBasePath, e.getMessage());
        }
    }
}
