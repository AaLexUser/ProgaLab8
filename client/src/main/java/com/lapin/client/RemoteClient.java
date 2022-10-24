package com.lapin.client;


import com.lapin.common.client.Client;
import com.lapin.common.client.gui.App;
import com.lapin.common.client.gui.controllers.StartUpController;
import com.lapin.di.context.AbstractBean;
import com.lapin.di.context.ApplicationContext;
import com.lapin.di.factory.BeanFactory;
import javafx.application.Application;
import javafx.stage.Stage;
import lombok.SneakyThrows;

import java.io.File;
import java.net.URI;
import java.net.URISyntaxException;

public class RemoteClient {
    public static void main(String[] args) throws URISyntaxException {
        BeanFactory beanFactory = new BeanFactory(ApplicationContext.getInstance());
        ApplicationContext.getInstance().setBeanFactory(beanFactory);
        File resources = (File) ApplicationContext.getInstance().getBean(new AbstractBean<>(File.class)
                .setParameterTypes(URI.class).setInitargs(RemoteClient
                        .class
                        .getClassLoader()
                        .getResource("config.properties")
                        .toURI()));
        Client user = (Client) ApplicationContext.getInstance().getBean(new AbstractBean<>(Client.class)
                .setParameterTypes(File.class, String[].class)
                .setInitargs(resources, args));
        StartUpController startUpController = new StartUpController();
        Thread userthread = new Thread(user);
        Thread waiting = new Thread(startUpController);
        userthread.start();
        waiting.start();
//        user.run();
    }
}
