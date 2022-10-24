package com.lapin.common.client.gui.controllers;

import com.lapin.common.client.gui.Localization;
import javafx.event.Event;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.MenuItem;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.util.Callback;
import lombok.Setter;

import java.io.IOException;
import java.util.Objects;

public abstract class AbstractController {
    @Setter
    private Scene currentScene;
    @Setter
    private Stage currentStage;

    public FXMLLoader getLoader() {
        return loader;
    }

    private FXMLLoader loader;

    public AbstractController switchScene(Event event, String viewPath, Callback<Class<?>, Object> callback) throws IOException{
        Localization localization = Localization.getInstance();
        loader = new FXMLLoader(Objects.requireNonNull(getClass().getResource(viewPath)));
        loader.setControllerFactory(callback);
        loader.getController();
        loader.setResources(localization.getResourceBundle());
        Parent root = loader.load();
        currentStage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        currentScene = new Scene(root);
        currentStage.setScene(currentScene);
        AbstractController abstractController = loader.getController();
        abstractController.setCurrentStage(currentStage);
        abstractController.setCurrentScene(currentScene);
        return this;
    }
    public void openPopupWindow(Event event, String viewPath, Callback<Class<?>, Object> callback){
        try{
            Localization localization = Localization.getInstance();

            FXMLLoader loader = new FXMLLoader(Objects.requireNonNull(getClass().getResource(viewPath)));
            loader.setControllerFactory(callback);
            loader.setResources(localization.getResourceBundle());
            Stage primaryStage;
            try {
                primaryStage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            } catch (ClassCastException e) {
                primaryStage = (Stage) (((MenuItem) event.getSource()).getParentPopup().getOwnerWindow());
            }
            Parent root = loader.load();
            Stage stage = new Stage();
            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.initModality(Modality.WINDOW_MODAL);
            stage.initOwner(primaryStage);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public void showErrorAlert(String text) {
        Alert alert = new Alert(Alert.AlertType.ERROR, text);
        alert.setHeaderText(null);
        alert.showAndWait();
    }

    public void showInfoAlert(String text) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION, text);
        alert.setHeaderText(null);
        alert.showAndWait();
    }

    public Scene getCurrentScene() {
        return currentScene;
    }

    public Stage getCurrentStage() {
        return currentStage;
    }
    public AbstractController showStage(){
        currentStage.show();
        return this;
    }
    public AbstractController setStageTitle(String title){
        currentStage.setTitle(title);
        return this;
    }
    public AbstractController setStylesheets(String StylesheetsPath){
        currentScene.getStylesheets().add(getClass().getResource(StylesheetsPath).toExternalForm());
        return this;
    }
}
