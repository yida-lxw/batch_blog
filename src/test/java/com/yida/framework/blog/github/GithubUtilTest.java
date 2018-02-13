package com.yida.framework.blog.github;

import com.yida.framework.blog.utils.github.GithubUtil;
import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.api.PullResult;
import org.eclipse.jgit.dircache.DirCache;
import org.eclipse.jgit.lib.Ref;
import org.eclipse.jgit.revwalk.RevCommit;
import org.eclipse.jgit.transport.PushResult;
import org.eclipse.jgit.transport.RemoteRefUpdate;

import java.io.IOException;
import java.util.Iterator;

/**
 * @Author Lanxiaowei
 * @Date 2018-02-13 17:11
 * @Description 这里是类的描述信息
 */
public class GithubUtilTest {
    public static void main(String[] args) throws IOException {
        String localRepositoryPath = "G:/git4test/blog";
        String remoteRepoSSHUrl = "git@github.com:yida-lxw/blog.git";
        String remoteRepoHttpUrl = "https://github.com/yida-lxw/blog.git";
        String githubUserName = "yida-lxw";
        String githubPassword = "你猜";

        //Git git = getGit(localRepositoryPath);


        //git clone via http url
        //Git git = cloneRepositoryWithHttpAuth(remoteRepoHttpUrl, localRepositoryPath, githubUserName, githubPassword);
        //System.out.println("git clone via http:" + git);

        //git clone via ssh url
        //Git git = cloneRepositoryWithSSHAuth(remoteRepoSSHUrl, localRepositoryPath);
        //System.out.println("git clone via ssh:" + git);

        Git git = GithubUtil.getGit(localRepositoryPath);

        //git checkout master branch
        Ref ref = GithubUtil.checkout(git, GithubUtil.BRANCH_MASTER);
        System.out.println("ref:" + ref);

        //git add
        DirCache dirCache = GithubUtil.add(git);
        System.out.println(dirCache);

        //git commit
        RevCommit revCommit = GithubUtil.commit(git, "commit via jgit for Testing", "Lanxiaowei", "736031305@qq.com", true, false);
        System.out.println(revCommit);

        //git pull
        PullResult pullResult = GithubUtil.pullWithSSH(git);
        System.out.println("git pull:" + pullResult);

        //git push
        Iterable<PushResult> pushResults = GithubUtil.pushWithSSH(git);
        Iterator<PushResult> pushResultIterator = pushResults.iterator();
        PushResult pushResult = null;
        RemoteRefUpdate.Status status = null;
        while (pushResultIterator.hasNext()) {
            pushResult = pushResultIterator.next();
            status = pushResult.getRemoteUpdate("refs/heads/" + GithubUtil.BRANCH_MASTER).getStatus();
            System.out.println(status.toString());
        }


        //git close only after a sort of operation of git
        git.close();
    }
}
