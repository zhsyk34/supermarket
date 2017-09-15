package com.xyj.supermarket.config;

import com.xyj.supermarket.util.PropUtils;

public abstract class Config {

    public static final int TCP_PORT = 9999;
    public static final int TCP_BACKLOG = 500;
    public static final int TCP_TIMEOUT = 3000;
    public static final int UDP_PORT = 9998;
    public static String STORE_NAME;
    /*----------------------------network-config----------------------------*/
    public static String TCP_HOST;
    /*----------------------------rfid-config----------------------------*/
    public static String RFID_SN;
    public static int RFID_ANT;
    public static int RFID_SYNC;
    public static int RFID_CLEAN;

    static {
        STORE_NAME = PropUtils.getString("store.name");

        RFID_SN = PropUtils.getString("rfid.sn");
        RFID_ANT = PropUtils.getInt("rfid.ant");
        RFID_CLEAN = PropUtils.getInt("rfid.clean") * 1000;
        RFID_SYNC = PropUtils.getInt("rfid.sync") * 1000;
    }

}
