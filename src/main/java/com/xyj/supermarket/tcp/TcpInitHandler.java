package com.xyj.supermarket.tcp;

import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

@Component
@ChannelHandler.Sharable
final class TcpInitHandler extends ChannelInboundHandlerAdapter {

    @Resource
    private TcpServerManager tcpServerManager;

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        tcpServerManager.accept(ctx.channel());
    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        tcpServerManager.close(ctx.channel());
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        tcpServerManager.error(ctx.channel(), cause);
    }

}
