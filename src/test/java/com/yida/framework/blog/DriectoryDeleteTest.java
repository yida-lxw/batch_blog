package com.yida.framework.blog;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

/**
 * @Author Lanxiaowei
 * @Date 2018-01-18 23:31
 * @Description 这里是类的描述信息
 */
public class DriectoryDeleteTest {
    public static void main(String[] args) throws IOException {
        String diretory = "C:/test1/";
        boolean result = Files.deleteIfExists(Paths.get(diretory));
        System.out.println("result: " + result);
    }
}
