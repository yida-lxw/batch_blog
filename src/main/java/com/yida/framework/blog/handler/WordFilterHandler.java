package com.yida.framework.blog.handler;

import com.yida.framework.blog.handler.input.WordFilterHandlerInput;
import com.yida.framework.blog.handler.output.WordFilterHandlerOutput;
import com.yida.framework.blog.utils.common.StringUtil;
import com.yida.framework.blog.utils.io.DocxFilenameFilter;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * @Author Lanxiaowei
 * @Date 2018-01-17 21:40
 * @Description Word文档过滤任务处理器
 */
public class WordFilterHandler implements Handler<WordFilterHandlerInput, WordFilterHandlerOutput> {
    private DocxFilenameFilter docxFilenameFilter;

    public WordFilterHandler(DocxFilenameFilter docxFilenameFilter) {
        this.docxFilenameFilter = docxFilenameFilter;
    }

    @Override
    public void handle(WordFilterHandlerInput input, WordFilterHandlerOutput output) {
        String wordBasePath = input.getWordBasePath();
        if (StringUtil.isNotEmpty(wordBasePath)) {
            if (!wordBasePath.endsWith("/") && !wordBasePath.endsWith("\\")) {
                wordBasePath += "/";
            }
        }

        List<String> blogSendDates = input.getBlogSendDates();
        if (null == blogSendDates || blogSendDates.size() <= 0) {
            String blogSendDate = input.getBlogSendDate();
            if (null == blogSendDate || "".equals(blogSendDate)) {
                output.setSuccessful(false);
                return;
            }
            blogSendDates = new ArrayList<String>();
            blogSendDates.add(blogSendDate);
        }
        File file = null;
        String wordPath = null;

        for (String blogSendDate : blogSendDates) {
            wordPath = wordBasePath + blogSendDate;
            file = new File(wordPath);
            String[] files = file.list(this.docxFilenameFilter);
            if (null == files || files.length <= 0) {
                continue;
            }
            for (String fileName : files) {
                output.getWordFilesPath().add(wordPath + "/" + fileName);
            }
        }
        output.setSuccessful(output.getWordFilesPath().size() > 0);
    }
}
