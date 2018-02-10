package com.yida.framework.blog.utils.github;

import com.jcraft.jsch.JSch;
import com.jcraft.jsch.JSchException;
import com.jcraft.jsch.Session;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.eclipse.jgit.api.CloneCommand;
import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.api.TransportConfigCallback;
import org.eclipse.jgit.api.errors.GitAPIException;
import org.eclipse.jgit.lib.Repository;
import org.eclipse.jgit.storage.file.FileRepositoryBuilder;
import org.eclipse.jgit.transport.*;
import org.eclipse.jgit.util.FS;

import java.io.File;
import java.io.IOException;

/**
 * @Author Lanxiaowei
 * @Date 2018-02-10 13:23
 * @Description Github操作工具类, 基于JGit封装
 */
public class GithubUtil {
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


        //git clone vi http url
        //Git git = cloneRepositoryWithHttpAuth(remoteRepoHttpUrl, localRepositoryPath, githubUserName, githubPassword);
        //System.out.println("git clone via http:" + git);

        //git clone vi ssh url
        Git git = cloneRepositoryWithSSHAuth(remoteRepoSSHUrl, localRepositoryPath);
        System.out.println("git clone via ssh:" + git);

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
                cloneCommand = cloneCommand.setCredentialsProvider(new UsernamePasswordCredentialsProvider(githubUserName, githubPassword));
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

    //git checkout as a branch
    /**
     * Ref ref = git.checkout().
     setCreateBranch(true).
     setName("branchName").
     setUpstreamMode(CreateBranchCommand.SetupUpstreamMode.TRACK).
     setStartPoint("origin/" + branchName).
     call();
     */


}
