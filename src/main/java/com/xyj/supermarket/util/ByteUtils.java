package com.xyj.supermarket.util;

import io.netty.buffer.ByteBuf;

@SuppressWarnings({"WeakerAccess", "unused"})
public abstract class ByteUtils {

    private static final int MARK = 0xff;

    public static int byteArrayToInt(byte[] bytes) {
        if (bytes == null || bytes.length == 0) {
            throw new RuntimeException("byte array is isEmpty.");
        }
        if (bytes.length > 4) {
            throw new RuntimeException("byte array length too long.");
        }
        int result = 0, offset = 0;
        for (int i = bytes.length - 1; i >= 0; i--) {
            result |= (bytes[i] & MARK) << (offset++ << 3);
        }
        return result;
    }

    private static byte[] intToByteArray(int i, int length) {
        if (length < 0 || length > 4) {
            throw new RuntimeException("integer range 1 - 4 bit.");
        }

        byte[] bytes = new byte[length];
        for (int k = 0; k < length; k++) {
            bytes[k] = (byte) (i >> ((length - 1 - k) << 3) & MARK);
        }
        return bytes;
    }

    public static byte[] intToByteArray(int i) {
        return intToByteArray(i, 4);
    }

    public static byte[] smallIntToByteArray(int i) {
        return intToByteArray(i, 2);
    }

    public static byte[] tinyToByteArray(int i) {
        return intToByteArray(i, 1);
    }

    public static boolean compareByteArray(byte[] src, byte[] dest) {
        if (src == null || dest == null) {
            return src == dest;
        }

        if (src.length != dest.length) {
            return false;
        }

        for (int i = 0; i < src.length; i++) {
            if (src[i] != dest[i]) {
                return false;
            }
        }
        return true;
    }

    public static byte[] read(ByteBuf in, int size) {
        byte[] result = new byte[size];
        in.readBytes(size).getBytes(0, result).release();
        return result;
    }

    public static byte[] read(ByteBuf in) {
        return read(in, in.readableBytes());
    }

}
