package com.yida.framework.blog.publish.csdn;

import com.yida.framework.blog.publish.BlogPublisher;
import com.yida.framework.blog.utils.common.StringUtil;
import com.yida.framework.blog.utils.httpclient.HttpClientUtil;
import com.yida.framework.blog.utils.httpclient.Result;
import com.yida.framework.blog.utils.io.FileUtil;
import org.apache.commons.codec.Charsets;
import org.apache.commons.codec.net.URLCodec;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import java.io.IOException;
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
        boolean isSuccess = login(loginUrl, csdnUserName, csdnPassword, params);
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
        //requestHeanders.put("Cookie","uuid_tt_dd=1233261396770835785_20171126; csdn_tt_dd=v10_cc91e6490f381a75aa9d4004b2ea9c2fc993e7361101dc5dbeb5c37fedd29284; UE=\"vnetoolxw_87@163.com\"; kd_user_id=ab7d70ee-bd0e-41b5-808c-9e9b7e1c6462; sensorsdata2015jssdkcross=%7B%22distinct_id%22%3A%2216026782080d1-09479f8b739aac-464c0328-2073600-160267820816ff%22%2C%22%24device_id%22%3A%2216026782080d1-09479f8b739aac-464c0328-2073600-160267820816ff%22%7D; gr_user_id=f3a8d719-fe44-4d34-ae3f-61117a74eee4; _ga=GA1.2.1645377725.1516633775; Hm_ct_6bcd52f51e9b3dce32bec4a3997715ac=1788*1*PC_VC; UN=vnetoolxw_87; dc_session_id=10_1529207119260.739075; smidV2=201806191754495a809c78fbe2fa2145c1ae6ab365049000afdccb6730cd560; Hm_lvt_6bcd52f51e9b3dce32bec4a3997715ac=1529413734,1529414216,1529414236,1529414846; UserName=vnetoolxw_87; UserInfo=bDdMJYfKPdOmf7NM865WhDJECFwTXj%2FwNWnL4s5K%2BB5Kpg8tf83zPAhyPpovRaazPOQQnPvKX6CyZIwmFb%2FfbG1X01%2FWdSrBs9MVxM%2FKE1zWqUJrdG1gjpXAOzr1FS21subsNIoqaYFaR94f4R3qIw%3D%3D; UserNick=vnetoolxw_87; AU=200; BT=1529417245649; dc_tos=pakpq4; Hm_lpvt_6bcd52f51e9b3dce32bec4a3997715ac=1529417021");
        requestHeanders.put("Host", "passport.csdn.net");
        requestHeanders.put("Referer", "https://passport.csdn.net/account/logout?ref=toolbar");
        requestHeanders.put("User-Agent", "Mozilla/5.0 (Windows NT 6.1; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/66.0.3359.139 Safari/537.36");
        requestHeanders.put("Upgrade-Insecure-Requests", "1");
        /************************设置请求头信息  End ************************/
        try {
            Result responseResult = HttpClientUtil.get(loginUrl, null, requestHeanders);
            String html = (null == responseResult) ? "" : responseResult.getResponseBody();
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
        //requestHeanders.put("Cookie","uuid_tt_dd=1233261396770835785_20171126; csdn_tt_dd=v10_cc91e6490f381a75aa9d4004b2ea9c2fc993e7361101dc5dbeb5c37fedd29284; UE=\"vnetoolxw_87@163.com\"; kd_user_id=ab7d70ee-bd0e-41b5-808c-9e9b7e1c6462; sensorsdata2015jssdkcross=%7B%22distinct_id%22%3A%2216026782080d1-09479f8b739aac-464c0328-2073600-160267820816ff%22%2C%22%24device_id%22%3A%2216026782080d1-09479f8b739aac-464c0328-2073600-160267820816ff%22%7D; gr_user_id=f3a8d719-fe44-4d34-ae3f-61117a74eee4; _ga=GA1.2.1645377725.1516633775; Hm_ct_6bcd52f51e9b3dce32bec4a3997715ac=1788*1*PC_VC; UN=vnetoolxw_87; dc_session_id=10_1529207119260.739075; JSESSIONID=5E69164CC2C5A5B540158521C7E9F040.tomcat2; smidV2=201806191754495a809c78fbe2fa2145c1ae6ab365049000afdccb6730cd560; BT=1529413854993; LSSC=LSSC-390603-exFu2BvpyuFeLc0sM1LlzCB7EK5DMY-passport.csdn.net; Hm_lvt_6bcd52f51e9b3dce32bec4a3997715ac=1529413734,1529414216,1529414236,1529414846; Hm_lpvt_6bcd52f51e9b3dce32bec4a3997715ac=1529415080; dc_tos=pako87");
        requestHeanders.put("Host", "passport.csdn.net");
        requestHeanders.put("Referer", "https://passport.csdn.net/account/logout?ref=toolbar");
        requestHeanders.put("User-Agent", "Mozilla/5.0 (Windows NT 6.1; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/66.0.3359.139 Safari/537.36");
        requestHeanders.put("Upgrade-Insecure-Requests", "1");

        Result responseResult = HttpClientUtil.postForm(loginUrl, requestParams, requestHeanders);
        String ret = (null == responseResult) ? "" : responseResult.getResponseBody();

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
        return result;
    }

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
        requestParams.put("title", title);
        requestParams.put("content", "aa");
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
        Result responseResult = HttpClientUtil.postForm(postBlogUrl, requestParams, requestHeanders);
        String ret = (null == responseResult) ? "" : responseResult.getResponseBody();
        boolean isPostSuccess = false;
        if (StringUtil.isNotEmpty(ret)) {
            isPostSuccess = ret.contains("\"status\":true");
            //System.out.println(ret);
        }
        System.out.println("博客发布结果：\n" + (isPostSuccess ? "成功" : "失败"));
        /************************开始发布博客至CSDN  End ************************/
        return true;
    }
}
