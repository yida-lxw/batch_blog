package com.yida.framework.blog.handler.output;

import java.util.List;

/**
 * @Author Lanxiaowei
 * @Date 2018-01-18 00:31
 * @Description Word文档解压任务处理器的输出
 * 先将Word文档直接解压,然后将内部的图片复制到当前同级的images目录下,
 * 最后输出被复制的所有图片文件的完整路径
 */
public class WordUnzipHandlerOutput extends HandlerOutput {
    /**
     * Word文档中包含的图片列表
     */
    private List<String> imagesFilePath;

    /**
     * word文档解压是否成功
     */
    private boolean successful;
}
