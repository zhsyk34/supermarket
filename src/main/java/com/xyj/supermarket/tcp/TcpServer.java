package com.xyj.supermarket.tcp;

import com.xyj.supermarket.config.AbstractDaemonService;
import com.xyj.supermarket.config.Config;
import com.xyj.supermarket.util.LoggerUtils;
import com.xyj.supermarket.util.NetworkUtils;
import com.xyj.supermarket.util.ThreadUtils;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.buffer.PooledByteBufAllocator;
import io.netty.channel.Channel;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;
import org.slf4j.Logger;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import java.util.List;

@Service
public final class TcpServer extends AbstractDaemonService {

    private final Logger logger = LoggerUtils.getLogger(this.getClass());

    @Resource
    private TcpInitHandler tcpInitHandler;
    @Resource
    private TcpMessageHandler tcpMessageHandler;

    @Override
    public void run() {
        multiNetwork();
    }

    private void init(String host) throws Exception {
        ServerBootstrap bootstrap = new ServerBootstrap();

        EventLoopGroup mainGroup = new NioEventLoopGroup();
        EventLoopGroup handleGroup = new NioEventLoopGroup();

        bootstrap.group(mainGroup, handleGroup).channel(NioServerSocketChannel.class);

        //setting options
        bootstrap.option(ChannelOption.SO_KEEPALIVE, true);
        bootstrap.option(ChannelOption.TCP_NODELAY, true);
        bootstrap.option(ChannelOption.SO_BACKLOG, Config.TCP_BACKLOG);
        bootstrap.option(ChannelOption.CONNECT_TIMEOUT_MILLIS, Config.TCP_TIMEOUT);

        //pool
        bootstrap.option(ChannelOption.ALLOCATOR, PooledByteBufAllocator.DEFAULT);
        bootstrap.childOption(ChannelOption.ALLOCATOR, PooledByteBufAllocator.DEFAULT);

        //logging
        bootstrap.childHandler(new LoggingHandler(LogLevel.WARN));

        //handler
        bootstrap.childHandler(new ChannelInitializer<Channel>() {
            @Override
            protected void initChannel(Channel ch) throws Exception {
                ch.pipeline().addLast(new TcpDecoder(), new TcpEncoder(), tcpInitHandler, tcpMessageHandler);
            }
        });

        try {
            Channel channel = bootstrap.bind(host, Config.TCP_PORT).sync().channel();

            super.setStartup(true);
            Config.TCP_HOST = host;

            logger.info("{}[{}:{}]启动成功", this.getClass().getSimpleName(), host, Config.TCP_PORT);

            channel.closeFuture().sync();
        } finally {
            mainGroup.shutdownGracefully();
            handleGroup.shutdownGracefully();
        }
    }

    private void multiNetwork() {
        List<String> hosts = NetworkUtils.findHosts();

        while (CollectionUtils.isEmpty(hosts)) {
            logger.error("正在获取网络信息...");
            ThreadUtils.await(2000L);
            hosts = NetworkUtils.findHosts();
        }

        for (String host : hosts) {
            try {
                init(host);

                if (super.isStartup()) {
                    break;
                }
            } catch (Exception e) {
                logger.error("{} 在[{}]启动失败", this.getClass().getSimpleName(), host, e);
            }
        }

        if (!super.isStartup()) {
            ThreadUtils.await(1000L);

            logger.error("尝试重新启动...");
            this.run();
        }
    }
}
