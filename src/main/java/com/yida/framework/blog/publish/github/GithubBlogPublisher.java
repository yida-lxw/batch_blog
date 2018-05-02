package com.yida.framework.blog.publish.github;

import com.yida.framework.blog.publish.BlogPublisher;
import com.yida.framework.blog.utils.github.GithubUtil;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.api.PullResult;
import org.eclipse.jgit.dircache.DirCache;
import org.eclipse.jgit.revwalk.RevCommit;

/**
 * @Author Lanxiaowei
 * @Date 2018-02-12 17:02
 * @Description Github博客发布接口实现
 */
public class GithubBlogPublisher implements BlogPublisher<GithubBlogPublisherParam> {
    private Logger log = LogManager.getLogger(GithubBlogPublisher.class.getName());

    @Override
    public String publish(GithubBlogPublisherParam blogPublishParam) {
        //Git git = GithubUtil.getGit(blogPublishParam.getGithubLocalRepositoryPath());
        //Ref ref = GithubUtil.checkout(git, blogPublishParam.getGithubBlogBranchName());
        //log.info("the return of git checkout:" + ref);

        //Pull first to avoid conflict.
        Git git = blogPublishParam.getGit();
        PullResult pullResult = null;
        String remoteRepoPath = blogPublishParam.getGithubRemoteRepositoryPath();
        boolean ssh = blogPublishParam.isSshOrHttps();
        if (ssh) {
            pullResult = GithubUtil.pullWithSSH(git, blogPublishParam.getGithubBlogBranchName(), null,
                    blogPublishParam.getGithubPrivateKeyPath());
        } else if (remoteRepoPath.startsWith("https://") || remoteRepoPath.startsWith("http://")) {
            pullResult = GithubUtil.pullWithHttp(git, blogPublishParam.getGithubBlogBranchName(),
                    blogPublishParam.getGithubUserName(), blogPublishParam.getGithubPassword());
        } else {
            log.error("The remote repository url[{}] you assigned was incorrect totally.",
                    blogPublishParam.getGithubRemoteRepositoryPath());
            return null;
        }
        log.info("the return of git pull:" + pullResult);

        //git add
        DirCache dirCache = GithubUtil.add(git, blogPublishParam.getFilePatterns());
        log.info("the return of git add:" + dirCache);

        //git commit
        RevCommit revCommit = GithubUtil.commit(git, "commit via jgit for Github blog",
                blogPublishParam.getGithubAuthorName(), blogPublishParam.getGithubAuthorEmail(), true, false);
        log.info("the return of git commit:" + revCommit);
        return revCommit.toString();
    }
}
