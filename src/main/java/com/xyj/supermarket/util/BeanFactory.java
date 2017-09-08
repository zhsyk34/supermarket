package com.xyj.supermarket.util;

import com.xyj.supermarket.config.SpringConfig;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

public abstract class BeanFactory {

    private static final AnnotationConfigApplicationContext CONTEXT = new AnnotationConfigApplicationContext(SpringConfig.class);

    public static <T> T getBean(Class<T> clazz) {
        return CONTEXT.getBean(clazz);
    }

    public static void destroy() {
        CONTEXT.close();
    }
}
