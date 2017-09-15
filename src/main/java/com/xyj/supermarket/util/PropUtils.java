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

    private static final String LINUX_DIR = "/home/yd/Workspace/eStore/";
    private static final String PROFILE = "xyj.properties";

    private static final Map<String, String> map = new HashMap<>();

    static {
        Resource resource = System.getProperty("os.name").toLowerCase().startsWith("window") ? new ClassPathResource(PROFILE) : new FileSystemResource(LINUX_DIR + PROFILE);

        if (resource.exists()) {
            try {
                Properties properties = PropertiesLoaderUtils.loadProperties(resource);

                properties.stringPropertyNames().forEach(key -> map.put(key, properties.getProperty(key)));

                logger.info("配置文件加载完毕");
            } catch (IOException e) {
                logger.error("加载配置文件{}出错...", PROFILE, e);
            }
        } else {
            logger.error("配置文件{}不存在...", PROFILE);
        }
    }

    public static String getString(String key) {
        return map.get(key);
    }

    public static int getInt(String key) {
        return Integer.parseInt(getString(key));
    }

}
