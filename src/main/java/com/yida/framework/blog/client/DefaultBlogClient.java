package com.yida.framework.blog.client;

/**
 * @Author Lanxiaowei
 * @Date 2018-02-13 10:26
 * @Description 博客发布工具的客户端接口的默认实现类
 */
public class DefaultBlogClient extends AbstractBlogClient {
    /**
     * 在博客发布之前做的一些准备工作,比如将Word/Markdown文件以及相关图片文件复制到
     * 本地的Github仓库中
     */
    @Override
    protected void beforeBlogSend() {
        //将本地的博客文档复制到Github的本地仓库中等待上传
        //FileUtil.copyDirectory(srcpath, targetpath, null, false);
    }

    @Override
    protected void prepareBlogPlatform() {

    }

    @Override
    protected void findTargetMarkdowns() {

    }

    @Override
    protected void blogSend() {

    }

    @Override
    protected void afterBlogSend() {

    }
}
