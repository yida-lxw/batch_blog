package com.yida.framework.blog.handler;

import com.yida.framework.blog.handler.input.MarkdownFixHandlerInput;
import com.yida.framework.blog.handler.output.MarkdownFixHandlerOutput;
import com.yida.framework.blog.utils.Constant;
import com.yida.framework.blog.utils.io.MarkdownFilenameFilter;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.File;
import java.io.FilenameFilter;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

/**
 * @Author Lanxiaowei
 * @Date 2018-01-21 18:56
 * @Description Markdown文件内容修复任务处理器
 */
public class MarkdownFixHandler implements Handler<MarkdownFixHandlerInput, MarkdownFixHandlerOutput> {
    private Logger log = LogManager.getLogger(MarkdownFixHandler.class.getName());
    private MarkdownFilenameFilter markdownFilenameFilter;

    public MarkdownFixHandler(MarkdownFilenameFilter markdownFilenameFilter) {
        this.markdownFilenameFilter = markdownFilenameFilter;
    }

    @Override
    public void handle(MarkdownFixHandlerInput input, MarkdownFixHandlerOutput output) {
        List<String> markdownParentPath = null;
        String wordBasePath = input.getWordBasePath();
        if (!wordBasePath.endsWith("/") && !wordBasePath.endsWith("\\")) {
            wordBasePath += "/";
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
        markdownParentPath = new ArrayList<String>();
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
            for (String directory : directories) {
                markdownParentPath.add(wordPath + "/" + directory);
            }
        }


        if (null == markdownParentPath || markdownParentPath.size() <= 0) {
            return;
        }

        String[] markdowns = null;
        List<String> lines = null;
        String tempDirDemo = String.format("![%s", Constant.SYSTEM_TEMP_DIR);
        for (String parentPath : markdownParentPath) {
            file = new File(parentPath);
            if (!file.exists() || !file.isDirectory()) {
                continue;
            }
            markdowns = file.list(this.markdownFilenameFilter);
            if (null == markdowns || markdowns.length <= 0) {
                continue;
            }

            if (!parentPath.endsWith("/")) {
                if (!parentPath.endsWith("\\")) {
                    parentPath += "/";
                }
            }
            String markdownPath = null;
            StringBuilder builder = new StringBuilder();
            try {
                String content = null;
                for (String markdownFileName : markdowns) {
                    markdownPath = parentPath + markdownFileName;
                    lines = Files.readAllLines(Paths.get(markdownPath),
                            Charset.forName(Constant.DEFAULT_CHARSET_UTF8));
                    if (null == lines || lines.size() <= 0) {
                        continue;
                    }
                    boolean nextSkip = false;
                    for (String line : lines) {
                        if (null == line) {
                            continue;
                        }
                        if (line.startsWith(tempDirDemo)) {
                            //如果makedown里引用了系统临时文件夹里的图片文件
                            String picPath = line.substring(line.indexOf("[") + 1, line.indexOf("]"));
                            line = line.replace(picPath, "");
                            nextSkip = true;
                        }
                        if (line.startsWith("![](media")) {
                            line = line.substring(0, line.indexOf("{"));
                            line = line.replace("(media/", "(images/");
                            builder.append(line).append("\n");
                            nextSkip = true;
                            continue;
                        }
                        if (!nextSkip) {
                            builder.append(line).append("\n");
                        } else {
                            nextSkip = false;
                        }
                    }
                    content = builder.toString().replaceAll("\n$", "");
                    Files.write(Paths.get(markdownPath), content.getBytes());
                }
            } catch (IOException e) {
                log.error("While reading the content of the markdown file[{}] occur unexcepted exception.", markdownPath);
            }
        }
    }
}
