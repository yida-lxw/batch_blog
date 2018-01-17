package com.yida.framework.blog.handler;

import java.util.ArrayList;
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

    /**
     * 文件过滤任务是否处理成功
     */
    private boolean successful;

    public List<String> getWordFilesPath() {
        if (null == wordFilesPath) {
            wordFilesPath = new ArrayList<String>(20);
        }
        return wordFilesPath;
    }

    public void setWordFilesPath(List<String> wordFilesPath) {
        this.wordFilesPath = wordFilesPath;
    }

    public boolean isSuccessful() {
        return successful;
    }

    public void setSuccessful(boolean successful) {
        this.successful = successful;
    }
}
