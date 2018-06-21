package com.yida.framework.blog.utils.httpclient;

import org.apache.http.cookie.Cookie;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * @Author Lanxiaowei
 * @Date 2018-06-21 19:42
 * @Description Http请求返回的数据包装实体
 */
public class Result {
    private String responseBody;

    private Map<String, String> cookies;

    public Result(String responseBody) {
        this.responseBody = responseBody;
    }

    public Result(String responseBody, Map<String, String> cookies) {
        this.responseBody = responseBody;
        this.cookies = cookies;
    }

    public Map<String, String> toCookieMap(List<Cookie> cookieList) {
        if (null == cookieList || cookieList.size() <= 0) {
            return null;
        }
        String key = null;
        String val = null;
        Map<String, String> map = new LinkedHashMap<>();
        for (Cookie cookie : cookieList) {
            key = cookie.getName();
            val = cookie.getValue();
            map.put(key, val);
        }
        this.cookies = map;
        return this.cookies;
    }

    public String buildCookieString(List<Cookie> cookieList) {
        if (null == cookieList || cookieList.size() <= 0) {
            return "";
        }
        String key = null;
        String val = null;
        StringBuilder cookieString = new StringBuilder();
        for (Cookie cookie : cookieList) {
            key = cookie.getName();
            val = cookie.getValue();
            cookieString.append(key + "=" + val + "; ");
        }
        return cookieString.toString();
    }

    public String buildCookieString(Map<String, String> cookieMap) {
        if (null == cookieMap || cookieMap.size() <= 0) {
            return "";
        }
        String key = null;
        String val = null;
        StringBuilder cookieString = new StringBuilder();
        for (Map.Entry<String, String> entry : cookieMap.entrySet()) {
            key = entry.getKey();
            val = entry.getValue();
            cookieString.append(key + "=" + val + "; ");
        }
        return cookieString.toString();
    }

    public String getResponseBody() {
        return responseBody;
    }

    public void setResponseBody(String responseBody) {
        this.responseBody = responseBody;
    }

    public Map<String, String> getCookies() {
        return cookies;
    }

    public void setCookies(Map<String, String> cookies) {
        this.cookies = cookies;
    }
}
