//package com.xw.rpc.loadbalancer;
//
//import com.alibaba.nacos.api.exception.NacosException;
//import com.alibaba.nacos.api.naming.pojo.Instance;
//import com.xw.rpc.entity.RpcRequest;
//import com.xw.rpc.util.NacosUtil;
//
//import java.nio.charset.StandardCharsets;
//import java.security.MessageDigest;
//import java.security.NoSuchAlgorithmException;
//import java.util.*;
//import java.util.concurrent.ConcurrentHashMap;
//
//public class ConsistentHashLoadBalance extends AbstractLoadBalancer{
//
//    private final ConcurrentHashMap<Instance, ConsistentHashSelector> selectors = new ConcurrentHashMap<>();
//
//    static class ConsistentHashSelector {
//        private final TreeMap<Long, String> virtualInvokers;
//
//        private final int identityHashCode;
//
//        ConsistentHashSelector(List<String> invokers, int replicaNumber, int identityHashCode) {
//            this.virtualInvokers = new TreeMap<>();
//            this.identityHashCode = identityHashCode;
//
//            for (String invoker : invokers) {
//                for (int i = 0; i < replicaNumber / 4; i++) {
//                    byte[] digest = md5(invoker + i);
//                    for (int h = 0; h < 4; h++) {
//                        long m = hash(digest, h);
//                        virtualInvokers.put(m, invoker);
//                    }
//                }
//            }
//        }
//
//        static byte[] md5(String key) {
//            MessageDigest md;
//            try {
//                md = MessageDigest.getInstance("MD5");
//                byte[] bytes = key.getBytes(StandardCharsets.UTF_8);
//                md.update(bytes);
//            } catch (NoSuchAlgorithmException e) {
//                throw new IllegalStateException(e.getMessage(), e);
//            }
//
//            return md.digest();
//        }
//
//        static long hash(byte[] digest, int idx) {
//            return ((long) (digest[3 + idx * 4] & 255) << 24 | (long) (digest[2 + idx * 4] & 255) << 16 | (long) (digest[1 + idx * 4] & 255) << 8 | (long) (digest[idx * 4] & 255)) & 4294967295L;
//        }
//
//        public String select(String rpcServiceKey) {
//            byte[] digest = md5(rpcServiceKey);
//            return selectForKey(hash(digest, 0));
//        }
//
//        public String selectForKey(long hashCode) {
//            Map.Entry<Long, String> entry = virtualInvokers.tailMap(hashCode, true).firstEntry();
//
//            if (entry == null) {
//                entry = virtualInvokers.firstEntry();
//            }
//
//            return entry.getValue();
//        }
//    }
//
//    @Override
//    public Instance select(List<Instance> instanceList, RpcRequest rpcRequest) {
//        int identityHashCode = System.identityHashCode(instanceList);
//        List<String> stringList = new ArrayList<>();
//        for (Instance i : instanceList) {
//             stringList.add(i.getServiceName());
//        }
//        String serviceName = rpcRequest.getInterfaceName();
//        ConsistentHashSelector selector = selectors.get(serviceName);
//        if (selector == null || selector.identityHashCode != identityHashCode) {
//            Instance instance = new Instance();
//            instance.setServiceName(serviceName);
//
//            selectors.put(instance, new ConsistentHashSelector(stringList, 160, identityHashCode));
//            selector = selectors.get(serviceName);
//        }
//        String res = serviceName + Arrays.stream(rpcRequest.getParameters());
//        return selector.select(res);
//    }
//}
