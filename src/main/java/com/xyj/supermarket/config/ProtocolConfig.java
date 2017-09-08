package com.xyj.supermarket.config;

import java.nio.charset.StandardCharsets;

public class ProtocolConfig {
    //header
    public static final byte[] HEADERS = "^^^".getBytes(StandardCharsets.UTF_8);
    //header length
    public static final int HEADERS_BYTES = HEADERS.length;
    //footer
    public static final byte[] FOOTERS = "$$$".getBytes(StandardCharsets.UTF_8);
    //footer length
    public static final int FOOTERS_BYTES = FOOTERS.length;
    //length length
    public static final int LENGTH_BYTES = 2;
    //冗余数据长度
    public static final int REDUNDANT_BYTES = HEADERS_BYTES + LENGTH_BYTES + FOOTERS_BYTES;
    //min data length
    public static final int MIN_DATA_BYTES = 1;
    //max buffer length
    public static final int MAX_BUFFER_BYTES = 1 << 10;
}
