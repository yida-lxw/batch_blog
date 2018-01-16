package com.yida.framework.blog.utils.cache;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;


/**
 * @Author Lanxiaowei
 * @Date 2018-01-10 10:35
 * @Description 缓存工具类
 */
public class CacheManager {
    private static Map<String, CacheData> CACHE_DATA = new ConcurrentHashMap<>();

    /**
     * @param key    数据在缓存中对应的key
     * @param load   当数据过期时，通过Load数据加载器来加载数据到缓存中
     * @param expire 数据过期时间，单位: 秒
     * @Author Lanxiaowei
     * @Date 2018-01-10 10:48:56
     * @Description 从缓存中获取数据
     * @Return T
     */
    public static <T> T getData(String key, Load<T> load, int expire) {
        T data = getData(key);
        if (data == null && load != null) {
            data = load.load();
            if (data != null) {
                setData(key, data, expire);
            }
        }
        return data;
    }

    public static <T> T getData(String key) {
        CacheData<T> data = CACHE_DATA.get(key);
        //数据在缓存中存在且(数据设置了永不过期或数据尚未过期),此时才会返回缓存数据
        if (data != null && (data.getExpire() <= 0 || data.getSaveTime() >= System.currentTimeMillis())) {
            return data.getData();
        }
        return null;
    }

    public static <T> void setData(String key, T data, int expire) {
        CACHE_DATA.put(key, new CacheData(data, expire));
    }

    public static void clear(String key) {
        CACHE_DATA.remove(key);
    }

    public static void clearAll() {
        CACHE_DATA.clear();
    }

    public interface Load<T> {
        T load();
    }

    private static class CacheData<T> {
        private T data;
        // 存活时间
        private long saveTime;
        // 过期时间 小于等于0,表示永久存活
        private long expire;

        CacheData(T t, int expire) {
            this.data = t;
            this.expire = expire <= 0 ? 0 : expire * 1000;
            this.saveTime = System.currentTimeMillis() + this.expire;
        }

        public T getData() {
            return data;
        }

        public long getExpire() {
            return expire;
        }

        public long getSaveTime() {
            return saveTime;
        }
    }
}
