package com.xyj.supermarket.tcp;

import io.netty.channel.Channel;
import lombok.NonNull;
import org.springframework.stereotype.Component;

import java.net.InetSocketAddress;

@Component
final class SimpleChannelAccessor implements ChannelAccessor {

    @Override
    public String ip(@NonNull Channel channel) {
        return ((InetSocketAddress) channel.remoteAddress()).getAddress().getHostAddress();
    }

    @Override
    public int port(@NonNull Channel channel) {
        return ((InetSocketAddress) channel.remoteAddress()).getPort();
    }

}
