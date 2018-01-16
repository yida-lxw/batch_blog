package com.yida.framework.blog.config;

import com.yida.framework.blog.utils.cache.CacheManager;
import com.yida.framework.blog.utils.common.GerneralUtil;
import com.yida.framework.blog.utils.common.PropertiesUtil;
import com.yida.framework.blog.utils.json.FastJsonUtil;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.Map;
import java.util.Properties;
import java.util.Scanner;

/**
 * @Author Lanxiaowei
 * @Date 2018-01-16 10:14
 * @Description 配置文件加载类
 */
public class ConfigManager {
    private Logger log = LogManager.getLogger(ConfigManager.class.getName());

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

        configPath = "blog.xml";
        configManager.setConfigPath(configPath);
        configManager.setConfigType(ConfigType.XML);
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
        if (ConfigType.XML.equals(this.configType)) {
            return CacheManager.getData("XML_CONFIG", new CacheManager.Load<Map<String, Object>>() {
                @Override
                public Map<String, Object> load() {
                    return readXMLConf();
                }
            }, 0);
        }
        log.error("This config file[{?}] isn't supported at now.", this.configPath);
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
            String json = builder.toString().trim();
            if (json.startsWith("{") && json.endsWith("}")) {
                try {
                    return FastJsonUtil.toMap(json);
                } catch (Exception e) {
                    log.error("Convert JSON String[{?}] to Map<String,Object> occured an unexcepted exception.", json);
                    return null;
                }
            }
        }
        return null;
    }

    /**
     * 读取XML格式的配置文件
     *
     * @return
     */
    private Map<String, Object> readXMLConf() {
        Properties props = new Properties();
        InputStream input = this.getClass().getClassLoader().getResourceAsStream(this.configPath);
        if (input != null) {
            try {
                props.loadFromXML(input);
                return PropertiesUtil.properties2Map(props);
            } catch (IOException e) {
                log.error("Load the xml config file[{?}] occured an unexcepted exception.", this.configPath);
                return null;
            } finally {
                try {
                    input.close();
                } catch (IOException e) {
                    log.error("Close the inputStream for the xml file[{?}] occured an unexcepted exception.", this.configPath);
                }
            }
        } else {
            log.error("We can't find the xml config file[{?}].", this.configPath);
            return null;
        }
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
