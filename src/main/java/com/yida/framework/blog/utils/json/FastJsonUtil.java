package com.yida.framework.blog.utils.json;


import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;

import java.util.List;
import java.util.Map;

/**
 * @Author Lanxiaowei
 * @Date 2018-01-16 13:38
 * @Description FastJSON操作工具类
 */
public class FastJsonUtil {
    //private Logger log = LogManager.getLogger(FastJsonUtil.class.getName());

    /**
     * 功能描述：把JSON数据转换成普通字符串列表
     *
     * @param jsonData JSON数据
     * @return
     * @throws Exception
     * @author myclover
     */
    public static List<String> getStringList(String jsonData) throws Exception {
        return JSON.parseArray(jsonData, String.class);
    }

    /**
     * 功能描述：把JSON数据转换成指定的java对象
     *
     * @param jsonData JSON数据
     * @param clazz    指定的java对象
     * @return
     * @throws Exception
     * @author myclover
     */
    public static <T> T getSingleBean(String jsonData, Class<T> clazz)
            throws Exception {
        return JSON.parseObject(jsonData, clazz);
    }

    /**
     * 功能描述：把JSON数据转换成指定的java对象列表
     *
     * @param jsonData JSON数据
     * @param clazz    指定的java对象
     * @return
     * @throws Exception
     * @author myclover
     */
    public static <T> List<T> getBeanList(String jsonData, Class<T> clazz)
            throws Exception {
        return JSON.parseArray(jsonData, clazz);
    }

    /**
     * 功能描述：把JSON数据转换成较为复杂的java对象列表
     *
     * @param jsonData JSON数据
     * @return
     * @throws Exception
     * @author myclover
     */
    public static List<Map<String, Object>> getBeanMapList(String jsonData) {
        return JSON.parseObject(jsonData,
                new TypeReference<List<Map<String, Object>>>() {
                });
    }

    /**
     * JSONObject.toJSONString(result,
     SerializerFeature.WriteClassName,
     SerializerFeature.WriteMapNullValue,
     SerializerFeature.WriteNullBooleanAsFalse,
     SerializerFeature.WriteNullListAsEmpty,
     SerializerFeature.WriteNullNumberAsZero,
     SerializerFeature.WriteNullStringAsEmpty,
     SerializerFeature.WriteDateUseDateFormat,
     SerializerFeature.WriteEnumUsingToString,
     SerializerFeature.WriteSlashAsSpecial,
     SerializerFeature.WriteTabAsSpecial);
     */
}
