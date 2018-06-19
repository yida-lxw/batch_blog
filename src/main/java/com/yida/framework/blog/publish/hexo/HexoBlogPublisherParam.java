package com.yida.framework.blog.publish.hexo;

import com.yida.framework.blog.publish.BlogPublishParam;

/**
 * @Author Lanxiaowei
 * @Date 2018-06-19 14:41
 * @Description 发布博客至Hexo所需的参数定义
 */
public class HexoBlogPublisherParam extends BlogPublishParam {
    public String getHexoHome() {
        return this.config.getHexoBasePath();
    }
}
