package com.yida.framework.blog.client;

import com.yida.framework.blog.publish.BlogPublisher;

import java.util.List;

/**
 * @Author Lanxiaowei
 * @Date 2018-02-12 12:12
 * @Description 博客发布工具的客户端接口抽象实现类
 */
public abstract class AbstractBlogClient {
    /**
     * 博客待发布的博客平台列表,比如:Github,ITeye,CSDN,OSChina,CNBlog,简书等等
     */
    protected List<BlogPublisher> blogPublisherList;

    /**
     * 需要发布的Markdown文件的存放路径
     */
    protected List<String> markdownFilePaths;

    /**
     * 需要发布的Markdown文件对应的内容
     */
    protected List<String> markdownFileContents;

    /**
     * 博客发布工具的启动入口
     */
    public void go() {
        beforeBlogSend();
        prepareBlogPlatform();
        findTargetMarkdowns();
        blogSend();
        afterBlogSend();
    }

    /**
     * 在博客发布之前做的一些准备工作,比如将Word/Markdown文件以及相关图片文件复制到
     * 本地的Github仓库中
     */
    protected abstract void beforeBlogSend();

    /**
     * 读取配置文件,确定需要发布到哪些博客平台
     */
    protected abstract void prepareBlogPlatform();

    /**
     * 查找所有需要发布为博客的Markdown文件
     *
     * @return
     */
    protected abstract void findTargetMarkdowns();

    /**
     * 发布博客至各大博客平台
     *
     * @return
     */
    protected abstract void blogSend();

    /**
     * 博客发布完成之后需要做的一些收尾工作,比如将已经发布过的MarkDown文件标记为已发送,
     * 避免下次发布时重复发布
     */
    protected abstract void afterBlogSend();
}
