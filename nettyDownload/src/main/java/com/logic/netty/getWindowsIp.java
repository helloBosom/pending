package com.logic.netty;

import java.net.InetAddress;
import java.net.UnknownHostException;

public class getWindowsIp {
    public static String getLocalIp() throws UnknownHostException {
        return InetAddress.getLocalHost().getHostAddress();
    }
}
