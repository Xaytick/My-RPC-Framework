package com.xw.rpc.transport.netty.client;

import com.xw.rpc.entity.RpcRequest;
import com.xw.rpc.entity.RpcResponse;
import com.xw.rpc.enumeration.RpcError;
import com.xw.rpc.exception.RpcException;
import com.xw.rpc.factory.SingletonFactory;
import com.xw.rpc.loadbalancer.LoadBalancer;
import com.xw.rpc.loadbalancer.RandomLoadBalancer;
import com.xw.rpc.registery.NacosServiceDiscovery;
import com.xw.rpc.registery.ServiceDiscovery;
import com.xw.rpc.serializer.CommonSerializer;
import com.xw.rpc.transport.RpcClient;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.InetSocketAddress;
import java.util.concurrent.CompletableFuture;

/**
 * NIO方式消费侧客户端类
 */
@Slf4j
public class NettyClient implements RpcClient {

    private static final EventLoopGroup group;
    private static final Bootstrap bootstrap;

    static {
        group = new NioEventLoopGroup();
        bootstrap = new Bootstrap();
        bootstrap.group(group)
                .channel(NioSocketChannel.class);
    }

    private final ServiceDiscovery serviceDiscovery;
    private final CommonSerializer serializer;

    private final UnprocessedRequests unprocessedRequests;

    public NettyClient() {
        this(DEFAULT_SERIALIZER, new RandomLoadBalancer());
    }
    public NettyClient(LoadBalancer loadBalancer) {
        this(DEFAULT_SERIALIZER, loadBalancer);
    }
    public NettyClient(Integer serializer) {
        this(serializer, new RandomLoadBalancer());
    }
    public NettyClient(Integer serializer, LoadBalancer loadBalancer) {
        this.serviceDiscovery = new NacosServiceDiscovery(loadBalancer);
        this.serializer = CommonSerializer.getByCode(serializer);
        this.unprocessedRequests = SingletonFactory.getInstance(UnprocessedRequests.class);
    }

    @Override
    public CompletableFuture<RpcResponse> sendRequest(RpcRequest rpcRequest) {
        if (serializer == null) {
            log.error("未设置序列化器");
            throw new RpcException(RpcError.SERIALIZER_NOT_FOUND);
        }
        CompletableFuture<RpcResponse> resultFuture = new CompletableFuture<>();
        InetSocketAddress inetSocketAddress = serviceDiscovery.lookupService(rpcRequest);
        Channel channel = ChannelProvider.get(inetSocketAddress, serializer);
        if (!channel.isActive()) {
            group.shutdownGracefully();
            return null;
        }
        unprocessedRequests.put(rpcRequest.getRequestId(), resultFuture);
        channel.writeAndFlush(rpcRequest).addListener((ChannelFutureListener) future1 -> {
            if (future1.isSuccess()) {
                log.info(String.format("客户端发送消息: %s", rpcRequest.toString()));
            } else {
                future1.channel().close();
                resultFuture.completeExceptionally(future1.cause());
                log.error("发送消息时有错误发生: ", future1.cause());
            }
        });
        return resultFuture;
    }

}