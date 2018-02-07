package com.yida.framework.blog.handler.input;

import com.yida.framework.blog.config.DefaultConfigurable;

import java.util.List;

/**
 * @Author Lanxiaowei
 * @Date 2018-01-15 22:35
 * @Description 任务处理器输入抽象
 */
public abstract class HandlerInput extends DefaultConfigurable {
    /**
     * Word文档内部存储Image图片的相对路径
     */
    public String WORD_IMAGE_PATH = "word/media/";

    public String getPandocHome() {
        return this.config.getPandocHome();
    }

    public String getWordBasePath() {
        return this.config.getWordBasePath();
    }

    public String getBlogSendDate() {
        return this.config.getBlogSendDate();
    }

    public List<String> getBlogSendDates() {
        return this.config.getBlogSendDates();
    }
}
