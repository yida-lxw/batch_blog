package com.yida.framework.blog.publish.github;

import com.yida.framework.blog.publish.BlogPublishParam;

/**
 * @Author Lanxiaowei
 * @Date 2018-02-12 17:03
 * @Description 发布博客至Github所需的参数定义
 */
public class GithubBlogPublishParam extends BlogPublishParam {
    /**
     * 待提交到Github的文件的路径表达式,支持相对路径,默认相对本地仓库根目录
     */
    private String[] filePatterns;

    /**
     * Github本地仓库目录
     */
    private String githubLocalRepositoryPath;

    /**
     * Github博客远程仓库的访问URL:有Https和SSH两种协议格式,选择其中任意一种即可
     */
    private String githubRemoteRepositoryPath;

    /**
     * Github博客在本地仓库中的目录位置
     */
    private String githubBlogLocalDirectory;

    /**
     * Github私钥文件本地存放目录(当使用SSH协议格式的仓库URL时,你可能需要提供此参数)
     */
    private String githubPrivateKeyPath;

    /**
     * Github的登录账号(当使用Https协议格式的仓库URL时,你需要提供此参数)
     */
    private String githubUserName;

    /**
     * Github的登录密码(当使用Https协议格式的仓库URL时,你需要提供此参数)
     */
    private String githubPassword;

    /**
     * Github中的作者以及提交者的姓名信息
     */
    private String githubAuthorName;

    /**
     * Github中的作者以及提交者的Email信息
     */
    private String githubAuthorEmail;

    public String[] getFilePatterns() {
        return filePatterns;
    }

    public void setFilePatterns(String[] filePatterns) {
        this.filePatterns = filePatterns;
    }

    public String getGithubLocalRepositoryPath() {
        this.githubLocalRepositoryPath = this.config.getGithubLocalRepoPath();
        return githubLocalRepositoryPath;
    }

    public void setGithubLocalRepositoryPath(String githubLocalRepositoryPath) {
        this.githubLocalRepositoryPath = githubLocalRepositoryPath;
    }

    public String getGithubRemoteRepositoryPath() {
        this.githubRemoteRepositoryPath = this.config.getGithubRemoteRepoPath();
        return githubRemoteRepositoryPath;
    }

    public void setGithubRemoteRepositoryPath(String githubRemoteRepositoryPath) {
        this.githubRemoteRepositoryPath = githubRemoteRepositoryPath;
    }

    public String getGithubBlogLocalDirectory() {
        this.githubBlogLocalDirectory = this.config.getGithubLocalCodeDir();
        return githubBlogLocalDirectory;
    }

    public void setGithubBlogLocalDirectory(String githubBlogLocalDirectory) {
        this.githubBlogLocalDirectory = githubBlogLocalDirectory;
    }

    public String getGithubPrivateKeyPath() {
        this.githubPrivateKeyPath = this.config.getGithubPrivateKeyPath();
        return githubPrivateKeyPath;
    }

    public void setGithubPrivateKeyPath(String githubPrivateKeyPath) {
        this.githubPrivateKeyPath = githubPrivateKeyPath;
    }

    public String getGithubUserName() {
        this.githubUserName = this.config.getGithubUserName();
        return githubUserName;
    }

    public void setGithubUserName(String githubUserName) {
        this.githubUserName = githubUserName;
    }

    public String getGithubPassword() {
        this.githubPassword = this.config.getGithubPassword();
        return githubPassword;
    }

    public void setGithubPassword(String githubPassword) {
        this.githubPassword = githubPassword;
    }

    public String getGithubAuthorName() {
        this.githubAuthorName = this.config.getGithubAuthorName();
        return githubAuthorName;
    }

    public void setGithubAuthorName(String githubAuthorName) {
        this.githubAuthorName = githubAuthorName;
    }

    public String getGithubAuthorEmail() {
        this.githubAuthorEmail = this.config.getGithubAuthorEmail();
        return githubAuthorEmail;
    }

    public void setGithubAuthorEmail(String githubAuthorEmail) {
        this.githubAuthorEmail = githubAuthorEmail;
    }
}
