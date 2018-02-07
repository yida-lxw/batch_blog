package com.yida.framework.blog.handler;

import com.yida.framework.blog.handler.input.WordUnzipHandlerInput;
import com.yida.framework.blog.handler.output.WordUnzipHandlerOutput;
import com.yida.framework.blog.utils.io.FileUtil;
import com.yida.framework.blog.utils.io.ZipArchiverFileFilter;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * @Author Lanxiaowei
 * @Date 2018-01-18 23:04
 * @Description Word文档解压任务处理器
 */
public class WordUnzipHandler implements Handler<WordUnzipHandlerInput, WordUnzipHandlerOutput> {
    private ZipArchiverFileFilter zipArchiverFileFilter;
    private WordFilterHandler wordFilterHandler;

    public WordUnzipHandler(ZipArchiverFileFilter zipArchiverFileFilter) {
        this.zipArchiverFileFilter = zipArchiverFileFilter;
    }

    public WordUnzipHandler(WordFilterHandler wordFilterHandler, ZipArchiverFileFilter zipArchiverFileFilter) {
        this.wordFilterHandler = wordFilterHandler;
        this.zipArchiverFileFilter = zipArchiverFileFilter;
    }

    @Override
    public void handle(WordUnzipHandlerInput input, WordUnzipHandlerOutput output) {
        this.wordFilterHandler.handle(input.getWordFilterHandlerInput(), output.getWordFilterHandlerOutput());
        input.setWordFilesName(output.getWordFilterHandlerOutput().getWordFilesPath());
        List<String> wordFilesName = input.getWordFilesName();
        if (null != wordFilesName && wordFilesName.size() > 0) {
            String wordFileName = null;
            boolean result = false;
            String unzipPath = null;
            List<String> unzipFilePaths = new ArrayList<String>();
            for (String wordFilePath : wordFilesName) {
                wordFileName = FileUtil.getPureFileName(wordFilePath);
                unzipPath = wordFilePath.substring(0,
                        (-1 == wordFilePath.lastIndexOf("/")) ?
                                wordFilePath.lastIndexOf("\\") + 1 : (wordFilePath.lastIndexOf("/") + 1)) +
                        wordFileName;
                result = zipArchiverFileFilter.doUnArchiver(new File(wordFilePath), unzipPath);
                if (result) {
                    unzipFilePaths.add(unzipPath);
                }
                if (!output.isSuccessful() && result) {
                    output.setSuccessful(result);
                }
            }
            output.setUnzipFilePaths(unzipFilePaths);
        }
    }
}
