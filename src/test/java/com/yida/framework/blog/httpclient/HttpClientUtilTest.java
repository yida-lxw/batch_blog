package com.yida.framework.blog.httpclient;

import com.yida.framework.blog.utils.httpclient.HttpClientUtil;

/**
 * @Author Lanxiaowei
 * @Date 2018-02-09 22:44
 * @Description HttpClient工具类的测试用例
 */
public class HttpClientUtilTest {
    public static void main(String[] args) {
        for (int i = 0; i < 10; i++) {
            String requestURL = "https://detail.tmall.com/item.htm?spm=a230r.1.14.27.210a82558PplkD&id=561751745746&ns=1&abbucket=14";
            String content = HttpClientUtil.get(requestURL, "GBK");
            System.out.println(content);
        }
    }
}
