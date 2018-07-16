package com.yida.framework.blog.handler;

import com.yida.framework.blog.handler.input.MarkdownFixHandlerInput;
import com.yida.framework.blog.handler.output.MarkdownFixHandlerOutput;
import com.yida.framework.blog.utils.Constant;
import com.yida.framework.blog.utils.common.StringUtil;
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
        markdownParentPath = new ArrayList<String>();
        if (StringUtil.isNotEmpty(wordBasePath)) {
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
        }

        String markdownBasePath = input.getMarkdownBasePath();
        if ((null == markdownParentPath || markdownParentPath.size() <= 0) &&
                StringUtil.isNotEmpty(markdownBasePath)) {
            if (null == markdownParentPath) {
                markdownParentPath = new ArrayList<>(10);
            }
            String markdownsubPath = null;
            for (String blogSendDate : blogSendDates) {
                if (markdownBasePath.endsWith("\\\\") || markdownBasePath.endsWith("\\")) {
                    markdownsubPath = markdownBasePath + blogSendDate + "\\";
                } else if (markdownBasePath.endsWith("/")) {
                    markdownsubPath = markdownBasePath + blogSendDate + "/";
                } else {
                    markdownsubPath = markdownBasePath + "/" + blogSendDate + "/";
                }
                markdownParentPath.add(markdownsubPath);
            }
        }

        if (null == markdownParentPath || markdownParentPath.size() <= 0) {
            return;
        }

        String[] markdowns = null;
        List<String> lines = null;
        String tempDirDemo = String.format("![%s", Constant.SYSTEM_TEMP_DIR);

        String githubRemoteRepoPath = input.getGithubRemoteRepoPath();
        String vnoteBlogpath = input.getVnoteBlogpath();
        boolean fix2HttpfulUrl = StringUtil.isNotEmpty(vnoteBlogpath);
        githubRemoteRepoPath = githubRemoteRepoPath.replace(".git", "");
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
            String impageHttpPrefix = githubRemoteRepoPath + "blob/" +
                    input.getGithubBlogBranchName() + "/" +
                    parentPath.replace(markdownBasePath, "") + "images";
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
                    boolean hasImgTitle = false;
                    String imgTitle = null;
                    for (String line : lines) {
                        hasImgTitle = false;
                        imgTitle = null;
                        if (null == line) {
                            continue;
                        }
                        line = line.trim();
                        if (line.startsWith(tempDirDemo)) {
                            //如果makedown里引用了系统临时文件夹里的图片文件
                            String picPath = line.substring(line.indexOf("[") + 1, line.indexOf("]"));
                            line = line.replace(picPath, "");
                            nextSkip = true;
                        } else {
                            imgTitle = line.replaceAll("!\\[(.*)\\]\\(images.*", "$1");
                            if (StringUtil.isNotEmpty(imgTitle)) {
                                hasImgTitle = true;
                                line = line.replace(imgTitle, "");
                            }
                        }
                        if (line.contains("![](media")) {
                            line = line.replaceAll("\\s+", "");
                            if (line.contains("!")) {
                                line = line.substring(line.indexOf("!"));
                            }
                        }
                        if (line.startsWith("![](media")) {
                            if (-1 != line.indexOf("{")) {
                                line = line.substring(0, line.indexOf("{"));
                            }

                            //line = line.replace("(media/", "(images/");
                            line = line.replace("(media/", "(" + impageHttpPrefix + "/");
                            builder.append(line).append("\n");
                            nextSkip = true;
                            continue;
                        } else if (fix2HttpfulUrl && line.contains("(images/")) {
                            line = line.replaceAll("(.*\\!\\[[^\\]]*\\]\\()(images)(\\/[\\_0-9a-zA-Z]+\\.[png|jpg|jpeg|gif]+\\))", "$1" + impageHttpPrefix + "$3");
                            if (line.contains(".jpg")) {
                                line = line.replace(".jpg)", ".jpg?raw=true)");
                            } else if (line.contains(".jpeg")) {
                                line = line.replace(".jpeg)", ".jpeg?raw=true)");
                            } else if (line.contains(".png")) {
                                line = line.replace(".png)", ".png?raw=true)");
                            } else if (line.contains(".gif")) {
                                line = line.replace(".gif)", ".gif?raw=true)");
                            }
                            builder.append(line).append("\n");
                            nextSkip = true;
                            continue;
                        }
                        if (hasImgTitle && StringUtil.isNotEmpty(imgTitle)) {
                            line = line.replace("![]", "![" + imgTitle + "]");
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
