package com.yida.framework.blog.publish;

/**
 * @Author Lanxiaowei
 * @Date 2018-02-12 10:52
 * @Description 博客发布所需的公共参数
 */
public abstract class BlogPublishParam {
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
}
