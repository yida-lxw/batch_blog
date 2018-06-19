package com.yida.framework.blog.publish;

import com.yida.framework.blog.config.DefaultConfigurable;

import java.util.ArrayList;
import java.util.List;

/**
 * @Author Lanxiaowei
 * @Date 2018-02-12 10:52
 * @Description 博客发布所需的公共参数
 */
public abstract class BlogPublishParam extends DefaultConfigurable {
    /**
     * 本地Word文档存放根目录
     */
    private String wordBasePath;

    /**
     * 博客发布日期,多个日期采用分号分割
     */
    private String blogSendDate;

    /**
     * 博客发布日期,多个日期采用分号分割
     */
    private List<String> blogSendDates;

    /**
     * 博客内容
     */
    private String blogContent;

    /**
     * 博客分类
     */
    private String category;

    /**
     * 博客平台的登录账号
     */
    private String userName;

    /**
     * 博客平台的登录密码
     */
    private String password;

    /**
     * 需要发布的Markdown文件的存放路径
     */
    protected List<String> markdownFilePaths;

    public String getWordBasePath() {
        this.wordBasePath = this.config.getWordBasePath();
        return this.wordBasePath;
    }

    public void setWordBasePath(String wordBasePath) {
        this.wordBasePath = wordBasePath;
    }

    public String getBlogSendDate() {
        this.blogSendDate = this.config.getBlogSendDate();
        return this.blogSendDate;
    }

    public void setBlogSendDate(String blogSendDate) {
        this.blogSendDate = blogSendDate;
    }

    public List<String> getBlogSendDates() {
        this.blogSendDates = this.config.getBlogSendDates();
        if (null == this.blogSendDates || this.blogSendDates.size() <= 0) {
            String sendDate = getBlogSendDate();
            if (null == sendDate || "".equals(sendDate)) {
                this.blogSendDates = new ArrayList<>(1);
                blogSendDates.set(0, sendDate);
            }
        }
        return this.blogSendDates;
    }

    public void setBlogSendDates(List<String> blogSendDates) {
        this.blogSendDates = blogSendDates;
    }

    public String getBlogContent() {
        return blogContent;
    }

    public void setBlogContent(String blogContent) {
        this.blogContent = blogContent;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public List<String> getMarkdownFilePaths() {
        return markdownFilePaths;
    }

    public void setMarkdownFilePaths(List<String> markdownFilePaths) {
        this.markdownFilePaths = markdownFilePaths;
    }
}
