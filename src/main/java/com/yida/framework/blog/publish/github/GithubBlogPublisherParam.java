package com.yida.framework.blog.publish.github;

import com.yida.framework.blog.publish.BlogPublishParam;
import org.eclipse.jgit.api.Git;

/**
 * @Author Lanxiaowei
 * @Date 2018-02-12 17:03
 * @Description 发布博客至Github所需的参数定义
 */
public class GithubBlogPublisherParam extends BlogPublishParam {
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
     * Github博客的远程分支名称
     */
    private String githubBlogBranchName;

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

    private Git git;

    /**
     * 远程仓库URL是SSH格式还是HTTPS格式,true表示SSH,false表示https
     */
    private boolean sshOrHttps;

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

    public String getGithubBlogBranchName() {
        this.githubBlogBranchName = this.config.getGithubBlogBranchName();
        return githubBlogBranchName;
    }

    public void setGithubBlogBranchName(String githubBlogBranchName) {
        this.githubBlogBranchName = githubBlogBranchName;
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

    public Git getGit() {
        return git;
    }

    public void setGit(Git git) {
        this.git = git;
    }

    public boolean isSshOrHttps() {
        if (null == this.githubRemoteRepositoryPath || "".equals(this.githubRemoteRepositoryPath)) {
            sshOrHttps = false;
        } else {
            if (this.githubRemoteRepositoryPath.startsWith("https://")) {
                sshOrHttps = false;
            } else if (this.githubRemoteRepositoryPath.startsWith("git@")) {
                sshOrHttps = true;
            } else {
                sshOrHttps = false;
            }
        }
        return sshOrHttps;
    }

    public void setSshOrHttps(boolean sshOrHttps) {
        this.sshOrHttps = sshOrHttps;
    }
}
