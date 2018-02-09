package com.yida.framework.blog.utils.httpclient.factory;

import org.apache.http.impl.client.CloseableHttpClient;

/**
 * @Author Lanxiaowei
 * @Date 2018-02-09 11:43
 * @Description HttpClient实例的工厂类, 用于创建HttpClient类的单例对象,
 * 因为HttpClient类内部已经包含了Http Connection Pool,
 * 故需要设计为单例模式
 */
public class HttpClientFactory {
    private CloseableHttpClient httpClient;

    private HttpClientFactory() {
        initialize();
    }

    public static final HttpClientFactory getInstance() {
        return HttpClientFactory.SingletonHolder.INSTANCE;
    }

    public void initialize() {

    }

    private static class SingletonHolder {
        private static final HttpClientFactory INSTANCE = new HttpClientFactory();
    }
}
