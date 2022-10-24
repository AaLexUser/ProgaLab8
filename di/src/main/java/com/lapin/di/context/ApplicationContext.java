package com.lapin.di.context;

import com.lapin.di.annotation.Inject;
import com.lapin.di.config.Configuration;
import com.lapin.di.factory.BeanFactory;

import com.lapin.di.postprocessor.BeanPostProcessor;
import lombok.Getter;
import lombok.Setter;


import java.lang.reflect.InvocationTargetException;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Обёртка над BeanFactory, контролирует жизненный цикл бинов
 */
public class ApplicationContext {
    private static final ApplicationContext APPLICATION_CONTEXT = new ApplicationContext();
    private final Map<Class, Object> beanMap = new ConcurrentHashMap<>();
    @Setter
    @Getter
    private BeanFactory beanFactory;
    private boolean singleton = true;

    public ApplicationContext() {
    }

    public static ApplicationContext getInstance() {
        return APPLICATION_CONTEXT;
    }
    public <T> T getBean(AbstractBean<T> abstractBean){
        return getBean(abstractBean, singleton,true);
    }
    public <T> T getBean(Class<T> clazz) {
        return getBean(new AbstractBean<>(clazz));
    }
    public <T> T getBean(AbstractBean<T> abstractBean, boolean singleton, boolean postprocessor) {
        if (beanMap.containsKey(abstractBean.getClazz())) {
            Inject inject = abstractBean.getClazz().getAnnotation(Inject.class);
            if (singleton || (inject != null && !inject.singleton())) {
                T bean = (T) beanMap.get(abstractBean.getClazz());
                if(postprocessor) {
                    callPostProcessor(bean);
                }
                return bean;
            }


        }
        T bean = beanFactory.getBean(abstractBean);
        if(postprocessor) {
            callPostProcessor(bean);
        }

        beanMap.put(abstractBean.getClazz(), bean);
        return bean;
    }

    public <T> T getBean(AbstractBean<T> abstractBean, boolean singleton) {
        this.singleton = singleton;
        T bean = getBean(abstractBean);
        this.singleton = true;
        return bean;
    }

    public <T> T getBean(String className) {
        Class<T> clazz = null;
        try {
            clazz = (Class<T>) beanFactory.getClassByClassName(className);
        } catch (Exception e) {
            System.err.println("Не удалось найти класс");
        }
        return APPLICATION_CONTEXT.getBean(clazz);
    }

    public void callPostProcessor(Object bean) {
        beanFactory.getBeanConfigurator().getScanner().getSubTypesOf(BeanPostProcessor.class)
                .forEach(processor -> {
                    try {
                        processor.getDeclaredConstructor().newInstance().process(bean);
                    } catch (InstantiationException e) {
                        e.printStackTrace();
                    } catch (IllegalAccessException e) {
                        e.printStackTrace();
                    } catch (InvocationTargetException e) {
                        e.printStackTrace();
                    } catch (NoSuchMethodException e) {
                        e.printStackTrace();
                    }
                });
    }
}
