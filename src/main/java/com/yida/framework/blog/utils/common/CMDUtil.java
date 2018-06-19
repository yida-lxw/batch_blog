package com.yida.framework.blog.utils.common;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;

/**
 * @Author  Lanxiaowei
 * @Date    2018-01-14 20:41
 * @Description  Windows里的DOS命令执行工具类
 */
public class CMDUtil {
    private Logger log = LogManager.getLogger(CMDUtil.class.getName());
    private static final String WINDOWS_DEFAULT_CHARSET = "gb2312";
    public CMDUtil(long time) {
        this.timeout = time;
    }

    public CMDUtil() {
        this(0);
    }

    /**
     * 超时时间
     */
    private long timeout = -1;
    /**
     * true未超时 false 超时
     */
    private boolean no_timeout = true;

    /**
     * @param command
     *            需要执行的命令
     * @param cb
     *            回调
     */
    public boolean execute(final String command, final CallBack cb) {
        final CallBack[] callback = new CallBack[1];
        if (cb == null) {
            callback[0] = new DefaultCallback();
        } else {
            callback[0] = cb;
        }

        if (command == null) {
            // 默认回调
            callback[0].onFailed(new Exception("command is null,please check it out carefully and after try it again."));
            return false;
        }

        final Process[] process = new Process[1];

        //线程
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    process[0] = Runtime.getRuntime().exec(command);
                } catch (Exception e) {
                    // 创建失败
                    callback[0].onFailed(e);
                    return;
                }

                // 读取ping的错误流内容
                BufferedReader errReader = null;
                try {
                    errReader = new BufferedReader(new InputStreamReader(
                            process[0].getErrorStream(), WINDOWS_DEFAULT_CHARSET));
                } catch (UnsupportedEncodingException e) {
                    callback[0].ontherResponse(new Exception("读取错误流出现问题："
                            + e.getMessage()));
                }
                String errtemp;
                StringBuffer errSr = new StringBuffer();
                try {
                    while ((errtemp = errReader.readLine()) != null
                            && no_timeout) {
                        // System.out.println("错误流 " + errtemp);
                        append(errSr, errtemp);
                    }
                    // 将错误流信息回调
                    if (!errSr.toString().trim().equals("")) {
                        callback[0].erroutResponse(errSr.toString());
                    }

                } catch (IOException e) {
                    callback[0].ontherResponse(new Exception("读取错误流出现问题："
                            + e.getMessage()));
                } finally {
                    if (errSr != null) {
                        errSr = null;
                    }
                    if (errReader != null) {
                        try {
                            errReader.close();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }

                BufferedReader inReader = null;
                try {
                    inReader = new BufferedReader(new InputStreamReader(
                            process[0].getInputStream(), WINDOWS_DEFAULT_CHARSET));
                } catch (UnsupportedEncodingException e) {
                    callback[0].ontherResponse(new Exception("读取输入流出现问题："
                            + e.getMessage()));
                }
                String intemp;
                StringBuffer inSr = new StringBuffer();
                try {
                    while ((intemp = inReader.readLine()) != null && no_timeout) {
                        append(inSr, intemp);
                    }

                    // 将输入流信息回调
                    if (!inSr.toString().trim().equals("")) {
                        callback[0].stdoutResponse(inSr.toString());
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                    callback[0].ontherResponse(new Exception("读取输入流出现问题："
                            + e.getMessage()));
                } finally {
                    if (inSr != null) {
                        inSr = null;
                    }
                    if (inReader != null) {
                        try {
                            inReader.close();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }

                // 读取ping的输入流内容
                // 等待
                int status = -1;
                try {
                    status = process[0].waitFor();
                    if (status == 0) {

                        callback[0].onSuccess();

                    } else {
                        callback[0]
                                .onFailed(new Exception("执行失败 返回码 "+status));
                    }
                } catch (Exception e) {
                    // 超时 -->> Thread.interrupted() -->> wautfor抛出异常
                    // callback[0].onFailed(e);
                    callback[0].onTimeout();
                    no_timeout = false;
                    e.printStackTrace();
                }
                if (process[0] != null) {
                    process[0].destroy();
                }
            }
        });
        thread.start();
        if (timeout > 0) {
            try {
                thread.join(timeout); // 超时的时间
                if (thread.isAlive()) {// 没有执行完，超时
                    thread.interrupt();
                } else {
                    return true;
                }
                return false;
            } catch (InterruptedException e) {
                log.error("invoke the dos command [" + command + "] occured exception:\n" + e.getMessage());
                return false;
            }
        } else {
            return true;
        }
    }

    /**
     * @param command 需要执行的命令
     */
    public boolean execute(final String command) {
        return execute(command, null);
    }

    // 给字符换行
    private void append(StringBuffer stringBuffer, String text) {
        if (stringBuffer != null) {
            if (!text.trim().equals("")) {
                stringBuffer.append(text + "\n");
            }
        }
    }

    /**
     * 回调
     */
    interface CallBack {
        // 执行完成 成功
        void onSuccess();

        // 执行完成 失败
        void onFailed(Exception e);

        /*
         * 超时
         */
        void onTimeout();

        /*
         * 返回标准输出流
         */
        void stdoutResponse(String stdoutString);

        /*
         * 返回标准错误流
         */
        void erroutResponse(String erroutString);

        /*
         * 返回其他异常
         */
        void ontherResponse(Exception e);
    }

    private class DefaultCallback implements CallBack {

        @Override
        public void onSuccess() {
            System.out.println("default callback onSuccess ");
        }

        @Override
        public void onFailed(Exception e) {
            System.out.println("default callback onFailed " + e.getMessage());
        }

        @Override
        public void onTimeout() {
            System.out.println("default callback onTimeout ");
        }

        @Override
        public void stdoutResponse(String t) {
            System.out.println("default callback stdoutResponse " + t);
        }

        @Override
        public void erroutResponse(String t) {
            System.out.println("default callback erroutResponse " + t);
        }

        @Override
        public void ontherResponse(Exception e) {
            System.out.println("default callback ontherResponse "
                    + e.getMessage());
        }

    }
}
