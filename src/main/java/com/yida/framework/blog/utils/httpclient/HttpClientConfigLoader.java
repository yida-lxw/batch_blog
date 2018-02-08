package com.yida.framework.blog.utils.httpclient;

import com.yida.framework.blog.utils.cache.CacheManager;
import com.yida.framework.blog.utils.common.PropertiesUtil;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @Author Lanxiaowei
 * @Date 2018-02-08 11:15
 * @Description HttpClient配置加载器
 */
public class HttpClientConfigLoader {


    /**
     * PoolingHttpClientConnectionManager poolingmgr = new PoolingHttpClientConnectionManager(
     RegistryBuilder.<ConnectionSocketFactory>create()
     .register("http", PlainConnectionSocketFactory.getSocketFactory())
     .register("https", sslSocketFactoryCopy)
     .build(),
     null,
     null,
     null,
     connTimeToLive,
     connTimeToLiveTimeUnit != null ? connTimeToLiveTimeUnit : TimeUnit.MILLISECONDS);
     */


    private static final String HTTPCLIENT_CONFIG_FILENAME = "httpclient.properties";

    private static Map<String, Object> configMap = new ConcurrentHashMap<String, Object>();

    static {
        Map<String, Object> map = readConfig();
        if (null != map && !map.isEmpty()) {
            configMap.putAll(map);
        }
    }

    public static Map<String, Object> readConfig() {
        return CacheManager.getData("HTTPCLIENT_CONFIG", new CacheManager.Load<Map<String, Object>>() {
            @Override
            public Map<String, Object> load() {
                return PropertiesUtil.getPropertiesMap(HTTPCLIENT_CONFIG_FILENAME);
            }
        }, 0);
    }

    public static String getStringProperty(String key, String defaultValue) {
        Object val = configMap.get(key);
        return (null == val || "".equalsIgnoreCase(val.toString())) ? defaultValue : val.toString();
    }

    public static String getStringProperty(String key) {
        return getStringProperty(key, null);
    }

    public static Integer getIntProperty(String key, Integer defaultValue) {
        Object val = configMap.get(key);
        try {
            return (null == val) ? defaultValue : Integer.valueOf(val.toString());
        } catch (Exception e) {
            return null;
        }
    }

    public static Integer getIntProperty(String key) {
        return getIntProperty(key, null);
    }

    public static Short getShortProperty(String key, Short defaultValue) {
        Object val = configMap.get(key);
        try {
            return (null == val) ? defaultValue : Short.valueOf(val.toString());
        } catch (Exception e) {
            return null;
        }
    }

    public static Short getShortProperty(String key) {
        return getShortProperty(key, null);
    }

    public static Long getLongProperty(String key, Long defaultValue) {
        Object val = configMap.get(key);
        try {
            return (null == val) ? defaultValue : Long.valueOf(val.toString());
        } catch (Exception e) {
            return null;
        }
    }

    public static Long getLongProperty(String key) {
        return getLongProperty(key, null);
    }

    public static Float getFloatProperty(String key, Float defaultValue) {
        Object val = configMap.get(key);
        try {
            return (null == val) ? defaultValue : Float.valueOf(val.toString());
        } catch (Exception e) {
            return null;
        }
    }

    public static Float getFloatProperty(String key) {
        return getFloatProperty(key, null);
    }

    public static Double getDoubleProperty(String key, Double defaultValue) {
        Object val = configMap.get(key);
        try {
            return (null == val) ? defaultValue : Double.valueOf(val.toString());
        } catch (Exception e) {
            return null;
        }
    }

    public static Double getDoubleProperty(String key) {
        return getDoubleProperty(key, null);
    }

    public static Boolean getBooleanProperty(String key, Boolean defaultValue) {
        Object val = configMap.get(key);
        try {
            return (null == val) ? defaultValue : Boolean.valueOf(val.toString());
        } catch (Exception e) {
            return null;
        }
    }

    public static Boolean getBooleanProperty(String key) {
        return getBooleanProperty(key, null);
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
}
