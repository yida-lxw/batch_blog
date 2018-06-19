package com.yida.framework.blog;

/**
 * @Author Lanxiaowei
 * @Date 2018-06-19 11:52
 * @Description 这里是类的描述信息
 */
public class FilePathTest {
    public static void main(String[] args) {
        String path = "asdfas撒旦法师打发![asdfasd阿什顿发](images/_1529314830_16105.gif)asdfasdfasdf阿什顿发";
        String newPath = path.replaceAll("(.*\\!\\[[^\\]]*\\]\\()(images)(\\/[\\_0-9a-zA-Z]+\\.[png|jpg|jpeg|gif]+\\))", "$1aaaaaaaaaa$3");
        System.out.println(newPath);
        System.out.println("");
    }
}
