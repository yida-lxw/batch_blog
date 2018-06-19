package com.yida.framework.blog.handler.output;

import java.util.ArrayList;
import java.util.List;

/**
 * @Author Lanxiaowei
 * @Date 2018-02-07 23:14
 * @Description HexoHandler任务处理器的输出
 */
public class HexoHandlerOutput extends HandlerOutput {
    /**
     * 迁移后Markdown文件的新存放路径
     */
    private List<String> markdownFilePath;

    public List<String> getMarkdownFilePath() {
        if (null == markdownFilePath) {
            markdownFilePath = new ArrayList<String>();
        }
        return markdownFilePath;
    }

    public void setMarkdownFilePath(List<String> markdownFilePath) {
        this.markdownFilePath = markdownFilePath;
    }
}
