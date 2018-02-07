package com.yida.framework.blog.handler;

import com.yida.framework.blog.handler.input.WordImageCopyHandlerInput;
import com.yida.framework.blog.handler.output.WordImageCopyHandlerOutput;
import com.yida.framework.blog.utils.io.FileUtil;
import com.yida.framework.blog.utils.io.ImageFilenameFilter;
import com.yida.framework.blog.utils.io.MarkdownFilenameFilter;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @Author Lanxiaowei
 * @Date 2018-01-18 23:44
 * @Description 复制Word内自包含的图片文件到指定的images目录的任务处理器
 */
public class WordImageCopyHandler implements Handler<WordImageCopyHandlerInput, WordImageCopyHandlerOutput> {
    private ImageFilenameFilter imageFilenameFilter;
    private MarkdownFilenameFilter markdownFilenameFilter;

    public WordImageCopyHandler() {
        this.imageFilenameFilter = new ImageFilenameFilter();
        this.markdownFilenameFilter = new MarkdownFilenameFilter();
    }

    @Override
    public void handle(WordImageCopyHandlerInput input, WordImageCopyHandlerOutput output) {
        List<String> unzipFilePaths = input.getUnzipFilePaths();
        if (null != unzipFilePaths && unzipFilePaths.size() > 0) {
            File file = null;
            Map<String, List<String>> imagesMap = new HashMap<String, List<String>>();
            List<String> imagesPerMarkdown = null;
            String actualImagePath = null;
            String imagesNewPath = null;
            String[] images = null;
            for (String unzipFilePath : unzipFilePaths) {
                file = new File(unzipFilePath);
                if (!file.exists() || !file.isDirectory()) {
                    continue;
                }
                images = file.list(this.imageFilenameFilter);
                if (!unzipFilePath.endsWith("/")) {
                    if (!unzipFilePath.endsWith("\\")) {
                        unzipFilePath += "/";
                    }
                }

                if (null != images && images.length > 0) {
                    imagesPerMarkdown = new ArrayList<String>();
                    //图片实际需要复制到的新路径
                    imagesNewPath = unzipFilePath + output.MD_IMAGE_BASEPATH;
                    //解压后图片的实际路径
                    actualImagePath = unzipFilePath + input.WORD_IMAGE_PATH;
                    for (String imageFileName : images) {
                        imagesPerMarkdown.add(imagesNewPath + imageFileName);
                    }
                    //开始图片复制操作
                    FileUtil.copyDirectory(actualImagePath, imagesNewPath, this.imageFilenameFilter);
                    imagesMap.put(imagesNewPath, imagesPerMarkdown);
                } else {
                    //若找不到图片
                    imagesMap.put(imagesNewPath, null);
                }

                //开始删除其他文件(images目录除外)
                FileUtil.deleteDirs(unzipFilePath + "_rels");
                FileUtil.deleteDirs(unzipFilePath + "docProps");
                FileUtil.deleteDirs(unzipFilePath + "word");
                FileUtil.deleteFileOrDirectory(unzipFilePath + "[Content_Types].xml");
            }
            output.setImagesFilePath(imagesMap);
        }
    }
}
