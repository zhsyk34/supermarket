package com.xyj.supermarket.config;

public interface DaemonService extends Runnable {

    boolean isStartup();

    void startup();
}
