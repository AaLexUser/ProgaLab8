package com.lapin.client;


import com.lapin.common.client.Client;
import com.lapin.common.client.gui.App;
import javafx.application.Application;
import javafx.stage.Stage;

import java.io.File;
import java.net.URISyntaxException;

public class RemoteClient {
    public static void main(String[] args) throws URISyntaxException {
        File resources = new File(RemoteClient
                .class
                .getClassLoader()
                .getResource("config.properties")
                .toURI());
        Client user = new Client(resources, args);
        user.run();
    }
}
