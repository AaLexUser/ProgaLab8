package com.lapin.di.annotation;

import com.lapin.di.context.AbstractBean;
import com.lapin.di.context.ApplicationContext;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface Inject {
    boolean singleton() default true;
}
