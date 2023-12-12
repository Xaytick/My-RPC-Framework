package com.xw.rpc.loadbalancer;

import com.alibaba.nacos.api.naming.pojo.Instance;
import com.xw.rpc.entity.RpcRequest;

import java.util.List;

public abstract class AbstractLoadBalancer implements LoadBalancer{

    @Override
    public Instance selectServiceAddress(List<Instance> serviceAddresses, RpcRequest rpcRequest) {
        if (serviceAddresses == null || serviceAddresses.isEmpty()) {
            return null;
        }
        if (serviceAddresses.size() == 1) {
            return serviceAddresses.get(0);
        }
        return select(serviceAddresses, rpcRequest);
    }

    protected abstract Instance select(List<Instance> serviceAddresses, RpcRequest rpcRequest);

}

