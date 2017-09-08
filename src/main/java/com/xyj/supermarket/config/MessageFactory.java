package com.xyj.supermarket.config;

import com.xyj.supermarket.rfid.TagMessage;
import com.xyj.supermarket.udp.NetworkMessage;

public abstract class MessageFactory {

    public static Command clean() {
        return Command.of(Action.CLEAN_CART);
    }

    public static TagMessage tag(String tid) {
        return TagMessage.of(tid);
    }

    public static NetworkMessage networkMessage(String ip, int port) {
        return NetworkMessage.of(ip, port);
    }

}
