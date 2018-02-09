package com.yida.framework.blog.httpclient;

import com.yida.framework.blog.utils.httpclient.config.HttpClientConfigLoader;

import java.util.Map;

/**
 * @Author Lanxiaowei
 * @Date 2018-02-09 11:25
 * @Description HttpClientConfigLoader类的测试用例
 */
public class HttpClientConfigLoaderTest {
    public static void main(String[] args) {
        Map<String, Object> configMap = HttpClientConfigLoader.getConfigMap();
        for (Map.Entry<String, Object> entry : configMap.entrySet()) {
            String key = entry.getKey();
            Object val = entry.getValue();
            System.out.println(key + "=" + val);
        }
    }
}
