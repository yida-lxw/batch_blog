package com.yida.framework.blog.handler;

import com.yida.framework.blog.handler.input.MarkdownMoveHandlerInput;
import com.yida.framework.blog.handler.output.MarkdownMoveHandlerOutput;
import com.yida.framework.blog.utils.common.StringUtil;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.File;
import java.io.FilenameFilter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.List;

/**
 * @Author Lanxiaowei
 * @Date 2018-02-07 23:11
 * @Description 迁移Markdown文件的存放目录，以便将word与markdown分开
 * 为什么要迁移?没有为什么，我想要分开存放仅此而已
 */
public class MarkdownMoveHandler implements Handler<MarkdownMoveHandlerInput, MarkdownMoveHandlerOutput> {
    private Logger log = LogManager.getLogger(MarkdownMoveHandler.class.getName());

    @Override
    public void handle(MarkdownMoveHandlerInput input, MarkdownMoveHandlerOutput output) {
        String wordBasePath = input.getWordBasePath();
        if (StringUtil.isEmpty(wordBasePath)) {
            return;
        }
        if (StringUtil.isNotEmpty(wordBasePath)) {
            if (!wordBasePath.endsWith("/") && !wordBasePath.endsWith("\\")) {
                wordBasePath += "/";
            }
        }
        List<String> blogSendDates = input.getBlogSendDates();
        if (null == blogSendDates || blogSendDates.size() <= 0) {
            String blogSendDate = input.getBlogSendDate();
            if (null == blogSendDate || "".equals(blogSendDate)) {
                return;
            }
            blogSendDates = new ArrayList<String>();
            blogSendDates.add(blogSendDate);
        }

        if (null == blogSendDates || blogSendDates.size() <= 0) {
            return;
        }
        File file = null;
        String wordPath = null;
        for (String blogSendDate : blogSendDates) {
            wordPath = wordBasePath + blogSendDate;
            file = new File(wordPath);
            String[] directories = file.list(new FilenameFilter() {
                @Override
                public boolean accept(File dir, String name) {
                    File tempFile = new File(dir.getAbsolutePath() + "/" + name);
                    return tempFile.isDirectory();
                }
            });
            if (null == directories || directories.length <= 0) {
                continue;
            }
            try {
                String targetPath = null;
                for (String directory : directories) {
                    targetPath = wordPath + "/" + directory + "/" + directory + ".md";
                    Files.move(Paths.get(wordPath + "/" + directory + ".md"),
                            Paths.get(targetPath), StandardCopyOption.REPLACE_EXISTING);
                    output.getMarkdownFilePath().add(targetPath);
                }
            } catch (IOException e) {
                e.printStackTrace();
                log.error("Moving the file from [{}] occured IOException:\n{}", wordPath, e.getMessage());
            }
        }
    }
}
