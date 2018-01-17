package com.yida.framework.blog.config;

import com.yida.framework.blog.utils.Constant;
import com.yida.framework.blog.utils.common.DateUtil;

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
                //Word文档的存储目录参数必须指定,否则程序无法继续,所以只好抛异常以示警示
                throw new IllegalArgumentException("YOU MUST specify the storage path parameter[word_basepath or markdown_basepath] for the word document.");
            }
        }
        this.blogSendDate = ConfigContext.getStringProperty(BLOG_SEND_DATE);
        if (null == this.blogSendDate || "".equals(this.blogSendDate)) {
            //若用户未在配置文件中配置[blog_send_dates]这项，则默认会获取当前时间作为博客发送时间
            this.blogSendDate = DateUtil.format(new Date(), Constant.DEFAULT_DATE_PATTERN);
        } else {
            //若用户配置了多个日期，则表示用户需要一次性发布多天的博客
            if (-1 != this.blogSendDate.indexOf(";")) {
                this.blogSendDates = ConfigContext.getStringListProperty(BLOG_SEND_DATE);
            }
        }
        this.githubUserName = ConfigContext.getStringProperty(GITHUB_USERNAME);
        this.githubPassword = ConfigContext.getStringProperty(GITHUB_PASSWORD);
    }

    @Override
    public String toString() {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("Pandoc Home: " + getPandocHome() + "\n");
        stringBuilder.append("Word BasePath: " + getWordBasePath() + "\n");
        stringBuilder.append("Markdown BasePath: " + getMarkdownBasePath() + "\n");
        stringBuilder.append("Blog SendDate: " + getBlogSendDate() + "\n");
        stringBuilder.append("Github UserName: " + getGithubUserName() + "\n");
        stringBuilder.append("Github Password: " + getGithubPassword() + "\n");
        return stringBuilder.toString();
    }

    public String getPandocHome() {
        return pandocHome;
    }

    public void setPandocHome(String pandocHome) {
        this.pandocHome = pandocHome;
    }

    public String getWordBasePath() {
        return wordBasePath;
    }

    public void setWordBasePath(String wordBasePath) {
        this.wordBasePath = wordBasePath;
    }

    public String getMarkdownBasePath() {
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
}
