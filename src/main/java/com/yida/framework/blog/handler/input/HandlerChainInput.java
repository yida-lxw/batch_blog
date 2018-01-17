package com.yida.framework.blog.handler.input;

/**
 * @Author Lanxiaowei
 * @Date 2018-01-15 22:45
 * @Description Handler处理链输入
 */
public class HandlerChainInput extends HandlerInput {
    /**
     * Word文件存储根目录
     */
    protected String wordFileBasePath;

    /**
     * Markdown文件存储根目录,设置了此目录，即表示你的博客你采用的是Markdown编写的，此时不需要进行docx至markdown的文件转换处理
     */
    protected String markdownBasePath;

    public String getWordFileBasePath() {
        return wordFileBasePath;
    }

    protected void setWordFileBasePath(String wordFileBasePath) {
        this.wordFileBasePath = wordFileBasePath;
    }

    public String getMarkdownBasePath() {
        return markdownBasePath;
    }

    protected void setMarkdownBasePath(String markdownBasePath) {
        this.markdownBasePath = markdownBasePath;
    }
}


