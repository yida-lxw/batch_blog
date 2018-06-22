package com.yida.framework.blog.publish.csdn;

import com.yida.framework.blog.publish.BlogPublisher;
import com.yida.framework.blog.utils.common.StringUtil;
import com.yida.framework.blog.utils.h2.H2DBUtil;
import com.yida.framework.blog.utils.httpclient.HttpClientUtil;
import com.yida.framework.blog.utils.httpclient.Result;
import com.yida.framework.blog.utils.io.FileUtil;
import com.yida.framework.blog.utils.json.FastJsonUtil;
import org.apache.commons.codec.Charsets;
import org.apache.commons.codec.net.URLCodec;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import java.io.IOException;
import java.sql.SQLException;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * @Author Lanxiaowei
 * @Date 2018-06-19 14:40
 * @Description CSDN博客发布接口实现
 */
public class CSDNBlogPublisher implements BlogPublisher<CSDNBlogPublisherParam> {
    private URLCodec urlCodec = new URLCodec();

    public static String decodeUnicode(final String dataStr) {
        int start = 0;
        int end = 0;
        final StringBuffer buffer = new StringBuffer();
        while (start > -1) {
            end = dataStr.indexOf("\\u", start + 2);
            String charStr = "";
            if (end == -1) {
                charStr = dataStr.substring(start + 2, dataStr.length());
            } else {
                charStr = dataStr.substring(start + 2, end);
            }
            char letter = (char) Integer.parseInt(charStr, 16);
            buffer.append(new Character(letter).toString());
            start = end;
        }
        return buffer.toString();
    }

    @Override
    public String publish(CSDNBlogPublisherParam blogPublishParam) {
        System.out.println("Enter CSDNBlogPublisher...");
        //读取Hexo目录下的所有Markdown
        List<String> markdownFilePaths = blogPublishParam.getMarkdownFilePaths();
        if (null == markdownFilePaths || markdownFilePaths.size() <= 0) {
            return null;
        }
        String csdnUserName = blogPublishParam.getCSDNUserName();
        String csdnPassword = blogPublishParam.getCSDNPassword();
        String loginUrl = blogPublishParam.getLoginUrl();
        String postBlogUrl = blogPublishParam.getPostBlogUrl();

        //第一步需要先登录CSDN
        String[] params = fetchNecessaryParam(loginUrl);
        if (null == params || params.length <= 0) {
            return null;
        }

        boolean isSuccess = false;
        if (params.length == 1) {
            isSuccess = "1".equals(params[0]) ? true : false;
        }
        if (!isSuccess) {
            try {
                Thread.sleep(5000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            isSuccess = login(loginUrl, csdnUserName, csdnPassword, params);
        }

        if (isSuccess) {
            String blogContent = null;
            String fileName = null;
            for (String markdownPath : markdownFilePaths) {
                fileName = FileUtil.getFileName(markdownPath);
                try {
                    blogContent = FileUtil.readFile(markdownPath, "UTF-8");
                    postBlog(postBlogUrl, blogContent, fileName);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        System.out.println("Exit CSDNBlogPublisher...");
        return null;
    }

    /**
     * 获取必要的登录参数信息
     *
     * @throws IOException
     */
    private String[] fetchNecessaryParam(String loginUrl) {
        // 查看CSDN登录页面源码发现登录时需要post5个参数
        // name、password，另外三个在页面的隐藏域中，a good start
        // 这样登录不行，因为第一次需要访问需要拿到上下文context
        // Document doc = Jsoup.connect(LOGIN_URL).get();


        /************************设置请求头信息  Begin ************************/
        Map<String, String> requestHeanders = new LinkedHashMap<>();
        requestHeanders.put("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,image/apng,*/*;q=0.8");
        requestHeanders.put("Accept-Language", "zh-CN,zh;q=0.9");
        requestHeanders.put("Connection", "keep-alive");
        requestHeanders.put("Cache-Control", "max-age=0");
        //设置Cookie
        setRequestCookie(requestHeanders);
        requestHeanders.put("Host", "passport.csdn.net");
        requestHeanders.put("Referer", "https://passport.csdn.net/account/logout?ref=toolbar");
        requestHeanders.put("User-Agent", "Mozilla/5.0 (Windows NT 6.1; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/66.0.3359.139 Safari/537.36");
        requestHeanders.put("Upgrade-Insecure-Requests", "1");
        /************************设置请求头信息  End ************************/
        Result responseResult = null;
        try {
            responseResult = HttpClientUtil.get(loginUrl, null, requestHeanders);
            String html = (null == responseResult) ? "" : responseResult.getResponseBody();
            if (html.contains("redirect_back")) {
                return new String[]{"1"};
            }
            Document doc = Jsoup.parse(html);
            Element form = doc.select(".user-pass").get(0);
            String lt = form.select("input[name=lt]").get(0).val();
            String execution = form.select("input[name=execution]").get(0).val();
            String _eventId = form.select("input[name=_eventId]").get(0).val();
            System.out.println("lt:" + lt);
            System.out.println("execution:" + execution);
            System.out.println("_eventId:" + _eventId);
            return new String[]{lt, execution, _eventId};
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (null != responseResult) {
                Map<String, String> cookieMap = responseResult.getCookies();
                saveCookie(cookieMap);
            }
        }
        return null;
    }

    /**
     * 登录CSDN
     *
     * @param loginUrl    登录请求链接
     * @param userName    CSDN登录账号
     * @param password    CSDN登录密码
     * @param otherParams 登录需要的其他参数
     * @return
     */
    private boolean login(String loginUrl, String userName, String password, String[] otherParams) {
        boolean result = false;
        Map<String, String> requestParams = new LinkedHashMap<>();
        requestParams.put("username", userName);
        requestParams.put("password", password);
        requestParams.put("lt", otherParams[0]);
        requestParams.put("execution", otherParams[1]);
        requestParams.put("_eventId", otherParams[2]);


        Map<String, String> requestHeanders = new LinkedHashMap<>();
        requestHeanders.put("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,image/apng,*/*;q=0.8");
        requestHeanders.put("Accept-Language", "zh-CN,zh;q=0.9");
        requestHeanders.put("Connection", "keep-alive");
        requestHeanders.put("Cache-Control", "max-age=0");
        //设置Cookie
        setRequestCookie(requestHeanders);
        requestHeanders.put("Host", "passport.csdn.net");
        requestHeanders.put("Referer", "https://passport.csdn.net/account/logout?ref=toolbar");
        requestHeanders.put("User-Agent", "Mozilla/5.0 (Windows NT 6.1; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/66.0.3359.139 Safari/537.36");
        requestHeanders.put("Upgrade-Insecure-Requests", "1");

        Result responseResult = null;
        try {
            responseResult = HttpClientUtil.postForm(loginUrl, requestParams, requestHeanders);
            String ret = (null == responseResult) ? "" : responseResult.getResponseBody();
            System.out.println(ret);
            if (ret.indexOf("redirect_back") > -1) {
                System.out.println("登录成功。。。。。");
                result = true;
            } else if (ret.indexOf("登录太频繁") > -1) {
                System.out.println("登录太频繁，请稍后再试。。。。。");
                result = false;
            } else {
                System.out.println("登录失败。。。。。");
                result = false;
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (null != responseResult) {
                Map<String, String> cookieMap = responseResult.getCookies();
                saveCookie(cookieMap);
            }
        }

        return result;
    }

    /**
     * 发布博客
     *
     * @param postBlogUrl 发布博客的接口URL
     * @param blogContent 博客的Markdown语法格式的字符串
     * @param fileName    Markdown文件名称
     * @return
     */
    private boolean postBlog(String postBlogUrl, String blogContent, String fileName) {
        if (StringUtil.isEmpty(blogContent)) {
            return false;
        }

        /************************解析博客标题、标签、分类信息  Begin ************************/
        String title = "";
        String tags = "";
        String categories = "";
        String channel = "16";
        String type = "original";
        String articleedittype = "1";
        int firstIndex = blogContent.indexOf("---");
        int lastIndex = blogContent.indexOf("---", firstIndex + 3);
        String factContent = null;
        String headPart = null;
        if (firstIndex == 0 && lastIndex != -1) {
            factContent = blogContent.substring(lastIndex + 3);
            factContent = factContent.replaceAll("^\n", "");
            headPart = blogContent.substring(firstIndex + 3, lastIndex);
            String[] lines = headPart.split("\n");
            boolean nextTags = false;
            boolean nextCategories = false;
            for (String line : lines) {
                line = line.trim();
                if ("".equals(line)) {
                    continue;
                }
                if (line.contains("title:")) {
                    title = line.replace("title:", "").trim();
                } else if (line.contains("tags:")) {
                    String tag = line.replace("tags:", "").trim();
                    if ("".equals(tag)) {
                        nextTags = true;
                    } else {
                        nextTags = false;
                        if (tag.indexOf("[") != -1 && tag.indexOf("]") != -1) {
                            tag = tag.substring(tag.indexOf("[") + 1, tag.indexOf("]"));
                        }
                        tags += tag + ",";
                    }
                    nextCategories = false;
                } else if (nextTags && line.startsWith("-")) {
                    String tag = line.replace("- ", "").trim();
                    if ("".equals(tag)) {
                        nextTags = true;
                    } else {
                        tags += tag + ",";
                    }
                    nextCategories = false;
                } else if (line.contains("categories:")) {
                    String categorie = line.replace("categories:", "").trim();
                    if ("".equals(categorie)) {
                        nextCategories = true;
                    } else {
                        nextCategories = false;
                        if (categorie.indexOf("[") != -1 && categorie.indexOf("]") != -1) {
                            categorie = categorie.substring(categorie.indexOf("[") + 1, categorie.indexOf("]"));
                        }
                        categories += categorie + ",";
                    }
                    nextTags = false;
                } else if (nextCategories && line.startsWith("-")) {
                    String categorie = line.replace("- ", "").trim();
                    if ("".equals(categorie)) {
                        nextCategories = true;
                    } else {
                        categories += categorie + ",";
                    }
                    nextTags = false;
                }
            }
            if (StringUtil.isNotEmpty(tags) && tags.endsWith(",")) {
                tags = tags.substring(0, tags.lastIndexOf(","));
            }
            if (StringUtil.isNotEmpty(categories) && categories.endsWith(",")) {
                categories = categories.substring(0, categories.lastIndexOf(","));
            }
        } else {
            if (StringUtil.isNotEmpty(fileName)) {
                if (fileName.endsWith(".md")) {
                    title = fileName.substring(0, fileName.lastIndexOf(".md"));
                } else if (fileName.endsWith(".MD")) {
                    title = fileName.substring(0, fileName.lastIndexOf(".MD"));
                }
            }
        }
        /************************解析博客标题、标签、分类信息  End ************************/


        /************************设置请求头信息  Begin ************************/
        Map<String, String> requestHeanders = new LinkedHashMap<>();
        requestHeanders.put("Accept-Language", "zh-CN,zh;q=0.9");
        requestHeanders.put("Connection", "keep-alive");
        requestHeanders.put("Content-Type", "application/x-www-form-urlencoded; charset=UTF-8");
        requestHeanders.put("Host", "mp.csdn.net");
        requestHeanders.put("Origin", "https://mp.csdn.net");
        requestHeanders.put("Referer", "https://mp.csdn.net/mdeditor");
        requestHeanders.put("User-Agent", "Mozilla/5.0 (Windows NT 6.1; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/66.0.3359.139 Safari/537.36");
        requestHeanders.put("X-Requested-With", "XMLHttpRequest");
        //设置Cookie
        setRequestCookie(requestHeanders);
        /************************设置请求头信息  End ************************/


        /************************设置请求表单信息  Begin ************************/
        try {
            factContent = urlCodec.encode(factContent, Charsets.UTF_8.name());
            factContent = factContent.replace("+", "%20").replace("(", "%28").replace(")", "%29");

            title = urlCodec.encode(title, Charsets.UTF_8.name());
        } catch (Exception e) {
            e.printStackTrace();
        }

        Map<String, String> requestParams = new LinkedHashMap<>();
        String blogId = loadBlogId(title);
        if (StringUtil.isNotEmpty(blogId)) {
            //设置了ID参数，则会更新博客，而不是重新发布一篇新博客，从而避免博客重复发布
            requestParams.put("id", blogId);
        }
        requestParams.put("title", title);
        requestParams.put("content", "    ");
        requestParams.put("markdowncontent", factContent);
        requestParams.put("id", "");
        requestParams.put("private", "0");
        requestParams.put("tags", tags);
        requestParams.put("categories", categories);
        requestParams.put("status", "0");
        requestParams.put("channel", channel);
        requestParams.put("type", type);
        requestParams.put("articleedittype", articleedittype);
        requestParams.put("Description", "");
        /************************设置请求表单信息  End ************************/


        /************************开始发布博客至CSDN  Bgin ************************/

        Result responseResult = null;
        boolean isPostSuccess = false;
        String errorMsg = null;
        try {
            responseResult = HttpClientUtil.postForm(postBlogUrl, requestParams, requestHeanders);
            String ret = (null == responseResult) ? "" : responseResult.getResponseBody();

            if (StringUtil.isNotEmpty(ret)) {
                System.out.println(ret);
                isPostSuccess = ret.contains("\"status\":true");
                //如果博客发布成功
                if (isPostSuccess) {
                    String blog_id = getBlogId(ret);
                    if (StringUtil.isNotEmpty(blog_id)) {
                        //将已发布的博客的ID保存到本地的H2数据库表blogs中
                        saveBlogInfo(blog_id, title);
                    }
                } else {
                    //如果博客发布失败
                    errorMsg = getErrorMsg(ret);
                    System.out.println("博客发布失败原因：" + errorMsg);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (null != responseResult) {
                Map<String, String> cookieMap = responseResult.getCookies();
                saveCookie(cookieMap);
            }
        }
        System.out.println("博客发布结果：\n" + (isPostSuccess ? "成功" : "失败,可能原因：" + errorMsg));
        /************************开始发布博客至CSDN  End ************************/
        return true;
    }

    /**
     * 将服务器端返回的Cookie数据保存到本地H2数据库表中
     * @param cookieMap
     */
    private void saveCookie(Map<String, String> cookieMap) {
        if (null == cookieMap || cookieMap.size() <= 0) {
            return;
        }
        String key = null;
        String val = null;
        String site_id = "csdn";
        String querySql = "select id from cookies where site_id=? and cookie_key=?";
        String updateSql = "update cookies set cookie_key=?,cookie_val=? where site_id=? and id=?";
        String insertSql = "insert into cookies(site_id,cookie_key,cookie_val) values(?,?,?)";
        for (Map.Entry<String, String> entry : cookieMap.entrySet()) {
            key = entry.getKey();
            val = entry.getValue();
            try {
                Long id = H2DBUtil.queryColumn(querySql, new Object[]{site_id, key}, "id", Long.class);
                if (id == null || id == 0) {
                    H2DBUtil.executeUpdate(insertSql, new Object[]{site_id, key, val});
                } else {
                    H2DBUtil.executeUpdate(updateSql, new Object[]{key, val, site_id, id});
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 将本地缓存的Cookie数据设置到请求头信息中
     * @param requestHeanders
     */
    private void setRequestCookie(Map<String, String> requestHeanders) {
        List<Map<String, Object>> cookieList = null;
        try {
            cookieList = H2DBUtil.executeQuery(
                    "select * from cookies where site_id = ?",
                    new Object[]{"csdn"});
        } catch (SQLException e) {
            e.printStackTrace();
        }
        String cookie = new Result(null).buildCookie(cookieList);
        if (StringUtil.isNotEmpty(cookie)) {
            requestHeanders.put("Cookie", cookie);
        }
    }

    /**
     * 从服务器端返回的JSON字符串中获取新发布的博客的ID
     *
     * @param responseJson
     * @return
     */
    private String getBlogId(String responseJson) {
        Map<String, Object> map = null;
        try {
            map = FastJsonUtil.toMap(responseJson);
        } catch (Exception e) {
            return null;
        }
        if (null == map || map.size() <= 0) {
            return null;
        }
        Map<String, Object> dataMap = (Map<String, Object>) map.get("data");
        if (null == dataMap || dataMap.size() <= 0) {
            return null;
        }
        Object obj = dataMap.get("id");
        if (null == obj || "".equalsIgnoreCase(obj.toString())) {
            return null;
        }
        return obj.toString();
    }

    /**
     * 从服务器端返回的JSON字符串中获取博客发布失败的提示信息
     *
     * @param responseJson
     * @return
     */
    private String getErrorMsg(String responseJson) {
        Map<String, Object> map = null;
        try {
            map = FastJsonUtil.toMap(responseJson);
        } catch (Exception e) {
            return null;
        }
        if (null == map || map.size() <= 0) {
            return null;
        }
        Object obj = map.get("error");
        if (null == obj || "".equalsIgnoreCase(obj.toString())) {
            return null;
        }
        return obj.toString();
    }

    /**
     * 保存博客信息至H2数据库
     *
     * @param blogId
     * @param title
     */
    private void saveBlogInfo(String blogId, String title) {
        String querySql = "select id from blogs where blog_id=? and site_id=?";
        String insertSql = "insert into blogs(site_id,blog_id,title) values(?,?,?)";
        String updateSql = "update blogs set title=? where id=? and blog_id=? and site_id=?";
        Long id = null;
        try {
            id = H2DBUtil.queryColumn(querySql, new Object[]{blogId, "csdn"}, "id", Long.class);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        if (null == id || id.longValue() == 0L) {
            try {
                H2DBUtil.executeUpdate(insertSql, new Object[]{"csdn", blogId, title});
            } catch (SQLException e) {
                e.printStackTrace();
            }
        } else {
            try {
                H2DBUtil.executeUpdate(updateSql, new Object[]{title, id, blogId, "csdn"});
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 从H2数据库中加载出是否包含有符合指定标题title的博客对应的唯一ID
     *
     * @param title 博客的标题
     * @return
     */
    private String loadBlogId(String title) {
        String querySql = "select blog_id from blogs where site_id=? and title=?";
        String blogId = null;
        try {
            blogId = H2DBUtil.queryColumn(querySql, new Object[]{"csdn", title}, "blog_id", String.class);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return blogId;
    }
}
