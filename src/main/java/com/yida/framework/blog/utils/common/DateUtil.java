package com.yida.framework.blog.utils.common;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;


/**
 * @Author Lanxiaowei
 * @Date 2018-01-16 23:07
 * @Description 日期操作工具类
 */
public class DateUtil {
    private static final Lock lock = new ReentrantLock();
    private static Logger log = LogManager.getLogger(DateUtil.class.getName());
    /**
     * 采用ThreadLocal解决SimpleDateFormat类的线程安全问题
     */
    private static Map<String, ThreadLocal<SimpleDateFormat>> simpleDateFormatMap = new ConcurrentHashMap<String, ThreadLocal<SimpleDateFormat>>();

    /**
     * Date对象转换成日期字符串
     *
     * @param date    待转换的日期对象
     * @param pattern 日期格式,比如yyyy-MM-dd
     * @return
     */
    public static String format(Date date, String pattern) {
        return getSdf(pattern).format(date);
    }

    /**
     * 日期字符串对象转换成Date对象
     *
     * @param dateStr 待转换的日期字符串
     * @param pattern 日期格式,比如yyyy-MM-dd
     * @return
     */
    public static Date parse(String dateStr, String pattern) {
        try {
            return getSdf(pattern).parse(dateStr);
        } catch (ParseException e) {
            log.error("Convert String Value[{?}] to java.util.Date occured unexcepted exception:\n{?}", dateStr, e.getMessage());
            return null;
        }
    }

    /**
     * 获取SimpleDateFormat对象实例
     *
     * @param pattern
     * @return
     */
    private static SimpleDateFormat getSdf(final String pattern) {
        ThreadLocal<SimpleDateFormat> threadLocal = simpleDateFormatMap.get(pattern);
        // 此处的双重判断和同步是为了防止sdfMap这个单例被多次put重复的sdf
        if (threadLocal == null) {
            try {
                lock.lockInterruptibly();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            threadLocal = simpleDateFormatMap.get(pattern);
            if (threadLocal == null) {
                // 只有Map中还没有这个pattern的sdf才会生成新的sdf并放入map
                log.info("put new sdf of pattern " + pattern + " to map");
                // 这里是关键,使用ThreadLocal<SimpleDateFormat>替代原来直接new SimpleDateFormat
                threadLocal = new ThreadLocal<SimpleDateFormat>() {
                    @Override
                    protected SimpleDateFormat initialValue() {
                        log.info("thread: " + Thread.currentThread() + " init pattern: " + pattern);
                        return new SimpleDateFormat(pattern);
                    }
                };
                simpleDateFormatMap.put(pattern, threadLocal);
            }
            lock.unlock();
        }
        return threadLocal.get();
    }
}
