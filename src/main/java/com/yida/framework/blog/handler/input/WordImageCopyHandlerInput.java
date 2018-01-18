package com.yida.framework.blog.handler.input;

import java.util.List;

/**
 * @Author Lanxiaowei
 * @Date 2018-01-18 23:37
 * @Description 复制Word内自包含的图片文件复制到指定目录的任务处理器的输入
 */
public class WordImageCopyHandlerInput extends HandlerInput {
    /**
     * Word文档解压后得到的目录列表
     */
    private List<String> unzipFilePaths;

    public List<String> getUnzipFilePaths() {
        return unzipFilePaths;
    }

    public void setUnzipFilePaths(List<String> unzipFilePaths) {
        this.unzipFilePaths = unzipFilePaths;
    }
}
