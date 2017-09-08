package com.xyj.supermarket.util;

import org.slf4j.Logger;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PropertiesLoaderUtils;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

public abstract class PropUtils {

    private static final Logger logger = LoggerUtils.getLogger(PropUtils.class);

    private static final String PROPERTIES_NAME = "xyj.properties";

    private static final Map<String, String> map = new HashMap<>();

    static {
        final Resource resource;
        if (System.getProperty("os.name").toLowerCase().startsWith("window")) {
            resource = new ClassPathResource(PROPERTIES_NAME);
        } else {
            resource = new FileSystemResource("/home/yd/Workspace/" + PROPERTIES_NAME);
        }

        if (resource.exists()) {
            try {
                Properties properties = PropertiesLoaderUtils.loadProperties(resource);

                properties.stringPropertyNames().forEach(key -> map.put(key, properties.getProperty(key)));

                logger.info("配置文件加载完毕");
            } catch (IOException e) {
                logger.error("加载配置文件{}出错...", PROPERTIES_NAME, e);
            }
        } else {
            logger.error("配置文件{}不存在...", PROPERTIES_NAME);
        }
    }

    public static String getString(String key) {
        return map.get(key);
    }

    public static int getInt(String key) {
        return Integer.parseInt(getString(key));
    }

    public static void main(String[] args) {
        System.out.println(getString("xyj.host"));
        System.out.println(getInt("xyj.port"));
    }
}
