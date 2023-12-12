package com.xw.rpc.loadbalancer;

import com.alibaba.nacos.api.naming.pojo.Instance;
import com.xw.rpc.entity.RpcRequest;

import java.util.List;
import java.util.Random;

public class RandomLoadBalancer extends AbstractLoadBalancer{
    @Override
    public Instance select(List<Instance> instanceList, RpcRequest rpcRequest) {
        return instanceList.get(new Random().nextInt(instanceList.size()));
    }
}
