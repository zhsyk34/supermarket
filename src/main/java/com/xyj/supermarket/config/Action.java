package com.xyj.supermarket.config;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public enum Action {
    ALWAYS_OPEN(0),//长久开门
    OPEN_AND_CLOSE(1),//开关门
    CLEAN_CART(2),//清空购物车
    NETWORK_REQUEST(3),//UDP请求
    TCP_HEARTBEAT(4);//TCP心跳

    private final int type;

    public static Action parse(int type) {
        for (Action action : values()) {
            if (action.type == type) {
                return action;
            }
        }
        return null;
    }
}
