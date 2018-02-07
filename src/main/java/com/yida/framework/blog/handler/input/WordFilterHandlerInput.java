package com.yida.framework.blog.handler.input;

import java.util.List;

/**
 * @Author Lanxiaowei
 * @Date 2018-01-17 21:37
 * @Description Word文档过滤任务处理的输入
 */
public class WordFilterHandlerInput extends HandlerInput {
    /**
     * Word文档的存储根目录
     */
    private String wordBasePath;

    /**
     * 博客的发布日期
     */
    private String blogSendDate;

    /**
     * 博客的发布日期(多个日期采用分号分割)
     */
    private List<String> blogSendDates;

    public WordFilterHandlerInput() {
        this.wordBasePath = this.config.getWordBasePath();
        this.blogSendDate = this.config.getBlogSendDate();
        this.blogSendDates = this.config.getBlogSendDates();
    }

    public String getWordBasePath() {
        return wordBasePath;
    }

    public void setWordBasePath(String wordBasePath) {
        this.wordBasePath = wordBasePath;
    }

    public String getBlogSendDate() {
        return blogSendDate;
    }

    public void setBlogSendDate(String blogSendDate) {
        this.blogSendDate = blogSendDate;
    }

    public List<String> getBlogSendDates() {
        return blogSendDates;
    }

    public void setBlogSendDates(List<String> blogSendDates) {
        this.blogSendDates = blogSendDates;
    }
}
