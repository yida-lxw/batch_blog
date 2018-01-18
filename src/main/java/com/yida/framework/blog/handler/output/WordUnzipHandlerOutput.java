package com.yida.framework.blog.handler.output;

import java.util.List;

/**
 * @Author Lanxiaowei
 * @Date 2018-01-18 00:31
 * @Description Word文档解压任务处理器的输出
 */
public class WordUnzipHandlerOutput extends HandlerOutput {
    /**
     * Word文档解压后得到的目录列表
     */
    private List<String> unzipFilePaths;

    /**
     * word文档解压是否成功
     */
    private boolean successful;

    public List<String> getUnzipFilePaths() {
        return unzipFilePaths;
    }

    public void setUnzipFilePaths(List<String> unzipFilePaths) {
        this.unzipFilePaths = unzipFilePaths;
    }

    public boolean isSuccessful() {
        return successful;
    }

    public void setSuccessful(boolean successful) {
        this.successful = successful;
    }
}
