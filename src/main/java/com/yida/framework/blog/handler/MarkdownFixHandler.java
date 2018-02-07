package com.yida.framework.blog.handler;

import com.yida.framework.blog.handler.input.MarkdownFixHandlerInput;
import com.yida.framework.blog.handler.output.MarkdownFixHandlerOutput;
import com.yida.framework.blog.utils.Constant;
import com.yida.framework.blog.utils.io.MarkdownFilenameFilter;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.Map;

/**
 * @Author Lanxiaowei
 * @Date 2018-01-21 18:56
 * @Description Markdown文件内容修复任务处理器
 */
public class MarkdownFixHandler implements Handler<MarkdownFixHandlerInput, MarkdownFixHandlerOutput> {
    private Logger log = LogManager.getLogger(MarkdownFixHandler.class.getName());
    private MarkdownFilenameFilter markdownFilenameFilter;

    public MarkdownFixHandler() {
        this.markdownFilenameFilter = new MarkdownFilenameFilter();
    }

    public static void main(String[] args) {
        System.out.println(String.format("![%s", Constant.SYSTEM_TEMP_DIR));
        File file = new File(String.format("![%s", Constant.SYSTEM_TEMP_DIR) + "1516427572.bmp");
        System.out.println(file.exists());
    }

    @Override
    public void handle(MarkdownFixHandlerInput input, MarkdownFixHandlerOutput output) {
        List<String> markdownParentPath = input.getMarkdownParentPath();
        if (null == markdownParentPath || markdownParentPath.size() <= 0) {
            return;
        }
        Map<String, List<String>> imagesFilePathMap = input.getImagesFilePath();
        if (null == imagesFilePathMap || imagesFilePathMap.size() <= 0) {
            return;
        }
        File file = null;
        //File markdownFile = null;
        String[] markdowns = null;
        String imagePathPermarkdown = null;
        List<String> imagesPath = null;
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
            imagePathPermarkdown = parentPath + output.MD_IMAGE_BASEPATH;
            imagesPath = imagesFilePathMap.get(imagePathPermarkdown);
            String markdownPath = null;
            StringBuilder builder = new StringBuilder();
            try {
                int index = 0;
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
                        if (null == line || "".equals(line)) {
                            continue;
                        }
                        if (line.startsWith("![C:\\\\Users\\\\ADMINI")) {
                            //如果makedown里引用了系统临时文件夹里的图片文件
                            if (line.contains("C:\\\\Users\\\\ADMINI\\~1\\\\AppData\\\\Local\\\\Temp\\\\")) {
                                String picPath = line.substring(line.indexOf("[") + 1, line.indexOf("]"));
                                line = line.replace(picPath, "");
                            }
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
