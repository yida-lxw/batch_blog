package com.yida.framework.blog.config;

import com.yida.framework.blog.utils.Constant;
import com.yida.framework.blog.utils.common.DateUtil;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

/**
 * @Author Lanxiaowei
 * @Date 2018-01-16 00:35
 * @Description Blog配置实体类
 */
public class BlogConfig {
    private static final String PANDOC_HOME = "pandoc_home";
    private static final String WORD_BASEPATH = "word_basepath";
    private static final String MARKDOWN_BASEPATH = "markdown_basepath";
    private static final String BLOG_SEND_DATE = "blog_send_dates";
    private static final String GITHUB_USERNAME = "github_username";
    private static final String GITHUB_PASSWORD = "github_pwd";
    private static final String GITHUB_PRIVATE_KEY_PATH = "github_private_key_path";
    private static final String GITHUB_LOCAL_REPO_PATH = "github_local_repo_path";
    private static final String GITHUB_REMOTE_REPO_PATH = "github_remote_repo_path";
    private static final String GITHUB_BLOG_BRANCH_NAME = "github_blog_branch_name";
    private static final String GITHUB_LOCAL_CODE_DIR = "github_local_code_dir";
    private static final String GITHUB_AUTHOR_NAME = "github_author_name";
    private static final String GITHUB_AUTHOR_EMAIL = "github_author_email";
    private static final String BLOG_PLATFORM_SUPPORTED = "blog_platform_supported";

    /**
     * Pandoc的安装目录
     */
    private String pandocHome;
    /**
     * Word文档的存储根目录
     */
    private String wordBasePath;
    /**
     * Markdown文档的存储根目录,当你指定了此参数,即表示你的博客是直接采用Markdown编写的,
     * 此时不需要进行Word文档到Markdown之间的文件格式转换
     */
    private String markdownBasePath;
    /**
     * 博客发送日期
     */
    private String blogSendDate;
    /**
     * 博客发送日期(多个)
     * 有时候可能你积攒了很多天的博客，想要一次性发布出去，
     * 此时你可以配置多个日期，多个日期采用分号进行分割
     */
    private List<String> blogSendDates;
    /**
     * Github的登录账号
     */
    private String githubUserName;
    /**
     * Github的登录密码
     */
    private String githubPassword;

    /**
     * Github的本地仓库目录
     */
    private String githubLocalRepoPath;

    /**
     * Github的远程仓库URL
     */
    private String githubRemoteRepoPath;

    /**
     * Github博客的远程分支名称
     */
    private String githubBlogBranchName;

    /**
     * 本地项目代码的存放目录
     */
    private String githubLocalCodeDir;

    /**
     * Github提交操作设置的作者和提交者的姓名信息
     */
    private String githubAuthorName;

    /**
     * Github提交操作设置的作者和提交者的邮箱地址信息
     */
    private String githubAuthorEmail;

    /**
     * Github私钥存放地址
     */
    private String githubPrivateKeyPath;

    /**
     * 支持的所有博客平台列表,多个请采用分号;进行分割
     */
    private String blogPlatformSupported;


    private BlogConfig() {
        initialize();
    }

    private static class SingletonHolder {
        private static final BlogConfig INSTANCE = new BlogConfig();
    }

    public static final BlogConfig getInstance() {
        return SingletonHolder.INSTANCE;
    }

    /**
     * 初始化系统配置
     */
    public void initialize() {
        this.pandocHome = ConfigContext.getStringProperty(PANDOC_HOME);
        if (null == this.pandocHome || "".equals(this.pandocHome)) {
            this.pandocHome = ConfigContext.getPandocHome();
            if (null == this.pandocHome || "".equals(this.pandocHome)) {
                throw new IllegalArgumentException("There is no PANDOC_HOME env required parameter in your system path.");
            }
        }
        this.wordBasePath = ConfigContext.getStringProperty(WORD_BASEPATH);
        if (null == this.wordBasePath || "".equals(this.wordBasePath)) {
            this.markdownBasePath = ConfigContext.getStringProperty(MARKDOWN_BASEPATH);
            if (null == this.markdownBasePath || "".equals(this.markdownBasePath)) {
                //Word文档的存储目录或者Markdown文档的存储目录参数两者必须至少指定其中一个,否则程序无法继续,所以只好抛异常以示警示
                //若两者同时指定了,那么默认会以markdownBasePath参数为准
                throw new IllegalArgumentException("YOU MUST specify the storage path parameter[word_basepath or markdown_basepath] for the word document.");
            }
        }
        this.blogSendDate = ConfigContext.getStringProperty(BLOG_SEND_DATE);
        if (null == this.blogSendDate || "".equals(this.blogSendDate)) {
            //若用户未在配置文件中配置[blog_send_dates]这项，则默认会获取当前时间作为博客发送时间
            this.blogSendDate = DateUtil.format(new Date(), Constant.DEFAULT_DATE_PATTERN);
            this.blogSendDates = new ArrayList<>(1);
            this.blogSendDates.add(this.blogSendDate);
        } else {
            //若用户配置了多个日期，则表示用户需要一次性发布多天的博客
            if (-1 != this.blogSendDate.indexOf(";")) {
                this.blogSendDates = ConfigContext.getStringListProperty(BLOG_SEND_DATE);
            }
        }
        this.blogPlatformSupported = ConfigContext.getStringProperty(BLOG_PLATFORM_SUPPORTED);

        this.githubUserName = ConfigContext.getStringProperty(GITHUB_USERNAME);
        this.githubPassword = ConfigContext.getStringProperty(GITHUB_PASSWORD);
        this.githubPrivateKeyPath = ConfigContext.getStringProperty(GITHUB_PRIVATE_KEY_PATH);
        this.githubLocalRepoPath = ConfigContext.getStringProperty(GITHUB_LOCAL_REPO_PATH);
        this.githubRemoteRepoPath = ConfigContext.getStringProperty(GITHUB_REMOTE_REPO_PATH);
        this.githubBlogBranchName = ConfigContext.getStringProperty(GITHUB_BLOG_BRANCH_NAME);
        this.githubLocalCodeDir = ConfigContext.getStringProperty(GITHUB_LOCAL_CODE_DIR);
        this.githubAuthorName = ConfigContext.getStringProperty(GITHUB_AUTHOR_NAME);
        this.githubAuthorEmail = ConfigContext.getStringProperty(GITHUB_AUTHOR_EMAIL);
    }

    @Override
    public String toString() {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("Pandoc Home: " + getPandocHome() + "\n");
        stringBuilder.append("Word BasePath: " + getWordBasePath() + "\n");
        stringBuilder.append("Markdown BasePath: " + getMarkdownBasePath() + "\n");
        stringBuilder.append("Blog SendDate: " + getBlogSendDate() + "\n");
        stringBuilder.append("Blog Platform Supported List: " + getBlogPlatformSupported() + "\n");
        stringBuilder.append("Github UserName: " + getGithubUserName() + "\n");
        stringBuilder.append("Github Password: " + getGithubPassword() + "\n");
        stringBuilder.append("Github Private Key: " + getGithubPrivateKeyPath() + "\n");
        stringBuilder.append("Github Local Repository Path: " + getGithubLocalRepoPath() + "\n");
        stringBuilder.append("Github Remote Repository Path: " + getGithubRemoteRepoPath() + "\n");
        stringBuilder.append("Github Blog Remote Branch Name: " + getGithubBlogBranchName() + "\n");
        stringBuilder.append("Github Local Code directory: " + getGithubLocalCodeDir() + "\n");
        stringBuilder.append("Github Author/Committer Name: " + getGithubAuthorName() + "\n");
        stringBuilder.append("Github Author/Committer Email: " + getGithubAuthorEmail());
        return stringBuilder.toString();
    }

    public String getPandocHome() {
        pandocHome = adjustPath(pandocHome);
        return pandocHome;
    }

    public void setPandocHome(String pandocHome) {
        this.pandocHome = pandocHome;
    }

    public String getWordBasePath() {
        wordBasePath = adjustPath(wordBasePath);
        return wordBasePath;
    }

    public void setWordBasePath(String wordBasePath) {
        this.wordBasePath = wordBasePath;
    }

    public String getMarkdownBasePath() {
        markdownBasePath = adjustPath(markdownBasePath);
        return markdownBasePath;
    }

    public void setMarkdownBasePath(String markdownBasePath) {
        this.markdownBasePath = markdownBasePath;
    }

    public String getBlogSendDate() {
        return blogSendDate;
    }

    public void setBlogSendDate(String blogSendDate) {
        this.blogSendDate = blogSendDate;
    }

    public List<String> getBlogSendDates() {
        return blogSendDates;
    }

    public void setBlogSendDates(List<String> blogSendDates) {
        this.blogSendDates = blogSendDates;
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

    public String getGithubLocalRepoPath() {
        githubLocalRepoPath = adjustPath(githubLocalRepoPath);
        return githubLocalRepoPath;
    }

    public void setGithubLocalRepoPath(String githubLocalRepoPath) {
        this.githubLocalRepoPath = githubLocalRepoPath;
    }

    public String getGithubRemoteRepoPath() {
        githubRemoteRepoPath = adjustPath(githubRemoteRepoPath);
        return githubRemoteRepoPath;
    }

    public void setGithubRemoteRepoPath(String githubRemoteRepoPath) {
        this.githubRemoteRepoPath = githubRemoteRepoPath;
    }

    public String getGithubBlogBranchName() {
        return githubBlogBranchName;
    }

    public void setGithubBlogBranchName(String githubBlogBranchName) {
        this.githubBlogBranchName = githubBlogBranchName;
    }

    public String getGithubLocalCodeDir() {
        githubLocalCodeDir = adjustPath(githubLocalCodeDir);
        return githubLocalCodeDir;
    }

    public void setGithubLocalCodeDir(String githubLocalCodeDir) {
        this.githubLocalCodeDir = githubLocalCodeDir;
    }

    public String getGithubAuthorName() {
        return githubAuthorName;
    }

    public void setGithubAuthorName(String githubAuthorName) {
        this.githubAuthorName = githubAuthorName;
    }

    public String getGithubAuthorEmail() {
        return githubAuthorEmail;
    }

    public void setGithubAuthorEmail(String githubAuthorEmail) {
        this.githubAuthorEmail = githubAuthorEmail;
    }

    public String getGithubPrivateKeyPath() {
        githubPrivateKeyPath = adjustPath(githubPrivateKeyPath);
        return githubPrivateKeyPath;
    }

    public void setGithubPrivateKeyPath(String githubPrivateKeyPath) {
        this.githubPrivateKeyPath = githubPrivateKeyPath;
    }

    public String getBlogPlatformSupported() {
        return blogPlatformSupported;
    }

    public void setBlogPlatformSupported(String blogPlatformSupported) {
        this.blogPlatformSupported = blogPlatformSupported;
    }

    public List<String> getBlogPlatformSupportedList() {
        if (null == this.blogPlatformSupported || "".equals(this.blogPlatformSupported)) {
            return null;
        }
        this.blogPlatformSupported = this.blogPlatformSupported.replaceAll("; ", ";");
        String[] array = this.blogPlatformSupported.split(";");
        return Arrays.asList(array);
    }

    private String adjustPath(String path) {
        if (path.endsWith("\\")) {
            path = path.replaceAll("\\\\", "/");
        }
        if (!path.endsWith("/")) {
            path = path + "/";
        }
        return path;
    }
}
