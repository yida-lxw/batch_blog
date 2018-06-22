package com.yida.framework.blog.json;

import com.yida.framework.blog.utils.json.FastJsonUtil;

import java.util.Map;

/**
 * @Author Lanxiaowei
 * @Date 2018-06-22 13:01
 * @Description JSON测试工具类
 */
public class JSONTest {
    public static void main(String[] args) throws Exception {
        String json = "{\"data\":{\"id\":80772279,\"url\":\"https:\\/\\/blog.csdn.net\\/vnetoolxw_87\\/article\\/details\\/80772279\"},\"error\":\"\",\"status\":true}";
        Map<String, Object> map = FastJsonUtil.toMap(json);
        if (null == map || map.size() <= 0) {
            return;
        }
        Map<String, Object> dataMap = (Map<String, Object>) map.get("data");
        if (null == dataMap || dataMap.size() <= 0) {
            return;
        }
        String id = dataMap.get("id").toString();
        System.out.println(id);
    }
}
