package com.xw.test;

import com.xw.rpc.annotation.Service;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.xw.rpc.api.HelloObject;
import com.xw.rpc.api.HelloService;

@Service
@Slf4j
public class HelloServiceImpl implements HelloService {


    @Override
    public String hello(HelloObject object) {
        log.info("接收到：{}", object.getMessage());
        return "hello: Netty, id=" + object.getId();
    }

}
