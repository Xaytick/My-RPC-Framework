package com.xw.rpc.transport.netty.server;

import com.xw.rpc.constant.RpcConstant;
import com.xw.rpc.hook.ShutdownHook;
import com.xw.rpc.provider.ServiceProviderImpl;
import com.xw.rpc.registery.NacosServiceRegistry;
import com.xw.rpc.serializer.CommonSerializer;
import com.xw.rpc.serializer.KryoSerializer;
import com.xw.rpc.transport.AbstractRpcServer;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;
import io.netty.handler.timeout.IdleStateHandler;
import com.xw.rpc.codec.CommonDecoder;
import com.xw.rpc.codec.CommonEncoder;
import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.TimeUnit;

/**
 * NIO方式服务提供侧
 */
@Slf4j
public class NettyServer extends AbstractRpcServer {

    private final CommonSerializer serializer;

    public NettyServer(String host, int port) {
        this(host, port, DEFAULT_SERIALIZER);
    }

    public NettyServer(String host, int port, Integer serializer) {
        this.host = host;
        this.port = port;
        serviceRegistry = new NacosServiceRegistry();
        serviceProvider = new ServiceProviderImpl();
        this.serializer = CommonSerializer.getByCode(serializer);
        scanServices();
    }

    @Override
    public void start() {
        ShutdownHook.getShutdownHook().addClearAllHook();
        EventLoopGroup bossGroup = new NioEventLoopGroup();
        EventLoopGroup workerGroup = new NioEventLoopGroup();
        try {
            ServerBootstrap serverBootstrap = new ServerBootstrap();
            serverBootstrap.group(bossGroup, workerGroup)
                    .channel(NioServerSocketChannel.class)
                    .handler(new LoggingHandler(LogLevel.INFO))
                    .option(ChannelOption.SO_BACKLOG, RpcConstant.BACK_LOG_SIZE)
                    .option(ChannelOption.SO_KEEPALIVE, true)
                    .childOption(ChannelOption.TCP_NODELAY, true)
                    .childHandler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        protected void initChannel(SocketChannel socketChannel) throws Exception {
                            ChannelPipeline channelPipeline = socketChannel.pipeline();
                            channelPipeline.addLast(new CommonEncoder(new KryoSerializer()))
                                    .addLast(new IdleStateHandler(RpcConstant.READER_IDLE_TIME, RpcConstant.WRITER_IDLE_TIME, RpcConstant.ALL_IDLE_TIME, TimeUnit.SECONDS))
                                    .addLast(new CommonDecoder())
                                    .addLast(new NettyServerHandler())
//                                    .addLast(new HeartBeatServerHandler())
                            ;
                        }
                    });
            ChannelFuture channelFuture = serverBootstrap.bind(port).sync();
            channelFuture.channel().closeFuture().sync();
        }
        catch (InterruptedException e) {
            log.error("启动服务器时有错误发生: ", e);
        }
        finally {
            bossGroup.shutdownGracefully();
            workerGroup.shutdownGracefully();
        }
    }


}
