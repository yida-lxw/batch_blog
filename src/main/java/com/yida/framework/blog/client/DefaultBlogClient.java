package com.yida.framework.blog.client;

import com.yida.framework.blog.utils.Constant;
import com.yida.framework.blog.utils.common.StringUtil;
import com.yida.framework.blog.utils.github.GithubUtil;
import com.yida.framework.blog.utils.io.FileUtil;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.eclipse.jgit.lib.Ref;

import java.io.File;
import java.io.FilenameFilter;
import java.util.List;

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
        if (null != this.blogPublisherKeyList && this.blogPublisherKeyList.size() > 0) {
            if (null != this.blogPublisherClassNames && this.blogPublisherClassNames.size() > 0) {
                for (String blogPublisherKey : blogPublisherKeyList) {
                    String fileName = null;
                    String blogPublisherClassPath = null;
                    for (String blogPublisherClassName : blogPublisherClassNames) {
                        blogPublisherClassPath = blogPublisherClassName.replaceAll("/", ".");
                        fileName = FileUtil.getFileName(blogPublisherClassName);

                    }
                }
            }
        }
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
        if (null != this.blogBasePaths && this.blogBasePaths.size() > 0) {
            for (String blogBasePath : this.blogBasePaths) {
                List<String> files = FileUtil.listFiles(blogBasePath, new FilenameFilter() {
                    @Override
                    public boolean accept(File dir, String name) {
                        String orignalName = name;
                        name = name.toLowerCase();
                        if (name.endsWith(".md") || name.endsWith(".markdown")
                                || name.endsWith(".docx")) {
                            if (name.startsWith(Constant.IGNORE_MARK)) {
                                return false;
                            }
                            return new File(dir + "/" + orignalName).isFile();
                        }
                        return false;
                    }
                }, true);
                if (null != files && files.size() > 0) {
                    String pureFileName = null;
                    String fileDir = null;
                    for (String file : files) {
                        pureFileName = FileUtil.getFileName(file);
                        //已经有[ignore]标记的文件直接跳过不处理
                        if (pureFileName.startsWith(Constant.IGNORE_MARK)) {
                            continue;
                        }
                        fileDir = StringUtil.fixedPathDelimiter(FileUtil.getFileDir(file));
                        FileUtil.renameFile(fileDir, pureFileName, Constant.IGNORE_MARK + pureFileName);
                    }
                }
            }
        }
        //关闭Git
        GithubUtil.closeGit(this.git);
    }
}
