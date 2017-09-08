package com.xyj.supermarket.util;

import com.xyj.supermarket.config.Config;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@SuppressWarnings("WeakerAccess")
public abstract class LoggerUtils {

    public static Logger getLogger(String name) {
        return LoggerFactory.getLogger(Config.STORE_NAME + "." + name);
    }

    public static Logger getLogger(Class<?> clazz) {
        return getLogger(clazz.getName());
    }
}
