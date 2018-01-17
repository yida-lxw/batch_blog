package com.yida.framework.blog.handler;

import java.util.List;

/**
 * @Author Lanxiaowei
 * @Date 2018-01-15 22:45
 * @Description Docx文件转Markdown处理器对应的输入类
 */
public class Word2MarkdownHandlerInput extends HandlerInput {
    /**
     * Word文件列表
     */
    protected List<String> wordFilesName;

    public List<String> getWordFilesName() {
        return wordFilesName;
    }

    public void setWordFilesName(List<String> wordFilesName) {
        this.wordFilesName = wordFilesName;
    }
}


