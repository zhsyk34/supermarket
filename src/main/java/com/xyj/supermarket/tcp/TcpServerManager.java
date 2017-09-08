package com.xyj.supermarket.tcp;

import com.xyj.supermarket.util.LoggerUtils;
import io.netty.channel.Channel;
import lombok.NonNull;
import org.slf4j.Logger;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.LinkedHashSet;
import java.util.Set;

@Service
public class TcpServerManager {

    private final Logger logger = LoggerUtils.getLogger(TcpServerManager.class);

    private final Set<Channel> clients = new LinkedHashSet<>();

    @Resource
    private ChannelAccessor channelAccessor;

    public boolean hasClient() {
        return !clients.isEmpty();
    }

    void accept(@NonNull Channel channel) {
        String ip = channelAccessor.ip(channel);
        int port = channelAccessor.port(channel);

        logger.info("客户端[{}:{}]发起连接", ip, port);

        clients.forEach(c -> {
            if (ip.equals(channelAccessor.ip(c))) {
                logger.error("关闭重复连接 {}:{}", ip, port);
                clients.remove(c);
            }
        });

        clients.add(channel);
    }

    void close(@NonNull Channel channel) {
        logger.info("客户端[{}:{}]失去连接", channelAccessor.ip(channel), channelAccessor.port(channel));
        clients.remove(channel);
    }

    void error(@NonNull Channel channel, Throwable cause) throws Exception {
        logger.error("客户端[{}:{}]出错", channelAccessor.ip(channel), channelAccessor.port(channel), cause);
    }

    public void broadcast(String msg) {
        logger.debug("准备向客户端广播信息:{}", msg);
        logger.debug("当前共有[{}]客户端", clients.size());
        clients.forEach(c -> logger.debug("[{}:{}]", channelAccessor.ip(c), channelAccessor.port(c)));
        clients.stream().filter(Channel::isOpen).forEach(channel -> channel.writeAndFlush(msg));
    }
}
