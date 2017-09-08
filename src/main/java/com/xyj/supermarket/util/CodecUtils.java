package com.xyj.supermarket.util;

import com.xyj.supermarket.config.ProtocolConfig;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.util.CharsetUtil;

public abstract class CodecUtils {

    public static byte[] encode(String input) {
        if (input == null || input.isEmpty()) {
            throw new RuntimeException("input data is isEmpty.");
        }

        byte[] data = input.getBytes(CharsetUtil.UTF_8);

        ByteBuf buffer = Unpooled.buffer(data.length + ProtocolConfig.REDUNDANT_BYTES);

        //header
        buffer.writeBytes(ProtocolConfig.HEADERS);
        //length
        buffer.writeBytes(ByteUtils.smallIntToByteArray(data.length));
        //data
        buffer.writeBytes(data);
        //footer
        buffer.writeBytes(ProtocolConfig.FOOTERS);

        return buffer.array();
    }

}
