package com.yida.framework.blog;

import com.yida.framework.blog.utils.common.CMDUtil;

/**
 * @Author Lanxiaowei
 * @Date 2018-01-14 20:55:${}
 * @Description CMDUtil工具类测试
 */
public class CMDTest {
    public static void main(String[] args) {
        String cmd = "cmd /c C: && pandoc -o 使用Java提交代码至Github.md 使用Java提交代码至Github.docx";
        CMDUtil cmdUtil = new CMDUtil(3000);
        cmdUtil.execute(cmd);
    }
}
