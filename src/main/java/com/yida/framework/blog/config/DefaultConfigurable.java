package com.yida.framework.blog.config;

/**
 * @Author Lanxiaowei
 * @Date 2018-01-16 23:48
 * @Description 系统可配置接口的默认实现, 需要获取系统配置信息的类，只需继承此类即可
 */
public abstract class DefaultConfigurable implements Configurable {
    protected BlogConfig config;

    public DefaultConfigurable() {
        initConfig();
    }

    /**
     * @return void
     * @throws
     * @Author: Lanxiaowei(736031305@qq.com)
     * @Title: initConfig
     * @Description: 初始化系统配置对象
     */
    @Override
    public BlogConfig initConfig() {
        this.config = BlogConfig.getInstance();
        return this.config;
    }

    public BlogConfig getConfig() {
        return config;
    }

    public void setConfig(BlogConfig config) {
        this.config = config;
    }
}
