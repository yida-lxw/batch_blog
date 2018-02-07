package com.yida.framework.blog.handler.input;

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

    private WordFilterHandlerInput wordFilterHandlerInput;

    public Word2MarkdownHandlerInput() {
        this.wordFilterHandlerInput = new WordFilterHandlerInput();
    }

    public String getPandocHome() {
        return this.config.getPandocHome();
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


