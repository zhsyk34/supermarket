package com.xyj.supermarket.udp;

import com.xyj.supermarket.config.AbstractDaemonService;
import com.xyj.supermarket.config.Config;
import com.xyj.supermarket.util.LoggerUtils;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.DatagramChannel;
import io.netty.channel.socket.nio.NioDatagramChannel;
import org.slf4j.Logger;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

@Service
public final class UdpServer extends AbstractDaemonService {

    private final Logger logger = LoggerUtils.getLogger(this.getClass());

    @Resource
    private UdpHandler udpHandler;

    @Override
    public void run() {
        Bootstrap bootstrap = new Bootstrap();
        EventLoopGroup group = new NioEventLoopGroup();
        try {
            bootstrap.group(group).channel(NioDatagramChannel.class);
            bootstrap.option(ChannelOption.SO_BROADCAST, true);
            bootstrap.handler(new ChannelInitializer<DatagramChannel>() {
                @Override
                protected void initChannel(DatagramChannel ch) throws Exception {
                    ch.pipeline().addLast(udpHandler);
                }
            });

            Channel channel = bootstrap.bind(Config.UDP_PORT).syncUninterruptibly().channel();

            logger.info("{}[{}:{}]启动成功", this.getClass().getSimpleName(), Config.TCP_HOST, Config.UDP_PORT);
            super.setStartup(true);

            channel.closeFuture().await();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            group.shutdownGracefully();
        }
    }
}
