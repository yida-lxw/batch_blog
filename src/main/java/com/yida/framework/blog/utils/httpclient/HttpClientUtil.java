package com.yida.framework.blog.utils.httpclient;

import com.yida.framework.blog.utils.httpclient.factory.HttpClientFactory;
import org.apache.http.Consts;
import org.apache.http.HttpEntity;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.entity.ContentType;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.Args;
import org.apache.http.util.CharArrayBuffer;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.*;
import java.net.URI;
import java.net.URISyntaxException;
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
     * @param url                     Post提交的请求URL地址
     * @param formParams              表单提交参数
     * @param headers                 请求头信息
     * @param contentEncoding         响应体内容的字符编码,默认为UTF-8
     * @param responseContentType     响应体内容的MIME类型,默认为text/html
     * @param formEncoding            请求参数的编码字符集,默认为UTF-8
     * @return
     */
    public static String postForm(String url, Map<String, String> formParams, Map<String, String> headers,
                                  String contentEncoding, String responseContentType, String formEncoding) {
        //Get HttpClient instance
        CloseableHttpClient closeHttpClient = getHttpClient();
        CloseableHttpResponse httpResponse = null;
        HttpPost httpPost = new HttpPost(url);

        if (null != headers && !headers.isEmpty()) {
            for (Map.Entry<String, String> entry : headers.entrySet()) {
                httpPost.addHeader(entry.getKey(), entry.getValue());
            }

        }

        if (null != formParams && !formParams.isEmpty()) {
            List<NameValuePair> params = new ArrayList<NameValuePair>(formParams.size());
            for (Map.Entry<String, String> entry : formParams.entrySet()) {
                params.add(new BasicNameValuePair(entry.getKey(), entry.getValue()));
            }

            try {
                httpPost.setEntity(new UrlEncodedFormEntity(params,
                        (null == formEncoding || "".equals(formEncoding)) ?
                                StandardCharsets.UTF_8.displayName() : formEncoding));
            } catch (UnsupportedEncodingException e) {
                log.error("An exception occurs when we encoding the form parameter:\n{}", e.getMessage());
            }
        }

        //begin to issue post request
        try {
            httpResponse = closeHttpClient.execute(httpPost);
            HttpEntity httpEntity = httpResponse.getEntity();
            return httpEntity2String(httpEntity, contentEncoding, responseContentType);
        } catch (IOException e) {
            log.error("An exception occurred while performing the HTTP post request with URL[{}]:\n{}.", url, e.getMessage());
        }
        return null;
    }

    /**
     * Post方式提交表单
     *
     * @param url                 Post提交的请求URL地址
     * @param formParams          表单提交参数
     * @param headers             请求头信息
     * @param contentEncoding     响应体内容的字符编码,默认为UTF-8
     * @param responseContentType 响应体内容的MIME类型,默认为text/html
     * @return
     */
    public static String postForm(String url, Map<String, String> formParams, Map<String, String> headers,
                                  String contentEncoding, String responseContentType) {
        return postForm(url, formParams, headers, contentEncoding, responseContentType, null);
    }

    /**
     * Post方式提交表单
     *
     * @param url             Post提交的请求URL地址
     * @param formParams      表单提交参数
     * @param headers         请求头信息
     * @param contentEncoding 响应体内容的字符编码,默认为UTF-8
     * @return
     */
    public static String postForm(String url, Map<String, String> formParams, Map<String, String> headers,
                                  String contentEncoding) {
        return postForm(url, formParams, headers, contentEncoding, null, null);
    }

    /**
     * Post方式提交表单
     *
     * @param url        Post提交的请求URL地址
     * @param formParams 表单提交参数
     * @param headers    请求头信息
     * @return
     */
    public static String postForm(String url, Map<String, String> formParams, Map<String, String> headers) {
        return postForm(url, formParams, headers, null, null, null);
    }

    /**
     * Post方式提交表单
     *
     * @param url                 Post提交的请求URL地址
     * @param formParams          表单提交参数
     * @param contentEncoding     响应体内容的字符编码,默认为UTF-8
     * @param responseContentType 响应体内容的MIME类型,默认为text/html
     * @return
     */
    public static String postForm(String url, Map<String, String> formParams, String contentEncoding, String responseContentType) {
        return postForm(url, formParams, null, contentEncoding, responseContentType, null);
    }

    /**
     * Post方式提交表单
     *
     * @param url             Post提交的请求URL地址
     * @param formParams      表单提交参数
     * @param contentEncoding 响应体内容的字符编码,默认为UTF-8
     * @return
     */
    public static String postForm(String url, Map<String, String> formParams, String contentEncoding) {
        return postForm(url, formParams, null, contentEncoding, null, null);
    }

    /**
     * Post方式提交表单
     *
     * @param url        Post提交的请求URL地址
     * @param formParams 表单提交参数
     * @return
     */
    public static String postForm(String url, Map<String, String> formParams) {
        return postForm(url, formParams, null, null, null, null);
    }

    /**
     * Post方式提交表单
     *
     * @param url                 Post提交的请求URL地址
     * @param contentEncoding     响应体内容的字符编码,默认为UTF-8
     * @param responseContentType 响应体内容的MIME类型,默认为text/html
     * @return
     */
    public static String postForm(String url, String contentEncoding, String responseContentType) {
        return postForm(url, null, null, contentEncoding, responseContentType, null);
    }

    /**
     * Post方式提交表单
     *
     * @param url             Post提交的请求URL地址
     * @param contentEncoding 响应体内容的字符编码,默认为UTF-8
     * @return
     */
    public static String postForm(String url, String contentEncoding) {
        return postForm(url, null, null, contentEncoding, null, null);
    }

    /**
     * Post方式提交表单
     *
     * @param url Post提交的请求URL地址
     * @return
     */
    public static String postForm(String url) {
        return postForm(url, null, null, null, null, null);
    }

    /**
     * 发送Http get请求
     *
     * @param url                 get请求的请求URL
     * @param requestParams       get请求的请求参数,通过代码自动拼接到请求URL末尾
     * @param requestHeader       get请求的头信息
     * @param contentEncoding     响应体内容的字符串编码,默认为UTF-8
     * @param responseContentType 响应体内容的MIME类型,默认为text/html
     * @param urlEncoding         get请求参数的URL编码字符集,默认为UTF-8
     * @return
     */
    public static String get(String url, Map<String, String> requestParams, Map<String, String> requestHeader,
                             String contentEncoding, String responseContentType, String urlEncoding) {
        HttpGet httpGet = new HttpGet();
        if (null != requestParams && !requestParams.isEmpty()) {
            /*List<NameValuePair> params = new ArrayList<NameValuePair>(requestParams.size());
            for (Map.Entry<String, String> entry : requestParams.entrySet()) {
                params.add(new BasicNameValuePair(entry.getKey(), entry.getValue()));
            }
            String paramString = null;
            try {
                paramString = EntityUtils.toString(new UrlEncodedFormEntity(params,
                        (null == urlEncoding || "".equals(urlEncoding)) ?
                                StandardCharsets.UTF_8.displayName() : urlEncoding),Consts.UTF_8);
            } catch (IOException e) {
                log.error("While URLEncoding The URL parameters for the URL[{}],we occured exception:\n{}",url,e.getMessage());
                return null;
            }
            httpGet.setURI(URI.create(url + "?" + paramString));*/

            //改用URIBuilder类来实现URL参数拼接
            URIBuilder uriBuilder = null;
            try {
                uriBuilder = new URIBuilder(url);
                uriBuilder.setCharset((null == urlEncoding || "".equals(urlEncoding)) ? Consts.UTF_8 : Charset.forName(urlEncoding));
            } catch (URISyntaxException e) {
                log.error("While building the instance of URIBuilder,we occured [URISyntaxException]:\n{}", url, e.getMessage());
                return null;
            }

            for (Map.Entry<String, String> entry : requestParams.entrySet()) {
                uriBuilder.setParameter(entry.getKey(), entry.getValue());
            }
            URI fullUri = null;
            try {
                fullUri = uriBuilder.build();
            } catch (URISyntaxException e) {
                log.error("While appending the parameters to the request uri,we occured [URISyntaxException]:\n{}", fullUri, e.getMessage());
                return null;
            }
            httpGet.setURI(fullUri);
        } else {
            httpGet.setURI(URI.create(url));
        }

        //Add Request Header Info for the Http Get Request
        if (null != requestHeader && !requestHeader.isEmpty()) {
            for (Map.Entry<String, String> entry : requestHeader.entrySet()) {
                httpGet.addHeader(entry.getKey(), entry.getValue());
            }
        }
        //Get HttpClient instance
        CloseableHttpClient closeHttpClient = getHttpClient();
        CloseableHttpResponse httpResponse = null;
        try {
            httpResponse = closeHttpClient.execute(httpGet);
        } catch (IOException e) {
            log.error("An exception occurred while performing the HTTP get request with URL[{}]:\n{}.", httpGet.getURI().toString(), e.getMessage());
            return null;
        }
        HttpEntity httpEntity = httpResponse.getEntity();
        if (null == httpEntity) {
            log.error("The http entity for the http get request with URL[{}] is null,please check it out.", httpGet.getURI().toString());
            return null;
        }
        return httpEntity2String(httpEntity, contentEncoding, responseContentType);
    }

    /**
     * 发送Http get请求
     *
     * @param url                 get请求的请求URL
     * @param requestParams       get请求的请求参数,通过代码自动拼接到请求URL末尾
     * @param requestHeader       get请求的头信息
     * @param contentEncoding     响应体内容的字符串编码,默认为UTF-8
     * @param responseContentType 响应体内容的MIME类型,默认为text/html
     * @return
     */
    public static String get(String url, Map<String, String> requestParams, Map<String, String> requestHeader,
                             String contentEncoding, String responseContentType) {
        return get(url, requestParams, requestHeader, contentEncoding, responseContentType, null);
    }

    /**
     * 发送Http get请求
     *
     * @param url             get请求的请求URL
     * @param requestParams   get请求的请求参数,通过代码自动拼接到请求URL末尾
     * @param requestHeader   get请求的头信息
     * @param contentEncoding 响应体内容的字符串编码,默认为UTF-8
     * @return
     */
    public static String get(String url, Map<String, String> requestParams, Map<String, String> requestHeader,
                             String contentEncoding) {
        return get(url, requestParams, requestHeader, contentEncoding, null, null);
    }

    /**
     * 发送Http get请求
     *
     * @param url                 get请求的请求URL
     * @param requestParams       get请求的请求参数,通过代码自动拼接到请求URL末尾
     * @param contentEncoding     响应体内容的字符串编码,默认为UTF-8
     * @param responseContentType 响应体内容的MIME类型,默认为text/html
     * @return
     */
    public static String get(String url, Map<String, String> requestParams,
                             String contentEncoding, String responseContentType) {
        return get(url, requestParams, null, contentEncoding, responseContentType, null);
    }

    /**
     * 发送Http get请求
     *
     * @param url                 get请求的请求URL
     * @param requestParams       get请求的请求参数,通过代码自动拼接到请求URL末尾
     * @param responseContentType 响应体内容的MIME类型,默认为text/html
     * @return
     */
    public static String get(String url, Map<String, String> requestParams, String responseContentType) {
        return get(url, requestParams, null, null, responseContentType, null);
    }

    /**
     * 发送Http get请求
     *
     * @param url           get请求的请求URL
     * @param requestParams get请求的请求参数,通过代码自动拼接到请求URL末尾
     * @param requestHeader get请求的头信息
     * @return
     */
    public static String get(String url, Map<String, String> requestParams, Map<String, String> requestHeader) {
        return get(url, requestParams, requestHeader, null, null, null);
    }

    /**
     * 发送Http get请求
     *
     * @param url           get请求的请求URL
     * @param requestParams get请求的请求参数,通过代码自动拼接到请求URL末尾
     * @return
     */
    public static String get(String url, Map<String, String> requestParams) {
        return get(url, requestParams, null, null, null, null);
    }

    /**
     * 发送Http get请求
     *
     * @param url get请求的请求URL
     * @return
     */
    public static String get(String url) {
        return get(url, null, null, null, null, null);
    }

    /**
     * 将HttpEntity实体转换成字符串
     *
     * @param httpEntity
     * @param contentEncoding
     * @param responseContentType
     * @return
     */
    public static String httpEntity2String(HttpEntity httpEntity, String contentEncoding, String responseContentType) {
        if (null != httpEntity) {
            InputStream instream = null;
            try {
                instream = httpEntity.getContent();
            } catch (IOException e) {
                log.error("while get the content from the httpEntity,we occur IOException:\n{}", e.getMessage());
                return null;
            }
            if (instream == null) {
                return null;
            } else {
                ContentType contentType = ContentType.create(
                        (null == responseContentType || "".equals(responseContentType)) ? "text/html" : responseContentType,
                        (null == contentEncoding || "".equals(contentEncoding)) ?
                                StandardCharsets.UTF_8.displayName() : contentEncoding);
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
                        try {
                            instream.close();
                        } catch (IOException e) {
                            log.error("We occur an exception while closing the InputStream:\n{}", e.getMessage());
                        }
                    }
                }
            }
        } else {
            log.error("The HttpEntity MUST not be NULL.");
            return null;
        }
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
        if (returnVal.endsWith(".html") || returnVal.endsWith(".htm") ||
                returnVal.endsWith(".HTML") || returnVal.endsWith(".HTM")) {
            returnVal = null;
        }
        return returnVal;
    }

    public static void main(String[] args) {
        System.out.println(getHost("http://spring.io"));
    }

    /** Post Json Parameters Sample code
     * StringEntity content=new StringEntity(data, Charset.forName("utf-8"));// 设置编码
     content.setContentType("application/json; charset=UTF-8");
     content.setContentEncoding("utf-8");
     post.setEntity(content);
     */
}
