package com.yida.framework.blog.publish.hexo;

import com.yida.framework.blog.publish.BlogPublisher;

import java.io.IOException;

/**
 * @Author Lanxiaowei
 * @Date 2018-06-19 14:40
 * @Description Hexo博客发布接口实现
 */
public class HexoBlogPublisher implements BlogPublisher<HexoBlogPublisherParam> {

    @Override
    public String publish(HexoBlogPublisherParam blogPublishParam) {
        System.out.println("Enter HexoBlogPublisher...");
        String hexoHome = blogPublishParam.getHexoHome();
        String command = buildCommand(hexoHome);

        try {
            Runtime.getRuntime().exec(command);
        } catch (IOException e) {
            e.printStackTrace();
        }
        System.out.println("Exit HexoBlogPublisher...");
        return null;
    }

    /**
     * 构建Hexo部署命令
     *
     * @param hexoHome Hexo的安装根目录
     * @return
     */
    private String buildCommand(String hexoHome) {
        return String.format("cmd.exe /k cd /d %s && start /b hexo clean && start /b hexo d -g", hexoHome);
    }
}
