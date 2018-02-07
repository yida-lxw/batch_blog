package com.yida.framework.blog.handler;

import com.yida.framework.blog.handler.input.*;
import com.yida.framework.blog.handler.output.*;
import com.yida.framework.blog.utils.io.DocxFilenameFilter;
import com.yida.framework.blog.utils.io.ImageFilenameFilter;
import com.yida.framework.blog.utils.io.MarkdownFilenameFilter;
import com.yida.framework.blog.utils.io.ZipArchiverFileFilter;

/**
 * @Author Lanxiaowei
 * @Date 2018-02-07 19:15
 * @Description MarkdownFixHandler类的测试用例
 */
public class MarkdownFixHandlerTest {
    public static void main(String[] args) {
        String blogBasePath = "C:/myblog/";
        String blogSendDate = "20180207";
        String pandocPath = "F:/pandoc-2.1/";
        MarkdownFilenameFilter markdownFilenameFilter = new MarkdownFilenameFilter();
        ImageFilenameFilter imageFilenameFilter = new ImageFilenameFilter();
        ZipArchiverFileFilter zipArchiverFileFilter = new ZipArchiverFileFilter();
        DocxFilenameFilter docxFilenameFilter = new DocxFilenameFilter();

        WordFilterHandlerInput wordFilterHandlerInput = new WordFilterHandlerInput(blogBasePath, blogSendDate);
        WordFilterHandlerOutput wordFilterHandlerOutput = new WordFilterHandlerOutput();
        WordFilterHandler wordFilterHandler = new WordFilterHandler(docxFilenameFilter);
        wordFilterHandler.handle(wordFilterHandlerInput, wordFilterHandlerOutput);
        //打印结果
        System.out.println(wordFilterHandlerOutput);


        Word2MarkdownHandlerInput word2MarkdownHandlerInput = new Word2MarkdownHandlerInput();
        word2MarkdownHandlerInput.setPandocHome(pandocPath);
        word2MarkdownHandlerInput.setWordFilesName(wordFilterHandlerOutput.getWordFilesPath());
        Word2MarkdownHandlerOutput word2MarkdownHandlerOutput = new Word2MarkdownHandlerOutput();
        Word2MarkdownHandler word2MarkdownHandler = new Word2MarkdownHandler();
        word2MarkdownHandler.handle(word2MarkdownHandlerInput, word2MarkdownHandlerOutput);
        System.out.println(word2MarkdownHandlerOutput);


        WordUnzipHandlerInput wordUnzipHandlerInput = new WordUnzipHandlerInput();
        wordUnzipHandlerInput.setWordFilesName(wordFilterHandlerOutput.getWordFilesPath());
        WordUnzipHandlerOutput wordUnzipHandlerOutput = new WordUnzipHandlerOutput();
        WordUnzipHandler wordUnzipHandler = new WordUnzipHandler(zipArchiverFileFilter);
        wordUnzipHandler.handle(wordUnzipHandlerInput, wordUnzipHandlerOutput);
        System.out.println(wordUnzipHandlerOutput);


        WordImageCopyHandlerInput wordImageCopyHandlerInput = new WordImageCopyHandlerInput();
        wordImageCopyHandlerInput.setUnzipFilePaths(wordUnzipHandlerOutput.getUnzipFilePaths());
        WordImageCopyHandlerOutput wordImageCopyHandlerOutput = new WordImageCopyHandlerOutput();
        WordImageCopyHandler wordImageCopyHandler = new WordImageCopyHandler(imageFilenameFilter, markdownFilenameFilter);
        wordImageCopyHandler.handle(wordImageCopyHandlerInput, wordImageCopyHandlerOutput);
        System.out.println(wordImageCopyHandlerOutput);


        MarkdownMoveHandlerInput markdownMoveHandlerInput = new MarkdownMoveHandlerInput();
        MarkdownMoveHandlerOutput markdownMoveHandlerOutput = new MarkdownMoveHandlerOutput();
        MarkdownMoveHandler markdownMoveHandler = new MarkdownMoveHandler();
        markdownMoveHandler.handle(markdownMoveHandlerInput, markdownMoveHandlerOutput);
        System.out.println(markdownMoveHandlerOutput);


        MarkdownFixHandlerInput markdownFixHandlerInput = new MarkdownFixHandlerInput();
        MarkdownFixHandlerOutput markdownFixHandlerOutput = new MarkdownFixHandlerOutput();
        MarkdownFixHandler markdownFixHandler = new MarkdownFixHandler(markdownFilenameFilter);
        markdownFixHandler.handle(markdownFixHandlerInput, markdownFixHandlerOutput);
        System.out.println(markdownFixHandlerOutput);


    }
}
