package com.yida.framework.blog.handler.output;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @Author Lanxiaowei
 * @Date 2018-01-18 23:42
 * @Description 复制Word内自包含的图片文件复制到指定目录的任务处理器的输出
 */
public class WordImageCopyHandlerOutput extends HandlerOutput {
    /**
     * 从Word文档内复制出来的图片文件列表,Key为Word文档解压目录下的images目录路径，Value为该目录下的所有图片路径
     */
    private Map<String, List<String>> imagesFilePath;

    private WordUnzipHandlerOutput wordUnzipHandlerOutput;

    public WordImageCopyHandlerOutput() {
        this.wordUnzipHandlerOutput = new WordUnzipHandlerOutput();
    }

    public Map<String, List<String>> getImagesFilePath() {
        return imagesFilePath;
    }

    public void setImagesFilePath(Map<String, List<String>> imagesFilePath) {
        this.imagesFilePath = imagesFilePath;
    }

    public WordUnzipHandlerOutput getWordUnzipHandlerOutput() {
        return wordUnzipHandlerOutput;
    }

    public void setWordUnzipHandlerOutput(WordUnzipHandlerOutput wordUnzipHandlerOutput) {
        this.wordUnzipHandlerOutput = wordUnzipHandlerOutput;
    }

    public List<String> getMarkdownParentPath() {
        if (null != imagesFilePath && imagesFilePath.size() > 0) {
            List<String> parentPathList = new ArrayList<String>();
            for (String path : imagesFilePath.keySet()) {
                parentPathList.add(path.replace(this.MD_IMAGE_BASEPATH, ""));
            }
            return parentPathList;
        }
        return null;
    }
}
