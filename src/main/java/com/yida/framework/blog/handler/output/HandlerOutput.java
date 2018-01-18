package com.yida.framework.blog.handler.output;

/**
 * @Author Lanxiaowei
 * @Date 2018-01-15 23:04
 * @Description 任务处理器的输出结果
 */
public abstract class HandlerOutput {
    /**
     * 当前博客内的图片存放的相对路径即在当前Markdown文件同级的images目录下
     */
    public String MD_IMAGE_BASEPATH = "images/";

    /**
     * 上一步任务的处理是否成功,可能会需要根据上一步任务的执行结果来决定是否需要执行当前任务
     */
    protected boolean previousResult;

    /**
     * 是否为任务处理链中的第一个任务
     */
    protected boolean firstHandler;

    /**
     * 是否为任务处理链中的最后一个任务
     */
    protected boolean lastHandler;

    public boolean isPreviousResult() {
        return previousResult;
    }

    protected void setPreviousResult(boolean previousResult) {
        this.previousResult = previousResult;
    }

    public boolean isFirstHandler() {
        return firstHandler;
    }

    public void setFirstHandler(boolean firstHandler) {
        this.firstHandler = firstHandler;
    }

    public boolean isLastHandler() {
        return lastHandler;
    }

    public void setLastHandler(boolean lastHandler) {
        this.lastHandler = lastHandler;
    }
}
