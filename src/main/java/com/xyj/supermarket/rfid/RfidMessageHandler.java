package com.xyj.supermarket.rfid;

import com.alibaba.fastjson.JSON;
import com.clou.uhf.G3Lib.Protocol.Tag_Model;
import com.xyj.supermarket.config.Config;
import com.xyj.supermarket.config.MessageFactory;
import com.xyj.supermarket.tcp.TcpServerManager;
import com.xyj.supermarket.util.LoggerUtils;
import org.slf4j.Logger;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;

@Component
public class RfidMessageHandler extends IAsynchronousMessageAdapter {

    private final Logger logger = LoggerUtils.getLogger(this.getClass());

    private long lastTime = System.currentTimeMillis();

    @Resource
    private TcpServerManager tcpServerManager;

    @Override
    public void OutPutTags(Tag_Model tag) {
        logger.debug("读取标签:{}", tag._TID);
        if (StringUtils.hasText(tag._TID)) {
            this.send(MessageFactory.tag(tag._TID));
        }
    }

    @Override
    public void GPIControlMsg(int gpiIndex, int gpiState, int startOrStop) {
        if (gpiState == 1) {
            logger.debug("检测到开门事件");
            long current = System.currentTimeMillis();
            if (current - lastTime > Config.RFID_CLEAN) {
                logger.info("通知清空购物车");
                this.send(MessageFactory.clean());
                this.lastTime = current;
            }
        }
    }

    private void send(Object o) {
        String msg = JSON.toJSONString(o);
        if (tcpServerManager.hasClient()) {
            tcpServerManager.broadcast(msg);
        } else {
            logger.error("当前客户端已断开,忽略本次发送");
        }
    }
}
