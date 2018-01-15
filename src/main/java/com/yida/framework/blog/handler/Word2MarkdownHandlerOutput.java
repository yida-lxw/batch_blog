package com.yida.framework.blog.handler;

/**
 * @Author Lanxiaowei
 * @Date 2018-01-15 23:10
 * @Description Docx文件转Markdown的任务输出结果
 */
public class Word2MarkdownHandlerOutput extends HandlerOutput {
    /**
     * 生成的Markdown文件存储绝对路径
     */
    protected String markdownFilePath;
    /**
     * 生成的Markdown文件的名称,默认与Word文档的名称保持一致
     */
    protected String markdownFileName;
}
