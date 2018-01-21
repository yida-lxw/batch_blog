package com.yida.framework.blog.handler.output;

import java.util.List;

/**
 * @Author Lanxiaowei
 * @Date 2018-01-21 18:38
 * @Description Markdown文件内容修复任务处理器的输出
 */
public class MarkdownFixHandlerOutput extends HandlerOutput {
    /**
     * 经过处理的Markdown文件列表
     */
    private List<String> markdownFilePaths;

    public List<String> getMarkdownFilePaths() {
        return markdownFilePaths;
    }

    public void setMarkdownFilePaths(List<String> markdownFilePaths) {
        this.markdownFilePaths = markdownFilePaths;
    }
}
