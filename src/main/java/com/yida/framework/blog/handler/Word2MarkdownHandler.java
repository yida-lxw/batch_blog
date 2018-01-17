package com.yida.framework.blog.handler;

import com.yida.framework.blog.handler.input.Word2MarkdownHandlerInput;
import com.yida.framework.blog.handler.output.Word2MarkdownHandlerOutput;
import com.yida.framework.blog.utils.common.CMDUtil;

import java.util.List;

/**
 * @Author Lanxiaowei
 * @Date 2018-01-15 22:42
 * @Description docx文件转换成Markdown文件
 */
public class Word2MarkdownHandler implements Handler<Word2MarkdownHandlerInput, Word2MarkdownHandlerOutput> {

    @Override
    public void handle(Word2MarkdownHandlerInput input, Word2MarkdownHandlerOutput output) {
        String pandocHome = input.getPandocHome();
        List<String> wordFilesName = input.getWordFilesName();
        //如果没有Word文件需要处理
        if (null == wordFilesName || wordFilesName.size() <= 0) {
            output.setSuccessful(false);
            return;
        }

        String command = null;
        CMDUtil cmdUtil = new CMDUtil(3000);
        boolean invokeResult = false;
        int successCount = 0;
        //遍历处理每个Word文档(todo:后续再考虑采用多线程去处理)
        for (String wordFileName : wordFilesName) {
            command = buildCommand(pandocHome, wordFileName);
            invokeResult = cmdUtil.execute(command);
            if (invokeResult) {
                successCount++;
                if (!output.isSuccessful()) {
                    output.setSuccessful(invokeResult);
                }
            }
        }
        output.setSuccessfulCount(successCount);
    }

    /**
     * 构建转换命令
     *
     * @param pandocHome
     * @param wordFileName
     * @return
     */
    private String buildCommand(String pandocHome, String wordFileName) {
        if (wordFileName.endsWith(".DOCX")) {
            wordFileName = wordFileName.replace(".DOCX", ".docx");
        }
        String mdFileName = wordFileName.replace(".docx", ".md");
        return String.format("cmd /c %s && pandoc -o %s %s", "王力", pandocHome, mdFileName, wordFileName);
    }
}
