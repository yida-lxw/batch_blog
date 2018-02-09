package com.yida.framework.blog.utils.httpclient;

import com.yida.framework.blog.utils.httpclient.factory.HttpClientFactory;
import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ContentType;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.message.BasicHeader;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.Args;
import org.apache.http.util.CharArrayBuffer;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.*;
import java.net.URI;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Map;

/**
 * @Author Lanxiaowei
 * @Date 2018-02-09 16:44
 * @Description HttpClient操作工具类, 基于Apache HttpClient-4.5.5版本封装的
 */
public class HttpClientUtil {
    private static Logger log = LogManager.getLogger(HttpClientUtil.class.getName());

    /**
     * Post方式提交表单
     *
     * @param url             Post提交的请求URL地址
     * @param formParams      表单提交参数
     * @param headers         请求头信息
     * @param contentEncoding 请求体内容的字符编码
     * @return
     */
    public static String postForm(String url, Map<String, String> formParams, Map<String, String> headers, String contentEncoding) {
        //Get HttpClient instance
        CloseableHttpClient closeHttpClient = getHttpClient();
        CloseableHttpResponse httpResponse = null;
        HttpPost httpPost = new HttpPost(url);

        if (null != headers && !headers.isEmpty()) {
            Header[] userHttpHeader = new Header[headers.size()];
            int index = 0;
            for (Map.Entry<String, String> entry : headers.entrySet()) {
                userHttpHeader[index++] = new BasicHeader(entry.getKey(), entry.getValue());
            }
            httpPost.setHeaders(userHttpHeader);
        }

        if (null != formParams && !formParams.isEmpty()) {
            List<NameValuePair> params = new ArrayList<NameValuePair>(formParams.size());
            for (Map.Entry<String, String> entry : formParams.entrySet()) {
                params.add(new BasicNameValuePair(entry.getKey(), entry.getValue()));
            }

            try {
                httpPost.setEntity(new UrlEncodedFormEntity(params,
                        (null == contentEncoding || "".equals(contentEncoding)) ?
                                StandardCharsets.UTF_8.displayName() : contentEncoding));
            } catch (UnsupportedEncodingException e) {
                log.error("An exception occurs when we encoding the form parameter:\n{}", e.getMessage());
            }
        }

        //begin to issue post request
        try {
            httpResponse = closeHttpClient.execute(httpPost);
            HttpEntity httpEntity = httpResponse.getEntity();
            if (null != httpEntity) {
                InputStream instream = httpEntity.getContent();
                if (instream == null) {
                    return null;
                } else {
                    ContentType contentType = ContentType.create("text/html", contentEncoding);
                    try {
                        Args.check(httpEntity.getContentLength() <= 2147483647L, "HTTP entity too large to be buffered in memory");
                        int capacity = (int) httpEntity.getContentLength();
                        if (capacity < 0) {
                            capacity = 4096;
                        }
                        Charset charset = null;
                        if (contentType != null) {
                            charset = contentType.getCharset();
                            if (charset == null) {
                                ContentType defaultContentType = ContentType.getByMimeType(contentType.getMimeType());
                                charset = defaultContentType != null ? defaultContentType.getCharset() : null;
                            }
                        }

                        if (charset == null) {
                            charset = HTTP.DEF_CONTENT_CHARSET;
                        }

                        Reader reader = new InputStreamReader(instream, charset);
                        CharArrayBuffer buffer = new CharArrayBuffer(capacity);
                        char[] tmp = new char[1024];

                        int l;
                        while ((l = reader.read(tmp)) != -1) {
                            buffer.append(tmp, 0, l);
                        }

                        return buffer.toString();
                    } catch (IOException e) {
                        log.error("We occur an exception while parsing the response information with MIMEType[{}] and charset[{}]:\n{}",
                                contentType.getMimeType(), contentType.getCharset().displayName(Locale.CHINA), e.getMessage());
                        return null;
                    } finally {
                        if (null != instream) {
                            instream.close();
                        }
                    }
                }
            }
        } catch (IOException e) {
            log.error("An exception occurred while performing the HTTP post request with URL[{}]:\n{}.", url, e.getMessage());
        }
        return null;
    }

    /**
     * 获取HttpClient实例对象,内部使用了单例模式,不会重复创建
     *
     * @return
     */
    public static CloseableHttpClient getHttpClient() {
        return HttpClientFactory.getInstance().getHttpClient();
    }

    /**
     * 根据URL地址获取Http Host值
     *
     * @param url
     * @return
     */
    public static String getHost(String url) {
        if (!url.startsWith("http://") && !url.startsWith("HTTP://") &&
                !url.startsWith("https://") && !url.startsWith("HTTPS://")) {
            url = url + "http://";
        }
        String returnVal = null;
        try {
            URI uri = new URI(url);
            returnVal = uri.getHost();
        } catch (Exception e) {
            log.error("Get http host from the URI[{}] occur exception:\n{}", url, e.getMessage());
        }
        if (returnVal.endsWith(".html") || !returnVal.startsWith(".htm") ||
                returnVal.startsWith(".HTML") || !returnVal.startsWith(".HTM")) {
            returnVal = null;
        }
        return returnVal;
    }

    /**
     * StringEntity content=new StringEntity(data, Charset.forName("utf-8"));// 设置编码
     content.setContentType("application/json; charset=UTF-8");
     content.setContentEncoding("utf-8");
     post.setEntity(content);
     */
}
