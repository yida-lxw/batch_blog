package com.yida.framework.blog.client;

import com.yida.framework.blog.publish.BlogPublishParam;
import com.yida.framework.blog.publish.BlogPublisher;
import com.yida.framework.blog.utils.Constant;
import com.yida.framework.blog.utils.common.ReflectionUtil;
import com.yida.framework.blog.utils.common.StringUtil;
import com.yida.framework.blog.utils.github.GithubUtil;
import com.yida.framework.blog.utils.io.FileUtil;
import com.yida.framework.blog.utils.io.ImageFilenameFilter;
import com.yida.framework.blog.utils.io.MarkdownFilenameFilter;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.eclipse.jgit.lib.Ref;
import org.eclipse.jgit.transport.PushResult;
import org.eclipse.jgit.transport.RemoteRefUpdate;

import java.io.File;
import java.io.FilenameFilter;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Iterator;
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
        super.transform();
        super.buildParams();
        String markdownBasePath = this.getConfig().getMarkdownBasePath();
        //将本地的指定的日期目录下的所有博客文档复制到Github的本地仓库中等待上传
        if (null != this.blogBasePaths && this.blogBasePaths.size() > 0) {
            int index = 0;
            for (String blogBasePath : this.blogBasePaths) {
                FileUtil.copyDirectory(blogBasePath, this.blogLocalRepoBasePaths.get(index++), null, false, Constant.IGNORE_MARK);
            }
            //初始化Git实例对象
            this.git = GithubUtil.getGit(this.config.getGithubLocalCodeDir());
            Ref ref = GithubUtil.checkout(this.git, this.config.getGithubBlogBranchName());
            log.info("the return of git checkout:" + ref);
        } else if (StringUtil.isNotEmpty(markdownBasePath)) {
            String blogBasePath = null;
            if (!markdownBasePath.endsWith("/")) {
                if (!markdownBasePath.endsWith("\\")) {
                    markdownBasePath = markdownBasePath + "/";
                }
            }
            int index = 0;
            String blogLocalRepoBasePath = null;
            for (String blogSendDate : this.getConfig().getBlogSendDates()) {
                blogLocalRepoBasePath = this.blogLocalRepoBasePaths.get(index++);
                blogBasePath = markdownBasePath + blogSendDate;
                FileUtil.copyDirectory(blogBasePath, blogLocalRepoBasePath, null, false, Constant.IGNORE_MARK);
                FileUtil.deleteFile(blogLocalRepoBasePath + "_vnote.json");
                File file = new File(blogLocalRepoBasePath);
                String[] vswps = file.list(new FilenameFilter() {
                    @Override
                    public boolean accept(File dir, String name) {
                        File tempFile = new File(dir.getAbsolutePath() + "/" + name);
                        return tempFile.isFile() && name.endsWith(".vswp");
                    }
                });
                if (null != vswps && vswps.length > 0) {
                    for (String vswp : vswps) {
                        FileUtil.deleteFile(blogLocalRepoBasePath + vswp);
                    }
                }
            }

            //初始化Git实例对象
            this.git = GithubUtil.getGit(this.config.getGithubLocalCodeDir());
            Ref ref = GithubUtil.checkout(this.git, this.config.getGithubBlogBranchName());
            log.info("the return of git checkout:" + ref);
        }
    }

    /**
     * 根据配置文件配置的支持的博客平台列表,加载对应的博客平台的发布实现类
     */
    @Override
    protected void prepareBlogPlatform() {
        if (null != this.blogPublisherKeyList && this.blogPublisherKeyList.size() > 0) {
            if (null != this.blogPublisherClassNames && this.blogPublisherClassNames.size() > 0) {
                if (null == this.blogPublisherList) {
                    this.blogPublisherList = new ArrayList<>();
                }
                for (String blogPublisherKey : blogPublisherKeyList) {
                    String fileName = null;
                    String blogPublisherClassPath = null;
                    String fileNamePrefix = null;
                    BlogPublisher blogPublisher = null;
                    for (String blogPublisherClassName : blogPublisherClassNames) {
                        blogPublisherClassPath = blogPublisherClassName.replace(this.getClassPath(), "")
                                .replaceAll("/", ".").replace(".class", "");
                        fileName = FileUtil.getPureFileName(blogPublisherClassName);
                        fileNamePrefix = fileName.replace("BlogPublisher", "").toLowerCase();
                        if (!fileNamePrefix.equalsIgnoreCase(blogPublisherKey)) {
                            continue;
                        }
                        blogPublisher = ReflectionUtil.createInstance(blogPublisherClassPath);
                        if (null == blogPublisher) {
                            continue;
                        }
                        this.blogPublisherList.add(blogPublisher);
                    }
                }
            }
        }
    }

    /**
     * 查找所有需要发布为博客的Markdown文件
     *
     * @return
     */
    @Override
    protected void findTargetMarkdowns() {
        if (null != this.blogBasePaths && this.blogBasePaths.size() > 0) {
            List<String> markdownFileList = null;
            if (null == this.markdownFilePaths) {
                this.markdownFilePaths = new ArrayList<>();
            }
            for (String blogBasePath : this.blogBasePaths) {
                markdownFileList = FileUtil.listFiles(blogBasePath, new MarkdownFilenameFilter(), true);
                if (null != markdownFileList && markdownFileList.size() > 0) {
                    this.markdownFilePaths.addAll(markdownFileList);
                }
            }
            readMarkdownFileContent();
            buildFilePatterns();
        } else {
            String markdownBasePath = this.getConfig().getMarkdownBasePath();
            if (!markdownBasePath.endsWith("/")) {
                if (!markdownBasePath.endsWith("\\")) {
                    markdownBasePath = markdownBasePath + "/";
                }
            }

            String blogBasePath = null;
            List<String> markdownFileList = null;
            if (null == this.markdownFilePaths) {
                this.markdownFilePaths = new ArrayList<>();
            }
            for (String blogSendDate : this.getConfig().getBlogSendDates()) {
                blogBasePath = markdownBasePath + blogSendDate;
                markdownFileList = FileUtil.listFiles(blogBasePath, new MarkdownFilenameFilter(), true);
                if (null != markdownFileList && markdownFileList.size() > 0) {
                    this.markdownFilePaths.addAll(markdownFileList);
                }
            }
            readMarkdownFileContent();
            buildFilePatterns();
        }
    }

    /**
     * 发布博客内容至配置的各博客平台
     */
    @Override
    protected void blogSend() {
        //默认会首先将博客发布至Github上
        if (null != this.blogPublisherList && this.blogPublisherList.size() > 0) {
            String blogPublisherClassPath = null;
            BlogPublishParam blogPublishParam = null;
            for (BlogPublisher blogPublisher : this.blogPublisherList) {
                blogPublisherClassPath = blogPublisher.getClass().getName();
                blogPublisherClassPath = blogPublisherClassPath + "Param";
                blogPublishParam = ReflectionUtil.createInstance(blogPublisherClassPath);
                if (null == blogPublishParam) {
                    continue;
                }
                if ("GithubBlogPublisher".equals(blogPublisher.getClass().getSimpleName())) {
                    ReflectionUtil.setFieldValue(blogPublishParam, "git", this.git);
                    ReflectionUtil.setFieldValue(blogPublishParam, "filePatterns", getFilePatterns());
                }


                try {
                    blogPublisher.publish(blogPublishParam);
                } catch (Exception e) {
                    e.printStackTrace();
                    log.error("There was an exception occured when the method[blogSend] was called in class[{}] for a blog post.",
                            blogPublisher.getClass().getName(), e.getMessage());
                }
            }
        }
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

        //git push
        gitPush();

        //close git
        GithubUtil.closeGit(this.git);
    }

    /**
     * 读取待发布的Markdown文件的内容
     */
    private void readMarkdownFileContent() {
        if (null != this.markdownFilePaths && this.markdownFilePaths.size() > 0) {
            byte[] byteArray = null;
            if (null == this.markdownFileContents) {
                this.markdownFileContents = new ArrayList<>();
            }
            for (String markdownFilePath : this.markdownFilePaths) {
                markdownFilePath = StringUtil.fixedPathDelimiter(markdownFilePath, false);
                try {
                    byteArray = Files.readAllBytes(Paths.get(markdownFilePath));
                } catch (Exception e) {
                    log.error("While reading content from the file[{}],we occur Exception:\n{}", markdownFilePath, e.getMessage());
                }
                if (null == byteArray || byteArray.length <= 0) {
                    continue;
                }
                this.markdownFileContents.add(new String(byteArray, StandardCharsets.UTF_8));
            }
        }
    }

    /**
     * 构建需要push到Github远程仓库的文件的表达式,默认是相对github_local_code_dir
     */
    private void buildFilePatterns() {
        if (null != this.markdownFilePaths && this.markdownFilePaths.size() > 0) {
            List<String> filePatternList = new ArrayList<>();
            String imagesPath = null;
            String relative_image_path = null;
            String markdownDir = null;
            String wordDir = null;
            String wordFilePattern = null;
            File imgFilesPath = null;
            String[] imageFiles = null;
            String markdownBasePath = this.config.getMarkdownBasePath();
            if (StringUtil.isNotEmpty(markdownBasePath) && !markdownBasePath.endsWith("/")) {
                if (!markdownBasePath.endsWith("\\")) {
                    markdownBasePath = markdownBasePath + "/";
                }
            }
            for (String markdownFilePath : this.markdownFilePaths) {
                markdownFilePath = StringUtil.fixedPathDelimiter(markdownFilePath, false);

                if (StringUtil.isNotEmpty(this.config.getWordBasePath())) {
                    markdownFilePath = markdownFilePath.replace(
                            StringUtil.fixedPathDelimiter(this.config.getWordBasePath(), true), "");
                    if (null == markdownFilePath || "".equalsIgnoreCase(markdownFilePath)) {
                        continue;
                    }
                    markdownDir = markdownFilePath.substring(0, markdownFilePath.lastIndexOf("/") + 1);
                    if (this.getConfig().getGithubLocalCodeDir().endsWith("/")) {
                        imagesPath = this.getConfig().getGithubLocalCodeDir() + markdownDir + "images/";
                    } else if (this.getConfig().getGithubLocalCodeDir().endsWith("\\")) {
                        imagesPath = StringUtil.fixedPathDelimiter(this.getConfig().getGithubLocalCodeDir() + markdownDir + "images/");
                    } else {
                        imagesPath = this.getConfig().getGithubLocalCodeDir() + "/" + markdownDir + "images/";
                    }
                } else if (StringUtil.isNotEmpty(markdownBasePath)) {
                    markdownFilePath = markdownFilePath.replace(
                            StringUtil.fixedPathDelimiter(markdownBasePath, true), "");
                    markdownDir = markdownFilePath.substring(0, markdownFilePath.lastIndexOf("/") + 1);
                    if (this.getConfig().getGithubLocalCodeDir().endsWith("/")) {
                        imagesPath = this.getConfig().getGithubLocalCodeDir() + markdownDir + "images/";
                    } else if (this.getConfig().getGithubLocalCodeDir().endsWith("\\")) {
                        imagesPath = StringUtil.fixedPathDelimiter(this.getConfig().getGithubLocalCodeDir() + markdownDir + "images/");
                    } else {
                        imagesPath = this.getConfig().getGithubLocalCodeDir() + "/" + markdownDir + "images/";
                    }
                }

                relative_image_path = markdownDir + "images/";
                imgFilesPath = new File(imagesPath);
                if (imgFilesPath.exists() && imgFilesPath.isDirectory()) {
                    imageFiles = imgFilesPath.list(new ImageFilenameFilter());
                    if (null != imageFiles && imageFiles.length > 0) {
                        for (String imageFile : imageFiles) {
                            filePatternList.add(relative_image_path + imageFile);
                        }
                    }
                }
                filePatternList.add(markdownFilePath);
                //filePatternList.add(imagesPath);
                if (markdownDir.endsWith("/")) {
                    markdownDir = markdownDir.substring(0, markdownDir.lastIndexOf("/"));
                }

                if (StringUtil.isNotEmpty(this.config.getWordBasePath()) &&
                        StringUtil.isEmpty(markdownBasePath)) {
                    wordDir = markdownDir.substring(0, markdownDir.lastIndexOf("/") + 1);
                    wordFilePattern = wordDir + "*.docx";
                    if (!filePatternList.contains(wordFilePattern)) {
                        filePatternList.add(wordFilePattern);
                    }
                }
            }
            if (null != filePatternList && filePatternList.size() > 0) {
                this.setFilePatterns(filePatternList.toArray(new String[]{}));
            }
        }
    }

    private void gitPush() {
        Iterable<PushResult> pushResults = null;
        if (this.config.isSSH()) {
            pushResults = GithubUtil.pushWithSSH(this.git, this.config.getGithubPrivateKeyPath(),
                    this.config.getGithubBlogBranchName());
        } else {
            pushResults = GithubUtil.pushWithHttp(this.git, this.config.getGithubBlogBranchName(),
                    this.config.getGithubUserName(), this.config.getGithubPassword());
        }

        if (null != pushResults) {
            Iterator<PushResult> pushResultIterator = pushResults.iterator();
            PushResult pushResult = null;
            RemoteRefUpdate.Status status = null;
            while (pushResultIterator.hasNext()) {
                pushResult = pushResultIterator.next();
                status = pushResult.getRemoteUpdate("refs/heads/" + GithubUtil.BRANCH_MASTER).getStatus();
                System.out.println(status.toString());
            }
        }
    }
}
