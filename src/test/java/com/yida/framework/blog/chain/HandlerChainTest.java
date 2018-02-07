package com.yida.framework.blog.chain;

import com.yida.framework.blog.handler.*;
import com.yida.framework.blog.handler.input.HandlerChainInput;
import com.yida.framework.blog.handler.output.HandlerChainOutput;
import com.yida.framework.blog.utils.io.DocxFilenameFilter;
import com.yida.framework.blog.utils.io.ImageFilenameFilter;
import com.yida.framework.blog.utils.io.MarkdownFilenameFilter;
import com.yida.framework.blog.utils.io.ZipArchiverFileFilter;

import java.util.ArrayList;
import java.util.List;

/**
 * @Author Lanxiaowei
 * @Date 2018-02-08 01:52
 * @Description HandlerChain类的测试用例
 */
public class HandlerChainTest {
    public static void main(String[] args) {
        MarkdownFilenameFilter markdownFilenameFilter = new MarkdownFilenameFilter();
        ImageFilenameFilter imageFilenameFilter = new ImageFilenameFilter();
        ZipArchiverFileFilter zipArchiverFileFilter = new ZipArchiverFileFilter();
        DocxFilenameFilter docxFilenameFilter = new DocxFilenameFilter();

        WordFilterHandler wordFilterHandler = new WordFilterHandler(docxFilenameFilter);
        WordUnzipHandler wordUnzipHandler = new WordUnzipHandler(wordFilterHandler, zipArchiverFileFilter);

        Word2MarkdownHandler word2MarkdownHandler = new Word2MarkdownHandler(wordFilterHandler);
        WordImageCopyHandler wordImageCopyHandler = new WordImageCopyHandler(wordUnzipHandler,
                imageFilenameFilter, markdownFilenameFilter);
        MarkdownMoveHandler markdownMoveHandler = new MarkdownMoveHandler();
        MarkdownFixHandler markdownFixHandler = new MarkdownFixHandler(markdownFilenameFilter);

        //add handlers to List<Handler>
        List<Handler> handlers = new ArrayList<Handler>();
        handlers.add(word2MarkdownHandler);
        handlers.add(wordImageCopyHandler);
        handlers.add(markdownMoveHandler);
        handlers.add(markdownFixHandler);

        //Create HandlerChain instance
        HandlerChain handlerChain = new HandlerChain();
        handlerChain.addHandlers(handlers);
        handlerChain.handle(new HandlerChainInput(), new HandlerChainOutput());
    }
}
