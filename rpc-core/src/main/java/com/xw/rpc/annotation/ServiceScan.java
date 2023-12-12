package com.xw.rpc.annotation;

import com.xw.rpc.spring.CustomScannerRegistrar;
import org.springframework.context.annotation.Import;

import java.lang.annotation.*;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
@Import(CustomScannerRegistrar.class)
@Documented
public @interface ServiceScan {

    public String value() default "";
}
