package com.yida.framework.blog.utils.github;

import com.jcraft.jsch.JSch;
import com.jcraft.jsch.JSchException;
import com.jcraft.jsch.Session;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.api.PullCommand;
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
    public static final String USER_HOME = System.getProperty("user.home");
    private static Logger log = LogManager.getLogger(GithubUtil.class.getName());
    String localRepoPath;
    String remoteRepoPath;
    String localCodeDirectory;

    public static void main(String[] args) throws IOException {
        String localRepositoryPath = "G:/git4test/blog";
        String remoteRepoUrl = "git@github.com:yida-lxw/batch_blog.git";

        Git git = initLocalRepo(localRepositoryPath);


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

    public static void cloneRepositoryWithSSHAuth(Git git) {

        SshSessionFactory sshSessionFactory = new JschConfigSessionFactory() {
            @Override
            protected void configure(OpenSshConfig.Host host, Session session) {
                session.setConfig("StrictHostKeyChecking", "no");
            }

            @Override
            protected JSch getJSch(final OpenSshConfig.Host hc, FS fs) throws JSchException {
                JSch jsch = super.getJSch(hc, fs);
                jsch.removeAllIdentity();
                jsch.addIdentity("/path/to/private/key");
                return jsch;
            }
        };

        String privateKey = USER_HOME + "/.ssh/id_rsa";
        PullCommand pull = git.pull().setTransportConfigCallback(new TransportConfigCallback() {

            @Override

            public void configure(Transport transport) {
                SshTransport sshTransport = (SshTransport) transport;
                sshTransport.setSshSessionFactory(sshSessionFactory);
            }
        });
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
