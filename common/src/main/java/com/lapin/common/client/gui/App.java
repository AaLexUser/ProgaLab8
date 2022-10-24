package com.lapin.common.client.gui;

import com.lapin.common.client.Client;
import com.lapin.common.client.gui.controllers.SignInController;
import com.lapin.di.annotation.Inject;
import com.lapin.di.context.ApplicationContext;
import com.lapin.network.StatusCodes;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.stage.Stage;
import lombok.Getter;
import lombok.Setter;

public class App extends Application {
    private final static Client client = ApplicationContext.getInstance().getBean(Client.class);
    @Getter
    private static Scene scene;
    @Override
    public void start(Stage stage) throws Exception {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/views/signIn.fxml"));
        Parent root = loader.load();
        SignInController signInController =loader.getController();
        stage.setTitle("Sign In");
        //stage.initStyle(StageStyle.UTILITY);
        Image icon = new Image("/images/AppIcon.png");
        stage.getIcons().add(icon);
        scene = new Scene(root);
        stage.setOnCloseRequest(windowEvent -> {
            client.setStatusCode(StatusCodes.EXIT_CLIENT);
            Platform.exit();
            windowEvent.consume();
        });
        scene.getStylesheets().add(getClass().getResource("/styles/signIn.css").toExternalForm());
        scene.setOnKeyPressed(new EventHandler<KeyEvent>() {
            @Override
            public void handle(KeyEvent event) {
                if(event.getCode() == KeyCode.ENTER){
                    signInController.signIn(null);
                }
            }
        });
        //stage.setResizable (false);
        stage.setScene(scene);
        stage.show();
    }
    public static void main(String[] args){
        launch(args);
    }
}
