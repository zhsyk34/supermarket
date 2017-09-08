package com.xyj.supermarket.udp;

import com.alibaba.fastjson.JSON;
import com.xyj.supermarket.config.Action;
import com.xyj.supermarket.config.Command;
import com.xyj.supermarket.config.Config;
import com.xyj.supermarket.config.MessageFactory;
import com.xyj.supermarket.util.ByteUtils;
import com.xyj.supermarket.util.LoggerUtils;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.channel.socket.DatagramPacket;
import io.netty.util.CharsetUtil;
import org.slf4j.Logger;
import org.springframework.stereotype.Component;

import java.net.InetSocketAddress;

@Component
final class UdpHandler extends SimpleChannelInboundHandler<DatagramPacket> {

    private final Logger logger = LoggerUtils.getLogger(this.getClass());

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, DatagramPacket msg) throws Exception {
        InetSocketAddress address = msg.sender();

        String receive = new String(ByteUtils.read(msg.content()), CharsetUtil.UTF_8);

        logger.debug("接收到客户端[{}] UDP 请求:{}", address, receive);

        Command command = JSON.parseObject(receive, Command.class);
        if (command != null && Action.parse(command.getAction()) == Action.NETWORK_REQUEST) {
            logger.debug("响应客户端的网络信息请求");
            NetworkMessage networkMessage = MessageFactory.networkMessage(Config.TCP_HOST, Config.TCP_PORT);
            ByteBuf buffer = Unpooled.wrappedBuffer(JSON.toJSONString(networkMessage).getBytes(CharsetUtil.UTF_8));
            ctx.writeAndFlush(new DatagramPacket(buffer, address));
        }
    }
}
