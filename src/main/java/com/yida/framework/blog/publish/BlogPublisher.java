package com.yida.framework.blog.publish;

/**
 * @Author Lanxiaowei
 * @Date 2018-02-12 10:43
 * @Description 博客发布接口
 */
public interface BlogPublisher<T extends BlogPublishParam> {
    //发布博客
    String publish(T blogPublishParam);
}
