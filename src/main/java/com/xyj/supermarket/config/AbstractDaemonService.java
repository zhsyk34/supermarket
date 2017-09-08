package com.xyj.supermarket.config;

import com.xyj.supermarket.util.ThreadUtils;
import lombok.Data;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Data
public abstract class AbstractDaemonService implements DaemonService {

    private static final ExecutorService EXECUTOR_SERVICE = Executors.newCachedThreadPool(r -> {
        Thread thread = new Thread(r);
        thread.setDaemon(true);
        return thread;
    });

    private volatile boolean startup = false;

    @Override
    public void startup() {
        EXECUTOR_SERVICE.submit(this);
        while (!this.isStartup()) {
            ThreadUtils.await(500);
        }
    }

}
