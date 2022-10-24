package com.lapin.common.client.gui.controllers;

import com.lapin.common.client.Client;
import com.lapin.common.client.clientpostprocessor.Authorization;
import com.lapin.common.controllers.CommandManager;
import com.lapin.di.context.ApplicationContext;
import javafx.animation.TranslateTransition;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.PasswordField;
import javafx.scene.control.*;
import javafx.scene.effect.DropShadow;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.paint.Color;
import javafx.scene.transform.Translate;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.util.Duration;
import lombok.Setter;

import java.io.IOException;


public class SignInController extends AbstractController {
    private double xOffset;
    private double yOffset;

    @Setter
    private Client client = ApplicationContext.getInstance().getBean(Client.class);
    @FXML
    private TextField usernameField;
    @FXML
    private PasswordField passwordField;
    @FXML
    private ImageView loginImg;
    @FXML
    private Button signIn;
    @FXML
    private Button singUp;
    @FXML
    private Label errorLabel;
    @FXML
    public void signIn(ActionEvent event) {
        Authorization auth = new Authorization(client);
        String username = usernameField.getText();
        String password = passwordField.getText();
        if(username.equals("") || password.equals("") ){
            error("Fill in all the fields");
        }
        else{
            try {
                auth.signIn(username, password);
                AbstractController abstractController = switchScene(event,"/views/homePage.fxml",aClass -> new HomePageController())
                        .setStylesheets("/styles/homePage.css")
                        .setStageTitle(username.toUpperCase());
                HomePageController homePage = abstractController.getLoader().getController();
                homePage.initSize();
                Scene scene = homePage.getCurrentScene();
//                root.setOnMousePressed(event1 -> {
//                    xOffset = event1.getSceneX();
//                    yOffset = event1.getSceneY();
//                    event1.consume();
//                });
//                root.setOnMouseDragged(event1 -> {
//                    stage.setX(event1.getScreenX() - xOffset);
//                    stage.setY(event1.getScreenY() - yOffset);
//                    event1.consume();
//                });
                scene.getRoot().setEffect(new DropShadow(10, Color.rgb(100, 100, 100)));
                scene.setFill(Color.TRANSPARENT);
                showStage();
            }catch (Exception e){
                error(e.getMessage());
            }
        }
    }
    @FXML
    private void signUp(ActionEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/views/signUp.fxml"));
        Parent root = loader.load();
        SignUpController signUpController = loader.getController();
        signUpController.setClient(client);
        Stage stage = (Stage)((Node)event.getSource()).getScene().getWindow();
        Scene scene = new Scene(root);
        stage.setScene(scene);
        scene.getStylesheets().add(getClass().getResource("/styles/signUp.css").toExternalForm());
        scene.setOnKeyPressed(new EventHandler<KeyEvent>() {
            @Override
            public void handle(KeyEvent event) {
                if(event.getCode() == KeyCode.ENTER){
                    signUpController.signUp(null);
                }
            }
        });

        stage.setTitle("Sign Up");

        stage.show();
    }
    private void error(String errorMessage){
        errorLabel.setText(errorMessage);
        usernameField.setStyle("-fx-border-color: transparent transparent red transparent;" +
                "-fx-prompt-text-fill: red;" +
                "-fx-text-fill: red;");
        passwordField.setStyle("-fx-border-color: transparent transparent red transparent;" +
                "-fx-prompt-text-fill: red;" +
                "-fx-text-fill: red;");
        errorLabel.setVisible(true);
        errorAnimation(usernameField);
        errorAnimation(passwordField);
    }
    private void errorAnimation(TextField textField){
        TranslateTransition translate = new TranslateTransition();
        translate.setNode(textField);
        translate.setDuration(Duration.millis(100));
        translate.setCycleCount(3);
        translate.setByX(10);
        translate.setAutoReverse(true);
        translate.play();
        Translate tr = new Translate();
        tr.setX(-10);
        textField.getTransforms().add(tr);
    }
}
