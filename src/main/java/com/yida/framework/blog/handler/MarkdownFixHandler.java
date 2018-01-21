package com.yida.framework.blog.handler;

import com.yida.framework.blog.handler.input.MarkdownFixHandlerInput;
import com.yida.framework.blog.handler.output.MarkdownFixHandlerOutput;

import java.util.List;

/**
 * @Author Lanxiaowei
 * @Date 2018-01-21 18:56
 * @Description Markdown文件内容修复任务处理器
 */
public class MarkdownFixHandler implements Handler<MarkdownFixHandlerInput, MarkdownFixHandlerOutput> {
    @Override
    public void handle(MarkdownFixHandlerInput input, MarkdownFixHandlerOutput output) {
        List<String> markdownParentPath = input.getMarkdownParentPath();
        if (null == markdownParentPath || markdownParentPath.size() <= 0) {
            return;
        }

    }
}
