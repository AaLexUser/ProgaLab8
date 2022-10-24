package com.lapin.di.context;

import com.lapin.di.annotation.Inject;

public class AbstractBean<T> {
    private final Class<T> clazz;
    private Class<?>[] parameterTypes;
    private Object[] initargs;

    public AbstractBean(Class<T> clazz){
        this.clazz = clazz;
        Inject inject = clazz.getAnnotation(Inject.class);
        if (inject!= null){

        }
    }

    public AbstractBean setParameterTypes(Class<?> ... parameterTypes) {
        this.parameterTypes = parameterTypes;
        return this;
    }

    public AbstractBean setInitargs(Object ... initargs) {
        this.initargs = initargs;
        return this;
    }

    public Class<?>[] getParameterTypes() {
        return parameterTypes;
    }

    public Object[] getInitargs() {
        return initargs;
    }

    public Class<T> getClazz() {
        return clazz;
    }
}
