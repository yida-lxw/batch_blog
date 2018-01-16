package com.yida.framework.blog.config;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @Author Lanxiaowei
 * @Date 2018-01-16 21:39
 * @Description 配置文件上下文
 */
public class ConfigContext {
    private static final String CONFIG_FILE_NAME = "blog";
    //默认采用JSON格式的配置文件
    private static ConfigType configType = ConfigType.JSON;

    private static Map<String, Object> configMap = new ConcurrentHashMap<String, Object>();

    static {
        Map<String, Object> map = new ConfigManager(getConfigFileName(), configType).readConfig();
        if (null != map && !map.isEmpty()) {
            configMap.putAll(map);
        }
    }

    public static String getStringProperty(String key) {
        Object val = configMap.get(key);
        return (null == val || "".equalsIgnoreCase(val.toString())) ? null : val.toString();
    }

    public static Integer getIntProperty(String key) {
        Object val = configMap.get(key);
        try {
            return (null == val) ? null : Integer.valueOf(val.toString());
        } catch (Exception e) {
            return null;
        }
    }

    public static Short getShortProperty(String key) {
        Object val = configMap.get(key);
        try {
            return (null == val) ? null : Short.valueOf(val.toString());
        } catch (Exception e) {
            return null;
        }
    }

    public static Long getLongProperty(String key) {
        Object val = configMap.get(key);
        try {
            return (null == val) ? null : Long.valueOf(val.toString());
        } catch (Exception e) {
            return null;
        }
    }

    public static Float getFloatProperty(String key) {
        Object val = configMap.get(key);
        try {
            return (null == val) ? null : Float.valueOf(val.toString());
        } catch (Exception e) {
            return null;
        }
    }

    public static Double getDoubleProperty(String key) {
        Object val = configMap.get(key);
        try {
            return (null == val) ? null : Double.valueOf(val.toString());
        } catch (Exception e) {
            return null;
        }
    }

    public static Boolean getBooleanProperty(String key) {
        Object val = configMap.get(key);
        try {
            return (null == val) ? null : Boolean.valueOf(val.toString());
        } catch (Exception e) {
            return null;
        }
    }

    public static String[] getStringArrayProperty(String key) {
        Object val = configMap.get(key);
        try {
            if (null == val) {
                return null;
            }
            String str = String.valueOf(val.toString());
            if (null == str || str.length() <= 0) {
                return null;
            }
            if (str.indexOf(";") == -1) {
                return new String[]{str};
            }
            return str.replace("; ", ";").split(";");
        } catch (Exception e) {
            return null;
        }
    }

    public static List<String> getStringListProperty(String key) {
        String[] array = getStringArrayProperty(key);
        if (null == array || array.length <= 0) {
            return null;
        }
        return Arrays.asList(array);
    }

    private static String getConfigFileName() {
        if (ConfigType.JSON.equals(configType)) {
            return CONFIG_FILE_NAME + ".json";
        }
        if (ConfigType.PROPERTIES.equals(configType)) {
            return CONFIG_FILE_NAME + ".properties";
        }
        if (ConfigType.XML.equals(configType)) {
            return CONFIG_FILE_NAME + ".xml";
        }
        //默认加载JSON格式的配置文件
        return CONFIG_FILE_NAME + ".json";
    }

    public void setConfigType(ConfigType configType) {
        this.configType = null == configType ? ConfigType.JSON : configType;
    }
}
