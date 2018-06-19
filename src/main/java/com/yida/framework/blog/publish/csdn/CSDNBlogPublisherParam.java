package com.yida.framework.blog.publish.csdn;

import com.yida.framework.blog.publish.BlogPublishParam;

/**
 * @Author Lanxiaowei
 * @Date 2018-06-19 14:41
 * @Description 发布博客至CSDN所需的参数定义
 */
public class CSDNBlogPublisherParam extends BlogPublishParam {
    private String loginUrl = "https://passport.csdn.net/account/login";
    private String postBlogUrl = "https://mp.csdn.net/mdeditor/saveArticle";

    public String getCSDNUserName() {
        return this.config.getCsdnUserName();
    }

    public String getCSDNPassword() {
        return this.config.getCsdnPassword();
    }

    public String getLoginUrl() {
        return loginUrl;
    }

    public String getPostBlogUrl() {
        return postBlogUrl;
    }

    public String getHexoHome() {
        return this.config.getHexoBasePath();
    }
}
