package com.xyj.supermarket;

import com.xyj.supermarket.rfid.RfidService;
import com.xyj.supermarket.tcp.TcpServer;
import com.xyj.supermarket.udp.UdpServer;
import com.xyj.supermarket.util.BeanFactory;
import com.xyj.supermarket.util.LoggerUtils;
import org.slf4j.Logger;

public class Entry {

    private static final Logger logger = LoggerUtils.getLogger(Entry.class);

    public static void main(String[] args) {
        BeanFactory.getBean(TcpServer.class).startup();

        BeanFactory.getBean(UdpServer.class).startup();

        BeanFactory.getBean(RfidService.class).startup();

        BeanFactory.destroy();

        logger.info("服务器已启动完毕");
    }

}
