package com.yida.framework.blog.handler.input;

import java.util.List;

/**
 * @Author Lanxiaowei
 * @Date 2018-01-18 00:21
 * @Description Word文档解压任务处理器的输入
 * 注意：只有Word2007及其以上版本的Word文档(即.docx文档)才能进行解压,
 * 而Word2003及其以下版本的Word文档(即.doc文档)不支持解压操作
 */
public class WordUnzipHandlerInput extends HandlerInput {
    /**
     * Word文件列表
     */
    protected List<String> wordFilesName;

    private WordFilterHandlerInput wordFilterHandlerInput;

    public WordUnzipHandlerInput() {
        this.wordFilterHandlerInput = new WordFilterHandlerInput();
    }

    public List<String> getWordFilesName() {
        return wordFilesName;
    }

    public void setWordFilesName(List<String> wordFilesName) {
        this.wordFilesName = wordFilesName;
    }

    public WordFilterHandlerInput getWordFilterHandlerInput() {
        return wordFilterHandlerInput;
    }

    public void setWordFilterHandlerInput(WordFilterHandlerInput wordFilterHandlerInput) {
        this.wordFilterHandlerInput = wordFilterHandlerInput;
    }
}
