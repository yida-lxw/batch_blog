package com.yida.framework.blog.handler;

import com.yida.framework.blog.handler.input.MarkdownFixHandlerInput;
import com.yida.framework.blog.handler.input.MarkdownMoveHandlerInput;
import com.yida.framework.blog.handler.input.Word2MarkdownHandlerInput;
import com.yida.framework.blog.handler.input.WordImageCopyHandlerInput;
import com.yida.framework.blog.handler.output.MarkdownFixHandlerOutput;
import com.yida.framework.blog.handler.output.MarkdownMoveHandlerOutput;
import com.yida.framework.blog.handler.output.Word2MarkdownHandlerOutput;
import com.yida.framework.blog.handler.output.WordImageCopyHandlerOutput;
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


        //NO
        /*WordFilterHandlerInput wordFilterHandlerInput = new WordFilterHandlerInput();
        WordFilterHandlerOutput wordFilterHandlerOutput = new WordFilterHandlerOutput();
        WordFilterHandler wordFilterHandler = new WordFilterHandler(docxFilenameFilter);
        wordFilterHandler.handle(wordFilterHandlerInput, wordFilterHandlerOutput);
        //打印结果
        System.out.println(wordFilterHandlerOutput);*/


        WordFilterHandler wordFilterHandler = new WordFilterHandler(docxFilenameFilter);

        //need WordFilterHandler
        Word2MarkdownHandlerInput word2MarkdownHandlerInput = new Word2MarkdownHandlerInput();
        Word2MarkdownHandlerOutput word2MarkdownHandlerOutput = new Word2MarkdownHandlerOutput();
        Word2MarkdownHandler word2MarkdownHandler = new Word2MarkdownHandler(wordFilterHandler);
        word2MarkdownHandler.handle(word2MarkdownHandlerInput, word2MarkdownHandlerOutput);
        System.out.println(word2MarkdownHandlerOutput);


        //need WordFilterHandler
        /*WordUnzipHandlerInput wordUnzipHandlerInput = new WordUnzipHandlerInput();
        WordUnzipHandlerOutput wordUnzipHandlerOutput = new WordUnzipHandlerOutput();
        WordUnzipHandler wordUnzipHandler = new WordUnzipHandler(wordFilterHandler,zipArchiverFileFilter);
        wordUnzipHandler.handle(wordUnzipHandlerInput, wordUnzipHandlerOutput);
        System.out.println(wordUnzipHandlerOutput);*/


        WordUnzipHandler wordUnzipHandler = new WordUnzipHandler(wordFilterHandler, zipArchiverFileFilter);

        //need WordUnzipHandler
        WordImageCopyHandlerInput wordImageCopyHandlerInput = new WordImageCopyHandlerInput();
        WordImageCopyHandlerOutput wordImageCopyHandlerOutput = new WordImageCopyHandlerOutput();
        WordImageCopyHandler wordImageCopyHandler = new WordImageCopyHandler(wordUnzipHandler,
                imageFilenameFilter, markdownFilenameFilter);
        wordImageCopyHandler.handle(wordImageCopyHandlerInput, wordImageCopyHandlerOutput);
        System.out.println(wordImageCopyHandlerOutput);


        //OK
        MarkdownMoveHandlerInput markdownMoveHandlerInput = new MarkdownMoveHandlerInput();
        MarkdownMoveHandlerOutput markdownMoveHandlerOutput = new MarkdownMoveHandlerOutput();
        MarkdownMoveHandler markdownMoveHandler = new MarkdownMoveHandler();
        markdownMoveHandler.handle(markdownMoveHandlerInput, markdownMoveHandlerOutput);
        System.out.println(markdownMoveHandlerOutput);


        //OK
        MarkdownFixHandlerInput markdownFixHandlerInput = new MarkdownFixHandlerInput();
        MarkdownFixHandlerOutput markdownFixHandlerOutput = new MarkdownFixHandlerOutput();
        MarkdownFixHandler markdownFixHandler = new MarkdownFixHandler(markdownFilenameFilter);
        markdownFixHandler.handle(markdownFixHandlerInput, markdownFixHandlerOutput);
        System.out.println(markdownFixHandlerOutput);


    }
}
