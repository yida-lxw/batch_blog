package com.yida.framework.blog.handler.input;

import java.util.List;
import java.util.Map;

/**
 * @Author Lanxiaowei
 * @Date 2018-01-21 17:45
 * @Description Markdown文件内容修复任务处理器的输入
 * 主要操作是修复Markdown内的一些图片的引用路径
 */
public class MarkdownFixHandlerInput extends HandlerInput {
    /**
     * Markdown文件所在根目录
     */
    private List<String> markdownParentPath;

    /**
     * 每个Images目录下包含的图片文件路径
     */
    private Map<String, List<String>> imagesFilePath;

    public List<String> getMarkdownParentPath() {
        return markdownParentPath;
    }

    public void setMarkdownParentPath(List<String> markdownParentPath) {
        this.markdownParentPath = markdownParentPath;
    }

    public Map<String, List<String>> getImagesFilePath() {
        return imagesFilePath;
    }

    public void setImagesFilePath(Map<String, List<String>> imagesFilePath) {
        this.imagesFilePath = imagesFilePath;
    }
}
