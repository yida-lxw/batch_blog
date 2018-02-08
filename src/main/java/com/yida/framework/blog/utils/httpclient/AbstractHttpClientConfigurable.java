package com.yida.framework.blog.utils.httpclient;

/**
 * @Author Lanxiaowei
 * @Date 2018-02-09 02:16
 * @Description HttpClient的配置抽象类，其他想要获取HttpClient配置信息的类只须继承此类即可
 */
public abstract class AbstractHttpClientConfigurable implements HttpClientConfigurable {
    protected HttpClientConfig clientConfig;

    @Override
    public HttpClientConfig initConfig() {
        this.clientConfig = HttpClientConfig.getInstance();
        return this.clientConfig;
    }

    public HttpClientConfig getClientConfig() {
        return clientConfig;
    }

    public void setClientConfig(HttpClientConfig clientConfig) {
        this.clientConfig = clientConfig;
    }
}
