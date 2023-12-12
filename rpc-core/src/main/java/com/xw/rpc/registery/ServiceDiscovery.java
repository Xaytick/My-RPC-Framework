package com.xw.rpc.registery;

import com.xw.rpc.entity.RpcRequest;

import java.net.InetSocketAddress;

public interface ServiceDiscovery {

    InetSocketAddress lookupService(RpcRequest rpcRequest);
}
