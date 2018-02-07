package com.yida.framework.blog.handler;

import com.yida.framework.blog.handler.input.WordFilterHandlerInput;
import com.yida.framework.blog.handler.output.WordFilterHandlerOutput;
import com.yida.framework.blog.utils.io.DocxFilenameFilter;
import com.yida.framework.blog.utils.io.ImageFilenameFilter;
import com.yida.framework.blog.utils.io.MarkdownFilenameFilter;
import com.yida.framework.blog.utils.io.ZipArchiverFileFilter;

/**
 * @Author Lanxiaowei
 * @Date 2018-02-07 16:07
 * @Description WordFilterHandler的测试用例
 */
public class WordFilterHandlerTest {
    public static void main(String[] args) {
        String blogBasePath = "C:/myblog/";
        String blogSendDate = "20180207";
        String pandocPath = "F:/pandoc-2.1/";
        MarkdownFilenameFilter markdownFilenameFilter = new MarkdownFilenameFilter();
        ImageFilenameFilter imageFilenameFilter = new ImageFilenameFilter();
        ZipArchiverFileFilter zipArchiverFileFilter = new ZipArchiverFileFilter();
        DocxFilenameFilter docxFilenameFilter = new DocxFilenameFilter();

        WordFilterHandlerInput wordFilterHandlerInput = new WordFilterHandlerInput();
        WordFilterHandlerOutput wordFilterHandlerOutput = new WordFilterHandlerOutput();
        WordFilterHandler wordFilterHandler = new WordFilterHandler(docxFilenameFilter);
        wordFilterHandler.handle(wordFilterHandlerInput, wordFilterHandlerOutput);
        //打印结果
        System.out.println(wordFilterHandlerOutput);
    }
}
