package com.xw.rpc.annotation;

import java.lang.annotation.*;

@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Inherited
@Documented
public @interface Service {

    String name() default "";

    String version() default "";

    String group() default "";
}
