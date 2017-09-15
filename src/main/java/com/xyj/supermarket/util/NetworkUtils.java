package com.xyj.supermarket.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.*;
import java.util.Collections;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Stream;

import static java.util.stream.Collectors.toList;

public abstract class NetworkUtils {

    private static final Logger logger = LoggerFactory.getLogger(NetworkUtils.class);

    public static String getHost() {
        try {
            return InetAddress.getLocalHost().getHostAddress();
        } catch (UnknownHostException e) {
            logger.error("获取网络信息出错", e);
            return null;
        }
    }

    public static List<String> findHosts() {
        try {
            return Collections.list(NetworkInterface.getNetworkInterfaces()).stream()
                    .filter(NetworkUtils::Loopback)
                    .filter(NetworkUtils::isUp)
                    .map(NetworkUtils::mapToAddress)
                    .flatMap(Function.identity())
                    .collect(toList());
        } catch (SocketException e) {
            logger.error("获取网络信息出错", e);
            return null;
        }
    }

    private static Stream<String> mapToAddress(NetworkInterface networkInterface) {
        return Collections.list(networkInterface.getInetAddresses()).stream()
                .filter(address -> !(address instanceof Inet6Address))
                .map(InetAddress::getHostAddress);
    }

    private static boolean Loopback(NetworkInterface networkInterface) {
        try {
            return !networkInterface.isLoopback();
        } catch (SocketException e) {
            return true;
        }
    }

    private static boolean isUp(NetworkInterface networkInterface) {
        try {
            return networkInterface.isUp();
        } catch (SocketException e) {
            return true;
        }
    }
}
