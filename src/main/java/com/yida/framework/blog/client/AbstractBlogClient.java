package com.yida.framework.blog.client;

import com.yida.framework.blog.config.DefaultConfigurable;
import com.yida.framework.blog.publish.BlogPublisher;
import com.yida.framework.blog.utils.io.FileUtil;
import org.eclipse.jgit.api.Git;

import java.io.File;
import java.io.FilenameFilter;
import java.util.ArrayList;
import java.util.List;

/**
 * @Author Lanxiaowei
 * @Date 2018-02-12 12:12
 * @Description 博客发布工具的客户端接口抽象实现类
 */
public abstract class AbstractBlogClient extends DefaultConfigurable implements BlogClient {
    private static final String BASE_PACKAGE = "com.yida.framework.blog.publish";
    protected Git git;

    /**
     * 配置文件中配置的当前支持的博客平台的key列表
     */
    protected List<String> blogPublisherKeyList;

    /**
     * 博客待发布的博客平台对应的实现类列表,比如:Github,ITeye,CSDN,OSChina,CNBlog,简书等等
     */
    protected List<BlogPublisher> blogPublisherList;

    /**
     * 所有已实现的博客平台发布实现类的名称列表
     */
    protected List<String> blogPublisherClassNames;

    /**
     * 博客文档按日期存放的根目录,比如C:/myblog/20180212,C:/myblog/20180213,C:/myblog/20180214
     */
    protected List<String> blogBasePaths;

    /**
     * 博客文档在Github本地仓库按日期存放的根目录,比如C:/git-local/blog/20180212,C:/git-local/blog/20180213,C:/git-local/blog/20180214
     */
    protected List<String> blogLocalRepoBasePaths;

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
    @Override
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

    /**
     * 构建所需的参数信息
     */
    protected void buildParams() {
        List<String> blogSendDates = this.config.getBlogSendDates();
        if (null == this.blogBasePaths) {
            this.blogBasePaths = new ArrayList<>();
        }
        if (null == this.blogLocalRepoBasePaths) {
            this.blogLocalRepoBasePaths = new ArrayList<>();
        }
        String wordBasePath = this.config.getWordBasePath();
        String blogLocalRepoBasePath = this.config.getGithubLocalCodeDir();
        for (String blogSendDate : blogSendDates) {
            this.blogBasePaths.add(wordBasePath + blogSendDate + "/");
            this.blogLocalRepoBasePaths.add(blogLocalRepoBasePath + blogSendDate + "/");
        }
        //Scan all BlogPublisher implements
        scanBlogPublisherClasses();
    }

    /**
     * 扫描出所有以实现的BlogPublisher实现类
     *
     * @return
     */
    private void scanBlogPublisherClasses() {
        String classpath = AbstractBlogClient.class.getResource("/").getPath();
        classpath = classpath.replaceFirst("/", "");
        String basePackage = BASE_PACKAGE;
        basePackage = basePackage.replace(".", "/");
        //combine the classpath and basePackage
        String searchPath = classpath + basePackage;
        List<String> javaFiles = FileUtil.listFiles(searchPath, new FilenameFilter() {
            @Override
            public boolean accept(File dir, String name) {
                if ("BlogPublisher.class".equals(name)) {
                    return false;
                }
                return name.endsWith("BlogPublisher.class");
            }
        }, true);
        if (null == javaFiles || javaFiles.size() <= 0) {
            return;
        }
        this.blogPublisherClassNames = new ArrayList<>(javaFiles.size());
        for (String javaFile : javaFiles) {
            this.blogPublisherClassNames.add(javaFile);
        }
    }

    public Git getGit() {
        return git;
    }
}
