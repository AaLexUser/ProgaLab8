package com.lapin.common.client.gui.controllers;

import com.lapin.common.client.Client;
import com.lapin.common.client.clientpostprocessor.Authorization;
import javafx.animation.TranslateTransition;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.transform.Translate;
import javafx.stage.Stage;
import javafx.util.Duration;
import lombok.Setter;

import java.io.IOException;

public class SignUpController {
    @Setter
    private Client client;
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
    private void signIn(ActionEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/views/signIn.fxml"));
        Parent root = loader.load();
        SignInController signInController = loader.getController();
        signInController.setClient(client);
        Stage stage = (Stage)((Node)event.getSource()).getScene().getWindow();
        Scene scene = new Scene(root);
        stage.setScene(scene);
        scene.getStylesheets().add(getClass().getResource("/styles/signIn.css").toExternalForm());
        scene.setOnKeyPressed(new EventHandler<KeyEvent>() {
            @Override
            public void handle(KeyEvent event) {
                if(event.getCode() == KeyCode.ENTER){
                    signInController.signIn(null);
                }
            }
        });
        stage.setTitle("Sign In");
        stage.show();
    }
    @FXML
    public void signUp(ActionEvent event){
        Authorization auth = new Authorization(client);
        String username = usernameField.getText();
        String password = passwordField.getText();
        if(username.equals("") || password.equals("")){
            error("Fill in all the fields");
        }
        else {
            try {
                auth.signUp(username, password);
                System.out.println("Success login");
            } catch (RuntimeException e) {
                error(e.getMessage());
            }
        }
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
