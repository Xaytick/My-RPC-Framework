package com.xw.rpc.registery;

import com.alibaba.nacos.api.exception.NacosException;
import com.alibaba.nacos.api.naming.NamingFactory;
import com.alibaba.nacos.api.naming.NamingService;
import com.alibaba.nacos.api.naming.pojo.Instance;
import com.xw.rpc.constant.RpcConstant;
import com.xw.rpc.enumeration.RpcError;
import com.xw.rpc.exception.RpcException;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.InetSocketAddress;
import java.util.List;

@Slf4j
public class NacosServiceRegistry implements ServiceRegistry{

    private static final NamingService namingService;

    static {
        try {
            namingService = NamingFactory.createNamingService(RpcConstant.SERVER_ADDR);
        }
        catch (NacosException e) {
            log.error("ERROR DURING CONNECTING TO NACOS: ", e);
            throw new RpcException(RpcError.FAILED_TO_CONNECT_TO_SERVICE_REGISTRY);
        }
    }

    @Override
    public void register(String serviceName, InetSocketAddress inetSocketAddress) {
        try {
            namingService.registerInstance(serviceName, inetSocketAddress.getHostName(), inetSocketAddress.getPort());
        }
        catch (NacosException e) {
            log.error("ERROR DURING REGISTERING SERVICE: ", e);
            throw new RpcException(RpcError.REGISTER_SERVICE_FAILED);
        }
    }

    @Override
    public InetSocketAddress lookupService(String serviceName) {
        try {
            List<Instance> instanceList = namingService.getAllInstances(serviceName);
            Instance instance = instanceList.get(0);
            return new InetSocketAddress(instance.getIp(), instance.getPort());
        }
        catch (NacosException e) {
            log.error("ERROR DURING LOOKING UP SERVICE", e);
        }
        return null;
    }
}
