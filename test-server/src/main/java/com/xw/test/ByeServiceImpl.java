package com.xw.test;

import com.xw.rpc.annotation.Service;
import com.xw.rpc.api.ByeService;


@Service
public class ByeServiceImpl implements ByeService {

    @Override
    public String bye(String name) {
        return "bye: " + name;
    }
}
