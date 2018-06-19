package com.yida.framework.blog;

import com.yida.framework.blog.utils.io.FileUtil;
import org.apache.commons.codec.Charsets;
import org.apache.commons.codec.DecoderException;
import org.apache.commons.codec.net.URLCodec;

import java.io.IOException;

/**
 * @Author Lanxiaowei
 * @Date 2018-06-19 11:52
 * @Description 这里是类的描述信息
 */
public class FilePathTest {
    public static void main(String[] args) throws IOException, DecoderException {
        String content = FileUtil.readFile("G:/hexo/source/_posts/Go学习笔记.md");
        System.out.println(content);

        final URLCodec urlCodec = new URLCodec();

        String enMsg = urlCodec.encode(content, Charsets.UTF_8.name());
        enMsg = enMsg.replace("+", "%20").replace("(", "%28").replace(")", "%29");

        System.out.println("\n加密之后:");
        System.out.println(enMsg);

        enMsg = "---%0Atitle%3A%20Go%E5%AE%89%E8%A3%85%0Alayout%3A%20post%0Adate%3A%202018-06-18%2017%3A18%3A16%0Acomments%3A%20true%0Apost_catalog%3A%20true%0Atags%3A%0A-%20Go%0Acategories%3A%0A-%20Go%0Akeywords%3A%20Go%0Apermalink%3A%20install-go%0Adescription%3A%0A---%0A%23%23%20%E5%AE%89%E8%A3%85Go%0A1.%20%E4%B8%8B%E8%BD%BDGo%E5%AE%89%E8%A3%85%E5%8C%85%0AGO%E5%AE%89%E8%A3%85%E5%8C%85%E4%B8%8B%E8%BD%BD%E5%9C%B0%E5%9D%80%EF%BC%9A%5Bhttps%3A%2F%2Fstudygolang.com%2Fdl%5D(https%3A%2F%2Fstudygolang.com%2Fdl)%0A!%5B%5D(https%3A%2F%2Fgithub.com%2Fyida-lxw%2Fblog%2Fblob%2Fmaster%2F20180207%2Fimages%2F_1529314830_16105.png%3Fraw%3Dtrue)%0A2.";
        String result = urlCodec.decode(enMsg);

        System.out.println("加密后在解密：\n");
        System.out.println(result);

        String title = urlCodec.encode("Go安装", Charsets.UTF_8.name());

        System.out.println(title);
    }
}
