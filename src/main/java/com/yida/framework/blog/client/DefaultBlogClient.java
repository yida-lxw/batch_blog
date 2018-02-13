package com.yida.framework.blog.client;

import com.yida.framework.blog.utils.Constant;
import com.yida.framework.blog.utils.github.GithubUtil;
import com.yida.framework.blog.utils.io.FileUtil;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.eclipse.jgit.lib.Ref;

/**
 * @Author Lanxiaowei
 * @Date 2018-02-13 10:26
 * @Description 博客发布工具的客户端接口的默认实现类
 */
public class DefaultBlogClient extends AbstractBlogClient {
    private Logger log = LogManager.getLogger(DefaultBlogClient.class.getName());

    /**
     * 在博客发布之前做的一些准备工作,比如将Word/Markdown文件以及相关图片文件复制到
     * 本地的Github仓库中
     */
    @Override
    protected void beforeBlogSend() {
        super.buildParams();
        //将本地的指定的日期目录下的所有博客文档复制到Github的本地仓库中等待上传
        if (null != this.blogBasePaths && this.blogBasePaths.size() > 0) {
            int index = 0;
            for (String blogBasePath : this.blogBasePaths) {
                FileUtil.copyDirectory(blogBasePath, this.blogLocalRepoBasePaths.get(index++), null, true, Constant.IGNORE_MARK);
            }
            //初始化Git实例对象
            this.git = GithubUtil.getGit(this.config.getGithubLocalRepoPath());
            Ref ref = GithubUtil.checkout(this.git, this.config.getGithubBlogBranchName());
            log.info("the return of git checkout:" + ref);
        }
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

    /**
     * 释放Git实例对象占用的文件句柄资源,并标记本地博客目录下的word和markdown文件为已发送[ignore],避免重复操作
     */
    @Override
    protected void afterBlogSend() {
        //为已经发布的文档添加[ignore]前缀标记


        //关闭Git
        GithubUtil.closeGit(this.git);
    }
}
