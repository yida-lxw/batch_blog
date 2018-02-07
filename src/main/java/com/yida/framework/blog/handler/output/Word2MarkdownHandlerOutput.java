package com.yida.framework.blog.handler.output;

import java.util.ArrayList;
import java.util.List;

/**
 * @Author Lanxiaowei
 * @Date 2018-01-15 23:10
 * @Description Docx文件转Markdown的任务输出结果
 */
public class Word2MarkdownHandlerOutput extends HandlerOutput {
    /**
     * 生成的Markdown文件完整路径(生成的Markdown文件的名称,默认与Word文档的名称保持一致)
     */
    protected List<String> markdownFilesPath;
    /**
     * 是否生成成功(只要有一个生成成功,就视为生成成功)
     */
    protected boolean successful;

    /**
     * 文件转换成功的个数
     */
    private int successfulCount;

    private WordFilterHandlerOutput wordFilterHandlerOutput;

    public Word2MarkdownHandlerOutput() {
        this.wordFilterHandlerOutput = new WordFilterHandlerOutput();
    }

    public List<String> getMarkdownFilesPath() {
        if (null == markdownFilesPath || markdownFilesPath.size() <= 0) {
            markdownFilesPath = new ArrayList<String>(20);
        }
        return markdownFilesPath;
    }

    public void setMarkdownFilesPath(List<String> markdownFilesPath) {
        this.markdownFilesPath = markdownFilesPath;
    }

    public boolean isSuccessful() {
        return successful;
    }

    public void setSuccessful(boolean successful) {
        this.successful = successful;
    }

    public int getSuccessfulCount() {
        return successfulCount;
    }

    public void setSuccessfulCount(int successfulCount) {
        this.successfulCount = successfulCount;
    }

    public WordFilterHandlerOutput getWordFilterHandlerOutput() {
        return wordFilterHandlerOutput;
    }

    public void setWordFilterHandlerOutput(WordFilterHandlerOutput wordFilterHandlerOutput) {
        this.wordFilterHandlerOutput = wordFilterHandlerOutput;
    }
}
