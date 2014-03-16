package com.abillist.sysweb.interceptor.anno

import java.lang.annotation.Documented
import java.lang.annotation.ElementType
import java.lang.annotation.Retention
import java.lang.annotation.RetentionPolicy
import java.lang.annotation.Target

/**
 * Created by shao on 14-3-15.
 */
@Retention(RetentionPolicy.RUNTIME)
@Target([ElementType.TYPE, ElementType.METHOD])
@Documented
@interface PathCheck {
    String[] exists() default ["path"];

    String[] notExists() default [];

    String[] beFile() default [];

    String[] beDirectory() default [];
}
