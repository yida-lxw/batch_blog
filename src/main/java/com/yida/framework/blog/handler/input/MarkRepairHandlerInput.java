package com.yida.framework.blog.handler.input;

/**
 * @Author Lanxiaowei
 * @Date 2018-01-18 00:21
 * @Description Markdown文件内部图片路径修复任务处理器的输入
 */
public class MarkRepairHandlerInput extends HandlerInput {
    /**
     * Word文档的存储根目录(因为默认Markdown就是存放在与Word同级目录下)
     */
    private String wordBasePath;
    /**
     * 博客发送日期
     */
    private String blogSendDate;
}
