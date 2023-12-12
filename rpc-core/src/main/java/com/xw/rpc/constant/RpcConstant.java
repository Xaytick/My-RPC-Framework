package com.xw.rpc.constant;

public class RpcConstant {

    public static final int MAGIC_NUMBER = 0xCAFEBABE;

    public static final String SERVER_ADDR = "127.0.0.1:8848";

    public static final int CONNECT_TIME_OUT = 5000;

    public static final int READER_IDLE_TIME = 5;

    public static final int WRITER_IDLE_TIME = 5;

    public static final int ALL_IDLE_TIME = 5;

    public static final int MAX_RETRY_COUNT = 5;

    public static final int BACK_LOG_SIZE = 256;

    public static final String SPRING_BEAN_BASE_PACKAGE = "com.xw.rpc";

    public static final String BASE_PACKAGE_ATTRIBUTE_NAME = "basePackage";
}
