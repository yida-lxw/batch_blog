package com.yida.framework.blog.config;

/**
 * @Author Lanxiaowei
 * @Date 2018-01-16 00:35
 * @Description Blog配置实体类
 */
public class BlogConfig {
    /**
     * Pandoc的安装目录
     */
    private String pandocHome;
    /**
     * Word文档的存储根目录
     */
    private String wordBasePath;
    /**
     * 博客发送日期
     */
    private String blogSendDate;
    /**
     * Github的登录账号
     */
    private String githubUserName;
    /**
     * Github的登录密码
     */
    private String githubPassword;

    public String getPandocHome() {
        return pandocHome;
    }

    public void setPandocHome(String pandocHome) {
        this.pandocHome = pandocHome;
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

    public String getGithubUserName() {
        return githubUserName;
    }

    public void setGithubUserName(String githubUserName) {
        this.githubUserName = githubUserName;
    }

    public String getGithubPassword() {
        return githubPassword;
    }

    public void setGithubPassword(String githubPassword) {
        this.githubPassword = githubPassword;
    }
}
