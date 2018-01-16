package com.yida.framework.blog.config;

import com.yida.framework.blog.utils.cache.CacheManager;
import com.yida.framework.blog.utils.common.GerneralUtil;
import com.yida.framework.blog.utils.common.PropertiesUtil;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

/**
 * @Author Lanxiaowei
 * @Date 2018-01-16 10:14
 * @Description 配置文件加载类
 */
public class ConfigManager {
    private String configPath;
    private ConfigType configType;

    public ConfigManager(String configPath, ConfigType configType) {
        this.configPath = configPath;
        if (null == configType) {
            //默认采用JSON格式作为配置文件
            configType = ConfigType.JSON;
        }
        this.configType = configType;
    }

    public ConfigManager(String configPath) {
        this(configPath, null);
    }

    public static void main(String[] args) {
        String configPath = "blog.json";
        ConfigManager configManager = new ConfigManager(configPath);
        Map<String, Object> map = configManager.readConfig();
        System.out.println(map);

        configPath = "blog.properties";
        configManager.setConfigPath(configPath);
        configManager.setConfigType(ConfigType.PROPERTIES);
        map = configManager.readConfig();
        System.out.println(map);
    }

    public Map<String, Object> readConfig() {
        if (ConfigType.JSON.equals(this.configType)) {
            return CacheManager.getData("JSON_CONFIG", new CacheManager.Load<Map<String, Object>>() {

                @Override
                public Map<String, Object> load() {
                    return readJSONConf();
                }
            }, 0);
        }
        if (ConfigType.PROPERTIES.equals(this.configType)) {
            return PropertiesUtil.getPropertiesMap(this.configPath);
        }
        return null;
    }

    /**
     * 读取JSON格式的配置文件
     *
     * @return
     */
    private Map<String, Object> readJSONConf() {
        //读取JSON格式的配置文件
        ClassLoader classLoader = getClass().getClassLoader();
        StringBuilder builder = new StringBuilder();
        File file = new File(classLoader.getResource(this.configPath).getFile());
        try (Scanner scanner = new Scanner(file)) {
            while (scanner.hasNextLine()) {
                String line = scanner.nextLine();
                //跳过空白行和注释行
                if (GerneralUtil.isEmptyString(line) || line.startsWith("#")) {
                    continue;
                }
                line = line.replaceAll("\\/\\/[^\\n]*|\\/\\*([^\\*^\\/]*|[\\*^\\/*]*|[^\\**\\/]*)*\\*+\\/", "").trim();
                if (GerneralUtil.isEmptyString(line)) {
                    continue;
                }
                if (-1 != line.lastIndexOf("\" ,") && line.lastIndexOf("\" ,") == line.indexOf("\" ,")) {
                    line = line.substring(0, line.lastIndexOf("\" ,") + 3);
                } else if (-1 != line.lastIndexOf("\",") && line.lastIndexOf("\",") == line.indexOf("\",")) {
                    line = line.substring(0, line.lastIndexOf("\",") + 2);
                }
                builder.append(line).append("\n");
            }
            scanner.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        if (builder.length() > 0) {
            //这里需要将JSON字符串转换成Map
            if (builder.toString().endsWith("\n")) {
                //删除末尾的换行符
                builder.deleteCharAt(builder.length() - 1);
            }
            System.out.println(builder.toString());
            return new HashMap<>();
        }
        return null;
    }

    public String getConfigPath() {
        return configPath;
    }

    public void setConfigPath(String configPath) {
        this.configPath = configPath;
    }

    public ConfigType getConfigType() {
        return configType;
    }

    public void setConfigType(ConfigType configType) {
        this.configType = configType;
    }
}
