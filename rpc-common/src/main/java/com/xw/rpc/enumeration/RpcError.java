package com.xw.rpc.enumeration;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * RPC调用过程中的错误
 */
@AllArgsConstructor
@Getter
public enum RpcError {

    SERVICE_INVOCATION_FAILURE("服务调用失败"),
    SERVICE_NOT_FOUND("找不到对应的服务"),
    SERVICE_NOT_IMPLEMENT_ANY_INTERFACE("注册的服务未实现接口"),
    UNKNOWN_PROTOCOL("不识别的协议包"),
    UNKNOWN_SERIALIZER("不识别的(反)序列化器"),
    UNKNOWN_PACKAGE_TYPE("不识别的数据包类型"),
    FAILED_TO_CONNECT_TO_SERVICE_REGISTRY("连接服务注册器失败"),
    REGISTER_SERVICE_FAILED("注册服务失败"),
    SERIALIZER_NOT_FOUND("未找到序列化器"),
    CLIENT_CONNECT_SERVER_FAILURE("客户端与服务器连接失败"),
    RESPONSE_NOT_MATCH("响应与请求号不匹配"),
    SERVICE_SCAN_PACKAGE_NOT_FOUND("找不到服务扫描包"),
    UNKNOWN_ERROR("未知错误");

    private final String message;

}
