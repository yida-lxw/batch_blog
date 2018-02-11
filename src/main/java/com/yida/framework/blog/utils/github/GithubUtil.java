package com.yida.framework.blog.utils.github;

import com.jcraft.jsch.JSch;
import com.jcraft.jsch.JSchException;
import com.jcraft.jsch.Session;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.eclipse.jgit.api.*;
import org.eclipse.jgit.api.errors.GitAPIException;
import org.eclipse.jgit.dircache.DirCache;
import org.eclipse.jgit.lib.PersonIdent;
import org.eclipse.jgit.lib.Ref;
import org.eclipse.jgit.lib.Repository;
import org.eclipse.jgit.merge.MergeStrategy;
import org.eclipse.jgit.revwalk.RevCommit;
import org.eclipse.jgit.storage.file.FileRepositoryBuilder;
import org.eclipse.jgit.transport.*;
import org.eclipse.jgit.util.FS;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

/**
 * @Author Lanxiaowei
 * @Date 2018-02-10 13:23
 * @Description Github操作工具类, 基于JGit封装
 */
public class GithubUtil {
    public static final String BRANCH_MASTER = "master";

    private static Logger log = LogManager.getLogger(GithubUtil.class.getName());

    public static final String USER_HOME = fixedPathDelimiter(System.getProperty("user.home"));
    public static final String PRIVATE_KEY = USER_HOME + "/.ssh/id_rsa";

    private static final ThreadLocal<SshSessionFactory> sshSessionFactoryThreadLocal = new ThreadLocal<>();

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

        Git git = getGit(localRepositoryPath);

        //git checkout master branch
        Ref ref = checkout(git, BRANCH_MASTER);
        System.out.println("ref:" + ref);

        //git add
        DirCache dirCache = add(git);
        System.out.println(dirCache);

        //git commit
        RevCommit revCommit = commit(git, "commit via jgit for Testing", "Lanxiaowei", "736031305@qq.com", true, false);
        System.out.println(revCommit);

        //git pull
        PullResult pullResult = pullWithSSH(git);
        System.out.println("git pull:" + pullResult);

        //git push
        Iterable<PushResult> pushResults = pushWithSSH(git);
        Iterator<PushResult> pushResultIterator = pushResults.iterator();
        PushResult pushResult = null;
        RemoteRefUpdate.Status status = null;
        while (pushResultIterator.hasNext()) {
            pushResult = pushResultIterator.next();
            status = pushResult.getRemoteUpdate("refs/heads/" + BRANCH_MASTER).getStatus();
            System.out.println(status.toString());
        }
    }

    /**
     * 判断一个本地仓库是否存在,即本地仓库目录下是否拥有一个名称为.git的隐藏文件夹
     *
     * @param localRepositoryPath 本地Git仓库目录路径,比如:G:/git-local/blog/
     * @return
     */
    public static boolean isLocalRepoExists(String localRepositoryPath) {
        localRepositoryPath = fixedPathDelimiter(localRepositoryPath);
        FileRepositoryBuilder repositoryBuilder = new FileRepositoryBuilder()
                .setGitDir(new File(localRepositoryPath + ".git"))
                .setMustExist(false);
        Repository repository = null;
        boolean result = false;
        try {
            repository = repositoryBuilder.build();
            result = repository.getRef("HEAD") != null;
        } catch (IOException e) {
            log.error("Building the Repository instance with the FileRepositoryBuilder class occur exception:\n{}", e.getMessage());
        }
        return result;
    }

    /**
     * 初始化一个本地git仓库,类似于执行git init命令
     *
     * @param localRepositoryPath 本地仓库目录
     * @return
     */
    public static Git initLocalRepo(String localRepositoryPath) {
        localRepositoryPath = fixedPathDelimiter(localRepositoryPath);
        Git git = null;
        try {
            git = Git.init()
                    .setBare(true)
                    .setDirectory(new File(localRepositoryPath + ".git"))
                    .call();
        } catch (GitAPIException e) {
            log.error("While Initializating the local repository with localRepositoryPath[{}],we occur exception:\n{}",
                    localRepositoryPath, e.getMessage());
        }
        return git;
    }

    /**
     * 打开本地仓库,获取Git实例对象,为执行后续Git操作做准备
     *
     * @param localRepositoryPath 本地仓库目录
     * @return
     */
    public static Git openLocalRepo(String localRepositoryPath) {
        localRepositoryPath = fixedPathDelimiter(localRepositoryPath);
        Git git = null;
        try {
            return Git.open(new File(localRepositoryPath));
        } catch (IOException e) {
            log.error("While Opening the local repository with localRepositoryPath[{}],we occur exception:\n{}",
                    localRepositoryPath, e.getMessage());
        }
        return git;
    }

    /**
     * 获取Git实例对象,此对象请不要重复创建
     *
     * @param localRepositoryPath 本地仓库目录
     * @return
     */
    public static Git getGit(String localRepositoryPath) {
        Git git = null;
        if (isLocalRepoExists(localRepositoryPath)) {
            git = openLocalRepo(localRepositoryPath);
        } else {
            git = initLocalRepo(localRepositoryPath);
        }
        return git;
    }

    /**
     * 克隆远程仓库到本地仓库-Http协议方式
     *
     * @param remoteRepoPath      远程Github仓库的URL地址-Http格式
     * @param localRepositoryPath 本地仓库目录
     * @param githubUserName      github登录账号
     * @param githubPassword      github登录密码
     * @param branchName          分支名称即你需要克隆哪个分支,默认为master分支
     * @param bare                是否只初始化远程仓库至本地,但不下载远程仓库里的任何文件至本地仓库
     * @param cloneAllBranches    是否克隆所有分支
     * @param remote              设置远程仓库的映射名称,不设置的话,默认值为origin
     * @return
     */
    public static Git cloneRepositoryWithHttpAuth(String remoteRepoPath, String localRepositoryPath,
                                                  String githubUserName, String githubPassword,
                                                  String branchName, boolean bare, boolean cloneAllBranches,
                                                  String remote) {
        Git git = null;
        try {
            CloneCommand cloneCommand = Git.cloneRepository()
                    .setBare(bare)
                    .setURI(remoteRepoPath)
                    .setDirectory(new File(localRepositoryPath))
                    .setCloneAllBranches(cloneAllBranches);
            if (null != branchName && !"".equals(branchName)) {
                cloneCommand = cloneCommand.setBranch("refs/heads/" + branchName);
            }
            if (null != githubUserName && !"".equals(githubUserName)) {
                cloneCommand = cloneCommand.setCredentialsProvider(
                        new UsernamePasswordCredentialsProvider(githubUserName, githubPassword));
            }
            if (null != remote && !"".equals(remote)) {
                //The default value for "remote" is [origin]
                cloneCommand = cloneCommand.setRemote(remote);
            }
            git = cloneCommand.call();
        } catch (GitAPIException e) {
            log.error("While Cloning the remote repository to local repository with remoteRepoPath-HTTP[{}] and localRepositoryPath[{}],we occur exception:\n{}",
                    remoteRepoPath, localRepositoryPath, e.getMessage());
        }
        return git;
    }

    /**
     * 克隆远程仓库到本地仓库
     *
     * @param remoteRepoPath      远程Github仓库的URL地址
     * @param localRepositoryPath 本地仓库目录
     * @param githubUserName      github登录账号
     * @param githubPassword      github登录密码
     * @param branchName          分支名称即你需要克隆哪个分支,默认为master分支
     * @param bare                是否只初始化远程仓库至本地,但不下载远程仓库里的任何文件至本地仓库
     * @param cloneAllBranches    是否克隆所有分支
     * @return
     */
    public static Git cloneRepositoryWithHttpAuth(String remoteRepoPath, String localRepositoryPath,
                                                  String githubUserName, String githubPassword,
                                                  String branchName, boolean bare, boolean cloneAllBranches) {
        return cloneRepositoryWithHttpAuth(remoteRepoPath, localRepositoryPath, githubUserName, githubPassword, branchName,
                bare, cloneAllBranches, null);
    }

    /**
     * 克隆远程仓库到本地仓库
     *
     * @param remoteRepoPath      远程Github仓库的URL地址
     * @param localRepositoryPath 本地仓库目录
     * @param githubUserName      github登录账号
     * @param githubPassword      github登录密码
     * @param branchName          分支名称即你需要克隆哪个分支,默认为master分支
     * @param bare                是否只初始化远程仓库至本地,但不下载远程仓库里的任何文件至本地仓库
     * @return
     */
    public static Git cloneRepositoryWithHttpAuth(String remoteRepoPath, String localRepositoryPath,
                                                  String githubUserName, String githubPassword,
                                                  String branchName, boolean bare) {
        return cloneRepositoryWithHttpAuth(remoteRepoPath, localRepositoryPath, githubUserName, githubPassword, branchName,
                bare, false, null);
    }

    /**
     * 克隆远程仓库到本地仓库
     *
     * @param remoteRepoPath      远程Github仓库的URL地址
     * @param localRepositoryPath 本地仓库目录
     * @param githubUserName      github登录账号
     * @param githubPassword      github登录密码
     * @param branchName          分支名称即你需要克隆哪个分支,默认为master分支
     * @return
     */
    public static Git cloneRepositoryWithHttpAuth(String remoteRepoPath, String localRepositoryPath,
                                                  String githubUserName, String githubPassword,
                                                  String branchName) {
        return cloneRepositoryWithHttpAuth(remoteRepoPath, localRepositoryPath, githubUserName, githubPassword, branchName,
                false, false, null);
    }

    /**
     * 克隆远程仓库到本地仓库
     *
     * @param remoteRepoPath      远程Github仓库的URL地址
     * @param localRepositoryPath 本地仓库目录
     * @param githubUserName      github登录账号
     * @param githubPassword      github登录密码
     * @return
     */
    public static Git cloneRepositoryWithHttpAuth(String remoteRepoPath, String localRepositoryPath,
                                                  String githubUserName, String githubPassword) {
        return cloneRepositoryWithHttpAuth(remoteRepoPath, localRepositoryPath, githubUserName, githubPassword, null,
                false, false, null);
    }

    /**
     * 克隆远程仓库到本地仓库
     *
     * @param remoteRepoPath      远程Github仓库的URL地址
     * @param localRepositoryPath 本地仓库目录
     * @return
     */
    public static Git cloneRepositoryWithHttpAuth(String remoteRepoPath, String localRepositoryPath) {
        return cloneRepositoryWithHttpAuth(remoteRepoPath, localRepositoryPath, null, null, null, false, false, null);
    }

    /**
     * 克隆远程仓库到本地仓库-SSH协议方式
     *
     * @param remoteRepoPath      远程Github仓库的URL地址-Http格式
     * @param localRepositoryPath 本地仓库目录
     * @param privateKey          本地私钥文件存放地址,默认值为C:/Users/Administrator/.ssh/id_rsa
     * @param branchName          分支名称即你需要克隆哪个分支,默认为master分支
     * @param bare                是否只初始化远程仓库至本地,但不下载远程仓库里的任何文件至本地仓库
     * @param cloneAllBranches    是否克隆所有分支
     * @param remote              设置远程仓库的映射名称,不设置的话,默认值为origin
     * @return
     */
    public static Git cloneRepositoryWithSSHAuth(String remoteRepoPath, String localRepositoryPath,
                                                 String privateKey, String branchName,
                                                 boolean bare, boolean cloneAllBranches, String remote) {
        SshSessionFactory sshSessionFactory = createSshSessionFactory(privateKey);
        Git git = null;
        try {
            CloneCommand cloneCommand = Git.cloneRepository()
                    .setBare(bare)
                    .setURI(remoteRepoPath)
                    .setDirectory(new File(localRepositoryPath))
                    .setCloneAllBranches(cloneAllBranches);
            if (null != branchName && !"".equals(branchName)) {
                cloneCommand = cloneCommand.setBranch("refs/heads/" + branchName);
            }
            if (null != remote && !"".equals(remote)) {
                //The default value for "remote" is [origin]
                cloneCommand = cloneCommand.setRemote(remote);
            }
            if (null != sshSessionFactory) {
                cloneCommand = cloneCommand.setTransportConfigCallback(new TransportConfigCallback() {
                    @Override
                    public void configure(Transport transport) {
                        SshTransport sshTransport = (SshTransport) transport;
                        sshTransport.setSshSessionFactory(sshSessionFactory);
                    }
                });
            }
            git = cloneCommand.call();
        } catch (GitAPIException e) {
            log.error("While Cloning the remote repository to local repository with remoteRepoPath-SSH[{}] and localRepositoryPath[{}],we occur exception:\n{}",
                    remoteRepoPath, localRepositoryPath, e.getMessage());
        }
        return git;
    }

    /**
     * 克隆远程仓库到本地仓库-SSH协议方式
     *
     * @param remoteRepoPath      远程Github仓库的URL地址-Http格式
     * @param localRepositoryPath 本地仓库目录
     * @param privateKey          本地私钥文件存放地址,默认值为C:/Users/Administrator/.ssh/id_rsa
     * @param branchName          分支名称即你需要克隆哪个分支,默认为master分支
     * @param bare                是否只初始化远程仓库至本地,但不下载远程仓库里的任何文件至本地仓库
     * @param cloneAllBranches    是否克隆所有分支
     * @return
     */
    public static Git cloneRepositoryWithSSHAuth(String remoteRepoPath, String localRepositoryPath,
                                                 String privateKey, String branchName,
                                                 boolean bare, boolean cloneAllBranches) {
        return cloneRepositoryWithSSHAuth(remoteRepoPath, localRepositoryPath, privateKey, branchName, bare, cloneAllBranches, null);
    }

    /**
     * 克隆远程仓库到本地仓库-SSH协议方式
     *
     * @param remoteRepoPath      远程Github仓库的URL地址-Http格式
     * @param localRepositoryPath 本地仓库目录
     * @param privateKey          本地私钥文件存放地址,默认值为C:/Users/Administrator/.ssh/id_rsa
     * @param branchName          分支名称即你需要克隆哪个分支,默认为master分支
     * @param bare                是否只初始化远程仓库至本地,但不下载远程仓库里的任何文件至本地仓库
     * @return
     */
    public static Git cloneRepositoryWithSSHAuth(String remoteRepoPath, String localRepositoryPath,
                                                 String privateKey, String branchName, boolean bare) {
        return cloneRepositoryWithSSHAuth(remoteRepoPath, localRepositoryPath, privateKey, branchName, bare, false, null);
    }

    /**
     * 克隆远程仓库到本地仓库-SSH协议方式
     *
     * @param remoteRepoPath      远程Github仓库的URL地址-Http格式
     * @param localRepositoryPath 本地仓库目录
     * @param privateKey          本地私钥文件存放地址,默认值为C:/Users/Administrator/.ssh/id_rsa
     * @param branchName          分支名称即你需要克隆哪个分支,默认为master分支
     * @return
     */
    public static Git cloneRepositoryWithSSHAuth(String remoteRepoPath, String localRepositoryPath,
                                                 String privateKey, String branchName) {
        return cloneRepositoryWithSSHAuth(remoteRepoPath, localRepositoryPath, privateKey, branchName, false, false, null);
    }

    /**
     * 克隆远程仓库到本地仓库-SSH协议方式
     *
     * @param remoteRepoPath      远程Github仓库的URL地址-Http格式
     * @param localRepositoryPath 本地仓库目录
     * @param privateKey          本地私钥文件存放地址,默认值为C:/Users/Administrator/.ssh/id_rsa
     * @return
     */
    public static Git cloneRepositoryWithSSHAuth(String remoteRepoPath, String localRepositoryPath, String privateKey) {
        return cloneRepositoryWithSSHAuth(remoteRepoPath, localRepositoryPath, privateKey, null, false, false, null);
    }

    /**
     * 克隆远程仓库到本地仓库-SSH协议方式
     *
     * @param remoteRepoPath      远程Github仓库的URL地址-Http格式
     * @param localRepositoryPath 本地仓库目录
     * @return
     */
    public static Git cloneRepositoryWithSSHAuth(String remoteRepoPath, String localRepositoryPath) {
        return cloneRepositoryWithSSHAuth(remoteRepoPath, localRepositoryPath, null, null, false, false, null);
    }

    /**
     * 将文件添加至本地暂存区,相当于git add命令
     *
     * @param git          Git实例对象
     * @param filePatterns 需要添加的文件表达式,默认是相对本地仓库根目录
     * @param update       是否开启更新模式,在更新模式下,不会添加新文件,只会更新已有文件的内容,默认不开启
     */
    public static DirCache add(Git git, String[] filePatterns, boolean update) {
        AddCommand addCommand = git.add().setUpdate(update);
        if (null != filePatterns && filePatterns.length > 0) {
            Set<String> filePatternSet = new HashSet<String>(Arrays.asList(filePatterns));
            for (String filePattern : filePatternSet) {
                if (null == filePattern) {
                    continue;
                }
                if ("".equals(filePattern)) {
                    filePattern = ".";
                }
                addCommand = addCommand.addFilepattern(filePattern);
            }
        } else {
            // default add all files
            addCommand = addCommand.addFilepattern(".");
        }
        try {
            return addCommand.call();
        } catch (GitAPIException e) {
            log.error("While add File to the local repository with filePatterns[{}],we occur exception:\n{}",
                    filePatterns, e.getMessage());
            return null;
        }
    }

    /**
     * 将文件添加至本地暂存区,相当于git add命令
     *
     * @param git          Git实例对象
     * @param filePatterns 需要添加的文件表达式,默认是相对本地仓库根目录
     */
    public static DirCache add(Git git, String[] filePatterns) {
        return add(git, filePatterns, false);
    }

    /**
     * 将文件添加至本地暂存区,相当于git add命令
     *
     * @param git Git实例对象
     */
    public static DirCache add(Git git) {
        return add(git, (String) null, false);
    }

    /**
     * 将文件添加至本地暂存区,相当于git add命令
     *
     * @param git         Git实例对象
     * @param filePattern 需要添加的文件表达式,默认是相对本地仓库根目录
     * @param update      是否开启更新模式,在更新模式下,不会添加新文件,只会更新已有文件的内容,默认不开启
     */
    public static DirCache add(Git git, String filePattern, boolean update) {
        return add(git, (null == filePattern) ? null : new String[]{filePattern}, update);
    }

    /**
     * 将文件添加至本地暂存区,相当于git add命令
     *
     * @param git         Git实例对象
     * @param filePattern 需要添加的文件表达式,默认是相对本地仓库根目录
     */
    public static DirCache add(Git git, String filePattern) {
        return add(git, filePattern, false);
    }

    /**
     * 将文件从Git暂存区移除,相当于git rm命令
     *
     * @param git          Git实例对象
     * @param filePatterns 需要删除的文件表达式,默认是相对本地仓库根目录
     * @param cached       是否只是将文件从暂存区移除,但不真实将文件从本地仓库中删除,默认为false
     * @return
     */
    public static DirCache rm(Git git, String[] filePatterns, boolean cached) {
        if (null == filePatterns || filePatterns.length <= 0) {
            return null;
        }
        RmCommand rmCommand = git.rm().setCached(cached);
        Set<String> filePatternSet = new HashSet<String>(Arrays.asList(filePatterns));
        for (String filePattern : filePatternSet) {
            if (null == filePattern || "".equals(filePattern)) {
                continue;
            }
            rmCommand = rmCommand.addFilepattern(filePattern);
        }
        try {
            return rmCommand.call();
        } catch (GitAPIException e) {
            log.error("While remove File from the local repository with filePatterns[{}],we occur exception:\n{}",
                    filePatterns, e.getMessage());
            return null;
        }
    }

    /**
     * 将文件从Git暂存区移除,相当于git rm命令
     *
     * @param git          Git实例对象
     * @param filePatterns 需要删除的文件表达式,默认是相对本地仓库根目录
     * @return
     */
    public static DirCache rm(Git git, String[] filePatterns) {
        return rm(git, filePatterns, false);
    }

    /**
     * 将文件从Git暂存区移除,相当于git rm命令
     *
     * @param git         Git实例对象
     * @param filePattern 需要删除的文件表达式,默认是相对本地仓库根目录
     * @param cached      是否只是将文件从暂存区移除,但不真实将文件从本地仓库中删除,默认为false
     * @return
     */
    public static DirCache rm(Git git, String filePattern, boolean cached) {
        if (null == filePattern || "".equals(filePattern)) {
            return null;
        }
        return rm(git, new String[]{filePattern}, cached);
    }

    /**
     * 将文件从Git暂存区移除,相当于git rm命令
     *
     * @param git         Git实例对象
     * @param filePattern 需要删除的文件表达式,默认是相对本地仓库根目录
     * @return
     */
    public static DirCache rm(Git git, String filePattern) {
        return rm(git, filePattern, false);
    }

    /**
     * 检出某个分支到本地仓库,类似于执行git checkout命令
     *
     * @param git               Git实例对象
     * @param branchName        分支名称,若不指定或留空,则默认会检出master分支至本地仓库
     * @param createBranch      是否自动创建本地分支(当本地仓库不存在该分支时),默认为true
     * @param setupUpstreamMode 自动创建本地分支时,自动与远程仓库的分支关联
     * @return
     */
    public static Ref checkout(Git git, String branchName, boolean createBranch,
                               CreateBranchCommand.SetupUpstreamMode setupUpstreamMode) {
        String actualBranchName = (null == branchName || "".equals(branchName)) ? BRANCH_MASTER : branchName;
        try {
            return git.checkout()
                    .setCreateBranch(createBranch)
                    .setForce(true)
                    .setName(actualBranchName)
                    .setStartPoint("origin/" + actualBranchName)
                    .setUpstreamMode(null == setupUpstreamMode ? CreateBranchCommand.SetupUpstreamMode.SET_UPSTREAM : setupUpstreamMode).call();
        } catch (GitAPIException e) {
            log.error("While checkout a branch to the local repository with branchName[{}],we occur exception:\n{}",
                    branchName, e.getMessage());
            return null;
        }
    }

    /**
     * 检出某个分支到本地仓库,类似于执行git checkout命令
     *
     * @param git          Git实例对象
     * @param branchName   分支名称,若不指定或留空,则默认会检出master分支至本地仓库
     * @param createBranch 是否自动创建本地分支(当本地仓库不存在该分支时),默认为true
     * @return
     */
    public static Ref checkout(Git git, String branchName, boolean createBranch) {
        return checkout(git, branchName, createBranch, null);
    }

    /**
     * 检出某个分支到本地仓库,类似于执行git checkout命令
     *
     * @param git        Git实例对象
     * @param branchName 分支名称,若不指定或留空,则默认会检出master分支至本地仓库
     * @return
     */
    public static Ref checkout(Git git, String branchName) {
        return checkout(git, branchName, true, null);
    }

    /**
     * 检出某个分支到本地仓库,类似于执行git checkout命令
     *
     * @param git Git实例对象
     * @return
     */
    public static Ref checkout(Git git) {
        return checkout(git, null, true, null);
    }

    /**
     * git提交操作即将新增/更新/删除的文件提交到本地仓库
     *
     * @param git            Git实例对象
     * @param commitMessage  提交注释说明信息
     * @param committerName  提交者的姓名
     * @param committerEmail 提交者的邮箱地址
     * @param allowEmpty     是否允许空提交
     * @param amend          修改当前分支的冲突
     */
    public static RevCommit commit(Git git, String commitMessage,
                                   String committerName, String committerEmail,
                                   boolean allowEmpty, boolean amend) {
        CommitCommand commitCommand = git.commit()
                .setAll(true)
                .setAllowEmpty(allowEmpty)
                .setAmend(amend);
        if ((null != committerName && !"".equals(committerName)) ||
                (null != committerEmail && !"".equals(committerEmail))) {
            commitCommand = commitCommand.setAuthor(new PersonIdent(committerName, committerEmail));
            commitCommand = commitCommand.setCommitter(committerName, committerEmail);
        }
        if (null != commitMessage && !"".equals(commitMessage)) {
            commitCommand = commitCommand.setMessage(commitMessage);
        }
        try {
            return commitCommand.call();
        } catch (GitAPIException e) {
            log.error("While commit File to the local repository with message[{}],we occur exception:\n{}",
                    commitMessage, e.getMessage());
            return null;
        }
    }

    /**
     * git提交操作即将新增/更新/删除的文件提交到本地仓库
     *
     * @param git            Git实例对象
     * @param commitMessage  提交注释说明信息
     * @param committerName  提交者的姓名
     * @param committerEmail 提交者的邮箱地址
     * @param allowEmpty     是否允许空提交
     */
    public static RevCommit commit(Git git, String commitMessage,
                                   String committerName, String committerEmail,
                                   boolean allowEmpty) {
        return commit(git, commitMessage, committerName, committerEmail, allowEmpty, false);
    }

    /**
     * git提交操作即将新增/更新/删除的文件提交到本地仓库
     *
     * @param git            Git实例对象
     * @param commitMessage  提交注释说明信息
     * @param committerName  提交者的姓名
     * @param committerEmail 提交者的邮箱地址
     */
    public static RevCommit commit(Git git, String commitMessage,
                                   String committerName, String committerEmail) {
        return commit(git, commitMessage, committerName, committerEmail, true, false);
    }

    /**
     * git提交操作即将新增/更新/删除的文件提交到本地仓库
     *
     * @param git           Git实例对象
     * @param commitMessage 提交注释说明信息
     */
    public static RevCommit commit(Git git, String commitMessage) {
        return commit(git, commitMessage, null, null, true, false);
    }

    /**
     * git提交操作即将新增/更新/删除的文件提交到本地仓库
     *
     * @param git Git实例对象
     */
    public static RevCommit commit(Git git) {
        return commit(git, null, null, null, true, false);
    }

    /**
     * 从远程仓库拉取最新内容至本地仓库,相当于执行git pull命令
     *
     * @param git              Git实例对象
     * @param remoteBranchName 远程分支名称,默认为master
     * @param remote           远程仓库的别名
     * @param privateKeyPath   本地私钥文件的存放路径
     * @param mergeStrategy    文件合并策略,默认为RECURSIVE
     * @return
     */
    public static PullResult pullWithSSH(Git git, String remoteBranchName, String remote,
                                         String privateKeyPath, MergeStrategy mergeStrategy) {
        PullCommand pullCommand = git.pull()
                .setRemote((null == remote || "".equals(remote)) ? "origin" : remote)
                .setRemoteBranchName((null == remoteBranchName || "".equals(remoteBranchName)) ?
                        BRANCH_MASTER : remoteBranchName)
                .setStrategy(null == mergeStrategy ? MergeStrategy.RECURSIVE : mergeStrategy);
        SshSessionFactory sshSessionFactory = createSshSessionFactory(privateKeyPath);
        if (null != sshSessionFactory) {
            pullCommand = pullCommand.setTransportConfigCallback(new TransportConfigCallback() {
                @Override
                public void configure(Transport transport) {
                    SshTransport sshTransport = (SshTransport) transport;
                    sshTransport.setSshSessionFactory(sshSessionFactory);
                }
            });
        }
        try {
            return pullCommand.call();
        } catch (GitAPIException e) {
            log.error("While pull from the remote repository with privateKeyPath[{}],we occur exception:\n{}",
                    privateKeyPath, e.getMessage());
            return null;
        }
    }


    /**
     * 从远程仓库拉取最新内容至本地仓库,相当于执行git pull命令
     *
     * @param git              Git实例对象
     * @param remoteBranchName 远程分支名称,默认为master
     * @param remote           远程仓库的别名
     * @param privateKeyPath   本地私钥文件的存放路径
     * @return
     */
    public static PullResult pullWithSSH(Git git, String remoteBranchName, String remote,
                                         String privateKeyPath) {
        return pullWithSSH(git, remoteBranchName, remote, privateKeyPath, null);
    }

    /**
     * 从远程仓库拉取最新内容至本地仓库,相当于执行git pull命令
     *
     * @param git              Git实例对象
     * @param remoteBranchName 远程分支名称,默认为master
     * @param remote           远程仓库的别名
     * @return
     */
    public static PullResult pullWithSSH(Git git, String remoteBranchName, String remote) {
        return pullWithSSH(git, remoteBranchName, remote, null, null);
    }

    /**
     * 从远程仓库拉取最新内容至本地仓库,相当于执行git pull命令
     *
     * @param git              Git实例对象
     * @param remoteBranchName 远程分支名称,默认为master
     * @return
     */
    public static PullResult pullWithSSH(Git git, String remoteBranchName) {
        return pullWithSSH(git, remoteBranchName, null, null, null);
    }

    /**
     * 从远程仓库拉取最新内容至本地仓库,相当于执行git pull命令
     *
     * @param git Git实例对象
     * @return
     */
    public static PullResult pullWithSSH(Git git) {
        return pullWithSSH(git, null, null, null, null);
    }

    /**
     * 从远程仓库拉取最新内容至本地仓库,相当于执行git pull命令
     *
     * @param git              Git实例对象
     * @param remoteBranchName 远程分支名称,默认为master
     * @param remote           远程仓库的别名
     * @param githubUserName   Github登录账号
     * @param githubPassword   Github登录密码
     * @param mergeStrategy    文件合并策略,默认为RECURSIVE
     * @return
     */
    public static PullResult pullWithHttp(Git git, String remoteBranchName, String remote,
                                          String githubUserName, String githubPassword,
                                          MergeStrategy mergeStrategy) {
        PullCommand pullCommand = git.pull()
                .setRemote((null == remote || "".equals(remote)) ? "origin" : remote)
                .setRemoteBranchName((null == remoteBranchName || "".equals(remoteBranchName)) ?
                        BRANCH_MASTER : remoteBranchName)
                .setStrategy(null == mergeStrategy ? MergeStrategy.RECURSIVE : mergeStrategy);

        if (null == githubUserName || "".equals(githubUserName)) {
            pullCommand = pullCommand.setCredentialsProvider(
                    new UsernamePasswordCredentialsProvider(githubUserName, githubPassword));
        }
        try {
            return pullCommand.call();
        } catch (GitAPIException e) {
            log.error("While pull from the remote repository with githubUserName[{}],we occur exception:\n{}",
                    githubUserName, e.getMessage());
            return null;
        }
    }

    /**
     * 从远程仓库拉取最新内容至本地仓库,相当于执行git pull命令
     *
     * @param git              Git实例对象
     * @param remoteBranchName 远程分支名称,默认为master
     * @param remote           远程仓库的别名
     * @param githubUserName   Github登录账号
     * @param githubPassword   Github登录密码
     * @return
     */
    public static PullResult pullWithHttp(Git git, String remoteBranchName, String remote,
                                          String githubUserName, String githubPassword) {
        return pullWithHttp(git, remoteBranchName, remote, githubUserName, githubPassword, null);
    }

    /**
     * 从远程仓库拉取最新内容至本地仓库,相当于执行git pull命令
     *
     * @param git              Git实例对象
     * @param remoteBranchName 远程分支名称,默认为master
     * @param githubUserName   Github登录账号
     * @param githubPassword   Github登录密码
     * @return
     */
    public static PullResult pullWithHttp(Git git, String remoteBranchName, String githubUserName, String githubPassword) {
        return pullWithHttp(git, remoteBranchName, null, githubUserName, githubPassword, null);
    }

    /**
     * 从远程仓库拉取最新内容至本地仓库,相当于执行git pull命令
     *
     * @param git            Git实例对象
     * @param githubUserName Github登录账号
     * @param githubPassword Github登录密码
     * @return
     */
    public static PullResult pullWithHttp(Git git, String githubUserName, String githubPassword) {
        return pullWithHttp(git, null, null, githubUserName, githubPassword, null);
    }

    /**
     * 将本地仓库的修改推送到Github远程仓库,相当于git push命令
     *
     * @param git            Git实例对象
     * @param branchName     分支名称
     * @param privateKeyPath 本地私钥文件的存放路径
     * @param thinPack       是否开启Git的数据包瘦身优化,默认为true即开启
     * @param force          强制将本地仓库内容推送到远程仓库进行覆盖,即使远程仓库的内容版本更新
     * @param pushAll        是否推送所有分支至远程仓库,默认至推送当前所在分支
     * @param atomic         是否开启原子推送
     * @param dryRun         开启dry-run模拟测试
     * @return
     */
    public static Iterable<PushResult> pushWithSSH(Git git, String branchName, String privateKeyPath, boolean thinPack,
                                                   boolean force, boolean pushAll, boolean atomic, boolean dryRun) {
        if (null == branchName || "".equals(branchName)) {
            branchName = BRANCH_MASTER;
        }
        RefSpec refSpec = new RefSpec(branchName + ":" + branchName);
        PushCommand pushCommand = git.push().setAtomic(atomic).setDryRun(dryRun)
                .setPushAll().setForce(force).setThin(thinPack)
                .setRefSpecs(refSpec);
        if (pushAll) {
            pushCommand = pushCommand.setPushAll();
        }
        SshSessionFactory sshSessionFactory = createSshSessionFactory(privateKeyPath);
        if (null != sshSessionFactory) {
            pushCommand = pushCommand.setTransportConfigCallback(new TransportConfigCallback() {
                @Override
                public void configure(Transport transport) {
                    SshTransport sshTransport = (SshTransport) transport;
                    sshTransport.setSshSessionFactory(sshSessionFactory);
                }
            });
        }
        try {
            return pushCommand.call();
        } catch (GitAPIException e) {
            log.error("While push[SSH] the commit to the remote repository(BranchName)[{}],we occur exception:\n{}",
                    branchName, e.getMessage());
            return null;
        }
    }

    /**
     * 将本地仓库的修改推送到Github远程仓库,相当于git push命令
     *
     * @param git            Git实例对象
     * @param branchName     分支名称
     * @param privateKeyPath 本地私钥文件的存放路径
     * @param thinPack       是否开启Git的数据包瘦身优化,默认为true即开启
     * @param force          强制将本地仓库内容推送到远程仓库进行覆盖,即使远程仓库的内容版本更新
     * @param pushAll        是否推送所有分支至远程仓库,默认只推送当前所在分支
     * @param atomic         是否开启原子推送
     * @return
     */
    public static Iterable<PushResult> pushWithSSH(Git git, String branchName, String privateKeyPath, boolean thinPack,
                                                   boolean force, boolean pushAll, boolean atomic) {
        return pushWithSSH(git, branchName, privateKeyPath, thinPack, force, pushAll, atomic, false);
    }

    /**
     * 将本地仓库的修改推送到Github远程仓库,相当于git push命令
     *
     * @param git            Git实例对象
     * @param branchName     分支名称
     * @param privateKeyPath 本地私钥文件的存放路径
     * @param thinPack       是否开启Git的数据包瘦身优化,默认为true即开启
     * @param force          强制将本地仓库内容推送到远程仓库进行覆盖,即使远程仓库的内容版本更新
     * @param pushAll        是否推送所有分支至远程仓库,默认只推送当前所在分支
     * @return
     */
    public static Iterable<PushResult> pushWithSSH(Git git, String branchName, String privateKeyPath, boolean thinPack,
                                                   boolean force, boolean pushAll) {
        return pushWithSSH(git, branchName, privateKeyPath, thinPack, force, pushAll, false, false);
    }

    /**
     * 将本地仓库的修改推送到Github远程仓库,相当于git push命令
     *
     * @param git            Git实例对象
     * @param branchName     分支名称
     * @param privateKeyPath 本地私钥文件的存放路径
     * @param thinPack       是否开启Git的数据包瘦身优化,默认为true即开启
     * @param force          强制将本地仓库内容推送到远程仓库进行覆盖,即使远程仓库的内容版本更新
     * @return
     */
    public static Iterable<PushResult> pushWithSSH(Git git, String branchName, String privateKeyPath, boolean thinPack,
                                                   boolean force) {
        return pushWithSSH(git, branchName, privateKeyPath, thinPack, force, false, false, false);
    }

    /**
     * 将本地仓库的修改推送到Github远程仓库,相当于git push命令
     *
     * @param git            Git实例对象
     * @param branchName     分支名称
     * @param privateKeyPath 本地私钥文件的存放路径
     * @param thinPack       是否开启Git的数据包瘦身优化,默认为true即开启
     * @return
     */
    public static Iterable<PushResult> pushWithSSH(Git git, String branchName, String privateKeyPath, boolean thinPack) {
        return pushWithSSH(git, branchName, privateKeyPath, thinPack, false, false, false, false);
    }

    /**
     * 将本地仓库的修改推送到Github远程仓库,相当于git push命令
     *
     * @param git            Git实例对象
     * @param branchName     分支名称
     * @param privateKeyPath 本地私钥文件的存放路径
     * @return
     */
    public static Iterable<PushResult> pushWithSSH(Git git, String branchName, String privateKeyPath) {
        return pushWithSSH(git, branchName, privateKeyPath, true, false, false, false, false);
    }

    /**
     * 将本地仓库的修改推送到Github远程仓库,相当于git push命令
     *
     * @param git        Git实例对象
     * @param branchName 分支名称
     * @return
     */
    public static Iterable<PushResult> pushWithSSH(Git git, String branchName) {
        return pushWithSSH(git, branchName, null, true, false, false, false, false);
    }

    /**
     * 将本地仓库的修改推送到Github远程仓库,相当于git push命令
     *
     * @param git Git实例对象
     * @return
     */
    public static Iterable<PushResult> pushWithSSH(Git git) {
        return pushWithSSH(git, null, null, true, false, false, false, false);
    }

    /**
     * 将本地仓库的修改推送到Github远程仓库,相当于git push命令
     *
     * @param git            Git实例对象
     * @param branchName     分支名称
     * @param githubUserName Github登录账号
     * @param githubPassword Github登录密码
     * @param thinPack       是否开启Git的数据包瘦身优化,默认为true即开启
     * @param force          强制将本地仓库内容推送到远程仓库进行覆盖,即使远程仓库的内容版本更新
     * @param pushAll        是否推送所有分支至远程仓库,默认至推送当前所在分支
     * @param atomic         是否开启原子推送
     * @param dryRun         开启dry-run模拟测试
     * @return
     */
    public static Iterable<PushResult> pushWithHttp(Git git, String branchName, String githubUserName, String githubPassword,
                                                    boolean thinPack, boolean force, boolean pushAll,
                                                    boolean atomic, boolean dryRun) {
        if (null == branchName || "".equals(branchName)) {
            branchName = BRANCH_MASTER;
        }
        RefSpec refSpec = new RefSpec(branchName + ":" + branchName);
        PushCommand pushCommand = git.push().setAtomic(atomic).setDryRun(dryRun)
                .setPushAll().setForce(force).setThin(thinPack)
                .setRefSpecs(refSpec);
        if (pushAll) {
            pushCommand = pushCommand.setPushAll();
        }
        if (null == githubUserName || "".equals(githubUserName)) {
            pushCommand = pushCommand.setCredentialsProvider(
                    new UsernamePasswordCredentialsProvider(githubUserName, githubPassword)
            );
        }
        try {
            return pushCommand.call();
        } catch (GitAPIException e) {
            log.error("While push[Http] the commit to the remote repository with BranchName[{}] and GithubUserName[{}],we occur exception:\n{}",
                    branchName, githubUserName, e.getMessage());
            return null;
        }
    }

    /**
     * 将本地仓库的修改推送到Github远程仓库,相当于git push命令
     *
     * @param git            Git实例对象
     * @param branchName     分支名称
     * @param githubUserName Github登录账号
     * @param githubPassword Github登录密码
     * @param thinPack       是否开启Git的数据包瘦身优化,默认为true即开启
     * @param force          强制将本地仓库内容推送到远程仓库进行覆盖,即使远程仓库的内容版本更新
     * @param pushAll        是否推送所有分支至远程仓库,默认至推送当前所在分支
     * @param atomic         是否开启原子推送
     * @return
     */
    public static Iterable<PushResult> pushWithHttp(Git git, String branchName, String githubUserName, String githubPassword,
                                                    boolean thinPack, boolean force, boolean pushAll,
                                                    boolean atomic) {
        return pushWithHttp(git, branchName, githubUserName, githubPassword, thinPack, force, pushAll, atomic, false);
    }

    /**
     * 将本地仓库的修改推送到Github远程仓库,相当于git push命令
     *
     * @param git            Git实例对象
     * @param branchName     分支名称
     * @param githubUserName Github登录账号
     * @param githubPassword Github登录密码
     * @param thinPack       是否开启Git的数据包瘦身优化,默认为true即开启
     * @param force          强制将本地仓库内容推送到远程仓库进行覆盖,即使远程仓库的内容版本更新
     * @param pushAll        是否推送所有分支至远程仓库,默认至推送当前所在分支
     * @return
     */
    public static Iterable<PushResult> pushWithHttp(Git git, String branchName, String githubUserName, String githubPassword,
                                                    boolean thinPack, boolean force, boolean pushAll) {
        return pushWithHttp(git, branchName, githubUserName, githubPassword, thinPack, force, pushAll, false, false);
    }

    /**
     * 将本地仓库的修改推送到Github远程仓库,相当于git push命令
     *
     * @param git            Git实例对象
     * @param branchName     分支名称
     * @param githubUserName Github登录账号
     * @param githubPassword Github登录密码
     * @param thinPack       是否开启Git的数据包瘦身优化,默认为true即开启
     * @param force          强制将本地仓库内容推送到远程仓库进行覆盖,即使远程仓库的内容版本更新
     * @return
     */
    public static Iterable<PushResult> pushWithHttp(Git git, String branchName, String githubUserName, String githubPassword,
                                                    boolean thinPack, boolean force) {
        return pushWithHttp(git, branchName, githubUserName, githubPassword, thinPack, force, false, false, false);
    }

    /**
     * 将本地仓库的修改推送到Github远程仓库,相当于git push命令
     *
     * @param git            Git实例对象
     * @param branchName     分支名称
     * @param githubUserName Github登录账号
     * @param githubPassword Github登录密码
     * @param thinPack       是否开启Git的数据包瘦身优化,默认为true即开启
     * @return
     */
    public static Iterable<PushResult> pushWithHttp(Git git, String branchName, String githubUserName, String githubPassword,
                                                    boolean thinPack) {
        return pushWithHttp(git, branchName, githubUserName, githubPassword, thinPack, false, false, false, false);
    }

    /**
     * 将本地仓库的修改推送到Github远程仓库,相当于git push命令
     *
     * @param git            Git实例对象
     * @param branchName     分支名称
     * @param githubUserName Github登录账号
     * @param githubPassword Github登录密码
     * @return
     */
    public static Iterable<PushResult> pushWithHttp(Git git, String branchName, String githubUserName, String githubPassword) {
        return pushWithHttp(git, branchName, githubUserName, githubPassword, true, false, false, false, false);
    }

    /**
     * 将本地仓库的修改推送到Github远程仓库,相当于git push命令
     *
     * @param git            Git实例对象
     * @param githubUserName Github登录账号
     * @param githubPassword Github登录密码
     * @return
     */
    public static Iterable<PushResult> pushWithHttp(Git git, String githubUserName, String githubPassword) {
        return pushWithHttp(git, null, githubUserName, githubPassword, true, false, false, false, false);
    }

    /**
     * 关闭Git实例,释放文件句柄资源
     *
     * @param git
     */
    public static void closeGit(Git git) {
        if (null != git) {
            git.close();
        }
    }

    /**
     * 创建SshSessionFactory实例对象
     *
     * @param privateKey 本地私钥文件的存放地址
     * @return
     */
    private static SshSessionFactory createSshSessionFactory(String privateKey) {
        SshSessionFactory sshSessionFactory = sshSessionFactoryThreadLocal.get();
        if (null != sshSessionFactory) {
            return sshSessionFactory;
        }
        if (null == privateKey || "".equals(privateKey)) {
            privateKey = PRIVATE_KEY;
        }
        final String finalPrivateKey = privateKey;
        sshSessionFactory = new JschConfigSessionFactory() {
            @Override
            protected void configure(OpenSshConfig.Host host, Session session) {
                session.setConfig("StrictHostKeyChecking", "no");
            }

            @Override
            protected JSch getJSch(final OpenSshConfig.Host host, FS fs) throws JSchException {
                JSch jsch = super.getJSch(host, fs);
                jsch.removeAllIdentity();
                jsch.addIdentity(finalPrivateKey);
                return jsch;
            }
        };
        return sshSessionFactory;
    }

    /**
     * 创建SshSessionFactory实例对象
     *
     * @return
     */
    private static SshSessionFactory createSshSessionFactory() {
        return createSshSessionFactory(null);
    }

    /**
     * 将路径字符串里的\\转换成/
     *
     * @param path
     * @return
     */
    private static String fixedPathDelimiter(String path) {
        if (null == path || "".equals(path)) {
            return path;
        }
        path = path.replaceAll("\\\\", "/");
        if (!path.endsWith("/")) {
            path = path + "/";
        }
        return path;
    }

    /**Commit*/
    //git commit -m "Gabba Gabba Hey"
    //git.commit().setMessage( "Gabba Gabba Hey" ).call();

    //init local repository
    //Git git = Git.init().setDirectory( "/path/to/repo" ).call();

    //git add
    //DirCache index = git.add().addFilePattern( "readme.txt" ).call();

    //git remove
    //DirCache index = git.rm().addFilepattern( "readme.txt" ).call();


    //Clone
    /**
     * Git git = Git.cloneRepository()
     .setURI( "https://github.com/eclipse/jgit.git" )
     .setDirectory( "/path/to/repo" )
     .call();
     */

    //Git open local repository
    //Git git = Git.open( new F‌ile( "/path/to/repo/.git" ) );

    //is this a repository?
    /**
     * Repository repository = repositoryBuilder.build();
     if( repository.getRef( "HEAD" ) != null ) {
     }
     */

    //JGit Authentication
    //command.setCredentialsProvider( new UsernamePasswordCredentialsProvider( "username", "password" ) );

    //State of a Repository
    //Status status = git.status().call();

    //Iterate the commit log
    //Iterable<RevCommit> iterable = git.log().call();
    /**
     * Repository repository = git.getRepository()
     try( RevWalk revWalk = new RevWalk( repository ) ) {
     ObjectId commitId = repository.resolve( "refs/heads/your-branch-name" );
     revWalk.markStart( revWalk.parseCommit( commitId ) );
     for( RevCommit commit : revWalk ) {
     System.out.println( commit.getFullMessage );
     }
     }
     */

    //git push
    /**
     * Iterable<PushResult> iterable = git.push().call();
     PushResult pushResult = iterable.iterator().next();
     Status status
     = pushResult.getRemoteUpdate( "refs/heads/your-branch-name" ).getStatus();
     */

    //git fetch
    /**
     * FetchResult fetchResult = local.fetch().call();
     TrackingRefUpdate refUpdate
     = fetchResult.getTrackingRefUpdate( "refs/remotes/origin/your-branch-name" );
     Result result = refUpdate.getResult();
     */

    //
    //git ssh auth
    /**
     * SshSessionFactory sshSessionFactory = new JschConfigSessionFactory() { @Override protected void configure(OpenSshConfig.Host host, Session session) { session.setConfig("StrictHostKeyChecking", "no"); }
     @Override protected JSch getJSch(final OpenSshConfig.Host hc, FS fs) throws JSchException {
     JSch jsch = super.getJSch(hc, fs);
     jsch.removeAllIdentity();
     jsch.addIdentity( "/path/to/private/key" );
     return jsch;
     }};
     */

    /**
     * String userHome = System.getProperty("user.home");
     String privateKey = userHome + "/.ssh/id_rsa";
     * PullCommand pull = git.pull().setTransportConfigCallback(new TransportConfigCallback() {

    @Override public void configure(Transport transport) {
    SshTransport sshTransport = (SshTransport) transport;
    sshTransport.setSshSessionFactory(sshSessionFactory);
    }
    });

     */

    //list all branchs
    /**
     * Collection<Ref> remoteRefs = Git.lsRemoteRepository()
     .setHeads( true )
     .setRemote( "https://github.com/eclipse/jgit.git" )
     .call();
     */

    //list repository
    /**
     * Collection<Ref> remoteRefs = Git.lsRemoteRepository()
     .setHeads( true )
     .setRemote( "https://github.com/eclipse/jgit.git" )
     .call();
     */

    //git checkout as a branch
    /**
     * Ref ref = git.checkout().
     setCreateBranch(true).
     setName("branchName").
     setUpstreamMode(CreateBranchCommand.SetupUpstreamMode.TRACK).
     setStartPoint("origin/" + branchName).
     call();
     */

    //git checkout
    //git.checkout().setCreateBranch( true ).setName( "new-branch" ).setStartPoint( "<id-to-commit>" ).call();
}
