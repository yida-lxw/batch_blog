package com.yida.framework.blog.handler;

/**
 * @Author Lanxiaowei
 * @Date 2018-01-15 22:45
 * @Description Docx文件转Makedown处理器对应的输入类
 */
public class Word2MakedownHandlerInput extends HandlerInput {
    /**
     * Word文件名称
     */
    protected String wordFileName;

    /**
     * 你需要发布哪一天的博客,日期格式为yyyyMMdd格式,比如:20180115
     */
    protected String blogDate;

    public String getWordFileName() {
        return wordFileName;
    }

    protected void setWordFileName(String wordFileName) {
        this.wordFileName = wordFileName;
    }

    public String getBlogDate() {
        return blogDate;
    }

    public void setBlogDate(String blogDate) {
        this.blogDate = blogDate;
    }
}


