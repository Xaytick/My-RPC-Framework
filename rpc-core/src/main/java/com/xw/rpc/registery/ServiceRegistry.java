package com.xw.rpc.registery;

import java.net.InetSocketAddress;

public interface ServiceRegistry {

    void register(String serviceName, InetSocketAddress inetSocketAddress);
    InetSocketAddress lookupService(String serviceName);
}
