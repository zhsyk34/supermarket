package com.xyj.supermarket.tcp;

import com.xyj.supermarket.config.ProtocolConfig;
import com.xyj.supermarket.util.ByteUtils;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;
import io.netty.util.CharsetUtil;

import java.util.List;

final class TcpDecoder extends ByteToMessageDecoder {

    @Override
    protected void decode(ChannelHandlerContext ctx, ByteBuf in, List<Object> out) throws Exception {
        final int size = in.readableBytes();

        //validate min data
        if (size < ProtocolConfig.MIN_DATA_BYTES + ProtocolConfig.REDUNDANT_BYTES) {
            return;
        }

        //validate buffer
        if (size > ProtocolConfig.MAX_BUFFER_BYTES) {
            in.clear();
            return;
        }

        in.markReaderIndex();

        //header
        byte[] header = ByteUtils.read(in, ProtocolConfig.HEADERS_BYTES);
        if (!ByteUtils.compareByteArray(header, ProtocolConfig.HEADERS)) {
            in.clear();
            return;
        }

        //length
        int length = ByteUtils.byteArrayToInt(ByteUtils.read(in, ProtocolConfig.LENGTH_BYTES));

        if (in.readableBytes() < length + ProtocolConfig.FOOTERS_BYTES) {
            in.resetReaderIndex();
            return;
        }

        //data
        byte[] data = ByteUtils.read(in, length);

        //footer
        byte[] footer = ByteUtils.read(in, ProtocolConfig.FOOTERS_BYTES);
        if (!ByteUtils.compareByteArray(footer, ProtocolConfig.FOOTERS)) {
            in.clear();
            return;
        }

        //fire data
        out.add(new String(data, CharsetUtil.UTF_8));

        //recursion
        if (in.readableBytes() > 0) {
            decode(ctx, in, out);
        }
    }

}
