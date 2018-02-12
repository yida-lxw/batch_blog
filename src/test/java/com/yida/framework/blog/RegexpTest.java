package com.yida.framework.blog;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @Author Lanxiaowei
 * @Date 2018-02-12 10:25
 * @Description 使用正则表达式测试从文件名中提取中括号中的文字信息
 */
public class RegexpTest {
    public static void main(String[] args) {
        String str = "[(Java)]抽象[for RegexpTest]类定义整个流程骨架[ignore].docx";
        String regex = "(?<=\\[)([^\\[^\\]]+)(?=\\])";
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(str);
        while (matcher.find()) {
            System.out.println(matcher.group());
        }
    }
}
