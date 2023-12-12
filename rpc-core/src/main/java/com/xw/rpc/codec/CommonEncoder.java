package com.xw.rpc.codec;

import com.xw.rpc.constant.RpcConstant;
import com.xw.rpc.entity.RpcRequest;
import com.xw.rpc.enumeration.PackageType;
import com.xw.rpc.serializer.CommonSerializer;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;

/**
 * 通用的编码拦截器
 * 字段	解释
 * Magic Number	魔数，表识一个 MRF 协议包，0xCAFEBABE
 * Package Type	包类型，标明这是一个调用请求还是调用响应
 * Serializer Type	序列化器类型，标明这个包的数据的序列化方式
 * Data Length	数据字节的长度
 * Data Bytes	传输的对象，通常是一个RpcRequest或RpcClient对象，取决于Package Type字段，对象的序列化方式取决于Serializer Type字段。
 */
public class CommonEncoder extends MessageToByteEncoder {

    private final CommonSerializer serializer;

    public CommonEncoder(CommonSerializer serializer) {
        this.serializer = serializer;
    }

    @Override
    protected void encode(ChannelHandlerContext ctx, Object msg, ByteBuf out) throws Exception {
        out.writeInt(RpcConstant.MAGIC_NUMBER);
        if(msg instanceof RpcRequest) {
            out.writeInt(PackageType.REQUEST_PACK.getCode());
        } else {
            out.writeInt(PackageType.RESPONSE_PACK.getCode());
        }
        out.writeInt(serializer.getCode());
        byte[] bytes = serializer.serialize(msg);
        out.writeInt(bytes.length);
        out.writeBytes(bytes);
    }

}
