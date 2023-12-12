package com.xw.test;

import com.xw.rpc.annotation.ServiceScan;
import com.xw.rpc.serializer.CommonSerializer;
import com.xw.rpc.transport.netty.server.NettyServer;

/**
 * 测试用Netty服务提供者（服务端）
 */
@ServiceScan
public class NettyTestServer {

    public static void main(String[] args) {
        NettyServer server = new NettyServer("127.0.0.1", 9999, CommonSerializer.KRYO_SERIALIZER);
        server.start();
    }

}
