package com.xw.rpc.registery;

import com.alibaba.nacos.api.exception.NacosException;
import com.alibaba.nacos.api.naming.pojo.Instance;
import com.xw.rpc.entity.RpcRequest;
import com.xw.rpc.enumeration.RpcError;
import com.xw.rpc.exception.RpcException;
import com.xw.rpc.loadbalancer.LoadBalancer;
import com.xw.rpc.loadbalancer.RandomLoadBalancer;
import com.xw.rpc.util.NacosUtil;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.InetSocketAddress;
import java.util.List;

@Slf4j
public class NacosServiceDiscovery implements ServiceDiscovery{

    private final LoadBalancer loadBalancer;

    public NacosServiceDiscovery (LoadBalancer loadBalancer) {
        if (loadBalancer == null) {
            this.loadBalancer = new RandomLoadBalancer();
        }
        else {
            this.loadBalancer = loadBalancer;
        }
    }

    @Override
    public InetSocketAddress lookupService(RpcRequest rpcRequest) {
        try {
            String serviceName = rpcRequest.getInterfaceName();
            List<Instance> instanceList = NacosUtil.getAllInstance(serviceName);
            if(instanceList.size() == 0) {
                log.error("找不到对应的服务: " + serviceName);
                throw new RpcException(RpcError.SERVICE_NOT_FOUND);
            }
            Instance instance = loadBalancer.selectServiceAddress(instanceList, rpcRequest);
            return new InetSocketAddress(instance.getIp(), instance.getPort());
        }
        catch (NacosException e) {
            log.error("ERROR DURING LOOKING UP SERVICE: ", e);
        }
        return null;
    }
}
