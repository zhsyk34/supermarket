package com.xyj.supermarket.udp;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor(staticName = "of")
@Getter
public class NetworkMessage {
    private final String ip;
    private final int port;
}
