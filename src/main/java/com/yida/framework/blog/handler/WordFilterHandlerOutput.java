package com.yida.framework.blog.handler;

import java.util.List;

/**
 * @Author Lanxiaowei
 * @Date 2018-01-17 21:41
 * @Description Word文档过滤任务处理的输入
 */
public class WordFilterHandlerOutput extends HandlerOutput {
    /**
     * 过滤出来的Word文档的路径列表
     */
    private List<String> wordFilesPath;

    public List<String> getWordFilesPath() {
        return wordFilesPath;
    }

    public void setWordFilesPath(List<String> wordFilesPath) {
        this.wordFilesPath = wordFilesPath;
    }
}
