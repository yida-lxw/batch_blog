package com.yida.framework.blog.handler.output;

import java.util.List;
import java.util.Map;

/**
 * @Author Lanxiaowei
 * @Date 2018-01-18 23:42
 * @Description 复制Word内自包含的图片文件复制到指定目录的任务处理器的输出
 */
public class WordImageCopyHandlerOutput extends HandlerOutput {
    /**
     * 从Word文档内复制出来的图片文件列表,Key为Word文档解压目录下的images目录路径，Value为该目录下的所有图片路径
     */
    private Map<String, List<String>> imagesFilePath;

    /**
     * 所有Markdown文件列表,下一步需要遍历这些Markdown文件并对其中的图片路径进行调整
     */
    private List<String> markdownFilesPath;

    public Map<String, List<String>> getImagesFilePath() {
        return imagesFilePath;
    }

    public void setImagesFilePath(Map<String, List<String>> imagesFilePath) {
        this.imagesFilePath = imagesFilePath;
    }
}
