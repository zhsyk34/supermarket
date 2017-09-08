package com.xyj.supermarket.config;

import com.xyj.supermarket.Entry;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

@Configuration
@ComponentScan(basePackageClasses = Entry.class)
public class SpringConfig {
}
