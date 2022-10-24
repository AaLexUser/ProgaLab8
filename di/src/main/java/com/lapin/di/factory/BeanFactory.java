package com.lapin.di.factory;


import com.lapin.di.annotation.Inject;
import com.lapin.di.config.Configuration;
import com.lapin.di.config.JavaConfiguration;
import com.lapin.di.configurator.BeanConfigurator;
import com.lapin.di.configurator.JavaBeanConfigurator;
import com.lapin.di.context.AbstractBean;
import com.lapin.di.context.ApplicationContext;
import lombok.Getter;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.util.Arrays;
import java.util.stream.Collectors;

/**
 * Фабрика бинов
 */
public class BeanFactory {
    @Getter
    private final BeanConfigurator beanConfigurator;
    private final ApplicationContext applicationContext;

    public BeanFactory(ApplicationContext applicationContext) {
        Configuration configuration = new JavaConfiguration();
        this.beanConfigurator = new JavaBeanConfigurator(configuration.getPackageToScan());
        this.applicationContext = applicationContext;
    }

    public <T> T getBean(AbstractBean<T> abstractBean) {
        Class<? extends T> implementationClass = abstractBean.getClazz();
        if (implementationClass.isInterface()) {
            try {
                implementationClass = beanConfigurator.getImplementationClass(implementationClass);
            }catch (ClassNotFoundException e){
                return null;
            }
        }
        T bean = null;
        try {
            bean = implementationClass.getDeclaredConstructor(abstractBean.getParameterTypes()).newInstance(abstractBean.getInitargs());
        } catch (InstantiationException e) {
            System.err.println("Не удалось создать экземпляр");
        } catch (IllegalAccessException e) {
            System.err.println("Нет доступа к конструктору");
        } catch (InvocationTargetException e) {
            System.err.println("InvocationTargetException");
        } catch (NoSuchMethodException e) {
            System.err.println("Конструктор не найден");
        }
        for (Field field : Arrays.stream(implementationClass.getDeclaredFields()).filter(field -> field.isAnnotationPresent(Inject.class)).collect(Collectors.toList())) {
            field.setAccessible(true);
            try {
                field.set(bean, applicationContext.getBean(field.getType()));
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        }
        return bean;
    }

    public Class<?> getClassByClassName(String className) throws Exception {
        return beanConfigurator.getImplementationClass(className);
    }
}
