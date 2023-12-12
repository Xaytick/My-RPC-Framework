package com.xw.test;

import com.xw.rpc.api.ByeService;
import com.xw.rpc.serializer.CommonSerializer;
import com.xw.rpc.transport.RpcClient;
import com.xw.rpc.transport.RpcClientProxy;
import com.xw.rpc.api.HelloObject;
import com.xw.rpc.api.HelloService;
import com.xw.rpc.transport.netty.client.NettyClient;

/**
 * 测试用Netty消费者
 */
public class NettyTestClient {

    public static void main(String[] args) {
        RpcClient client = new NettyClient(CommonSerializer.KRYO_SERIALIZER);
        RpcClientProxy rpcClientProxy = new RpcClientProxy(client);
        HelloService helloService = rpcClientProxy.getProxy(HelloService.class);
        HelloObject object = new HelloObject(123, "This is a message");
        System.out.println(helloService.hello(object));
        ByeService byeService = rpcClientProxy.getProxy(ByeService.class);
        System.out.println(byeService.bye("Netty"));
    }

}
