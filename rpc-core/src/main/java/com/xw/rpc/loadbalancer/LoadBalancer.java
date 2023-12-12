package com.xw.rpc.loadbalancer;

import com.alibaba.nacos.api.naming.pojo.Instance;
import com.xw.rpc.entity.RpcRequest;

import java.util.List;

public interface LoadBalancer {

    Instance selectServiceAddress(List<Instance> instanceList, RpcRequest rpcRequest);
}
