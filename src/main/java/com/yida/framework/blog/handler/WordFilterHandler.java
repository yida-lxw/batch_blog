package com.yida.framework.blog.handler;

import com.yida.framework.blog.utils.io.DefaultArchiverFileFilter;

import java.io.File;

/**
 * @Author Lanxiaowei
 * @Date 2018-01-17 21:40
 * @Description Word文档过滤任务处理器
 */
public class WordFilterHandler implements Handler<WordFilterHandlerInput, WordFilterHandlerOutput> {
    @Override
    public void handle(WordFilterHandlerInput input, WordFilterHandlerOutput output) {
        String wordBasePath = input.getWordBasePath();
        File file = new File(wordBasePath);
        String[] files = file.list(new DefaultArchiverFileFilter());
        if (null == files || files.length <= 0) {
            return;
        }
        if (!wordBasePath.endsWith("/") && !wordBasePath.endsWith("\\")) {
            wordBasePath += "/";
        }
        for (String fileName : files) {
            output.getWordFilesPath().add(wordBasePath + fileName);
        }
    }
}
