package com.xyj.supermarket.tcp;

import com.alibaba.fastjson.JSON;
import com.xyj.supermarket.config.Action;
import com.xyj.supermarket.config.Command;
import com.xyj.supermarket.rfid.RfidService;
import com.xyj.supermarket.util.LoggerUtils;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import org.slf4j.Logger;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.Optional;

@Component
@ChannelHandler.Sharable
final class TcpMessageHandler extends ChannelInboundHandlerAdapter {

    private final Logger logger = LoggerUtils.getLogger(this.getClass());
    @Resource
    private RfidService rfidService;

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        if ((msg instanceof String)) {
            String receive = (String) msg;
            logger.debug("接收到客户端请求:{}", receive);

            Command command = JSON.parseObject(receive, Command.class);
            if (command == null) {
                logger.debug("非法的请求数据");
                return;
            }

            Optional.ofNullable(Action.parse(command.getAction())).ifPresent(action -> {
                switch (action) {
                    case ALWAYS_OPEN:
                        logger.info("请求长久开门");
                        rfidService.keepOpen();
                        break;
                    case OPEN_AND_CLOSE:
                        rfidService.openDoor();
                        break;
                    case TCP_HEARTBEAT:
                        ctx.channel().writeAndFlush(receive);
                        break;
                    default:
                        break;
                }
            });
        }
    }
}
