package com.lapin.common.client.gui.controllers;

import com.lapin.common.controllers.CommandManager;
import com.lapin.common.data.*;
import com.lapin.di.context.ApplicationContext;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;

import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import lombok.Getter;

import java.net.URL;
import java.time.LocalDate;
import java.util.ResourceBundle;
import java.util.Set;

public class HomePageController implements Initializable {
    @FXML
    private TableView<ShowRoute> tableRoute;
    @FXML
    private TableColumn<ShowRoute, Integer> id;
    @FXML
    private TableColumn<ShowRoute, String> name;
    @FXML
    private TableColumn<ShowRoute, Double> coordinateX;
    @FXML
    private TableColumn<ShowRoute, Double> coordinateY;
    @FXML
    private TableColumn<ShowRoute, LocalDate> creationDate;
    @FXML
    private TableColumn<ShowRoute, Integer> fromX;
    @FXML
    private TableColumn<ShowRoute, Float> fromY;
    @FXML
    private TableColumn<ShowRoute, Double> fromZ;
    @FXML
    private TableColumn<ShowRoute, Float> toX;
    @FXML
    private TableColumn<ShowRoute, Long> toY;
    @FXML
    private TableColumn<ShowRoute, String> toName;
    @FXML
    private TableColumn<ShowRoute, Long> distance;
    @FXML
    private TableColumn<ShowRoute, String> author;
    private final ObservableList<ShowRoute> showRoutes = getRouteList();
    @FXML
    private Label usernameLabel;
    private final User user= ApplicationContext.getInstance().getBean(CommandManager.class).getClient().getUser();
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        //Id column
        id.setCellValueFactory(new PropertyValueFactory<>("id"));

        //Name column
        name.setCellValueFactory(new PropertyValueFactory<>("name"));

        //Coordinate X column
        coordinateX.setCellValueFactory(new PropertyValueFactory<>("coordinateX"));

        //Coordinate Y column
        coordinateY.setCellValueFactory(new PropertyValueFactory<>("coordinateY"));

        //CreationDate column
        creationDate.setCellValueFactory(new PropertyValueFactory<>("creationDate"));

        //From X column
        fromX.setCellValueFactory(new PropertyValueFactory<>("fromX"));

        //From Y column
        fromY.setCellValueFactory(new PropertyValueFactory<>("fromY"));

        //From Z column
        fromZ.setCellValueFactory(new PropertyValueFactory<>("fromZ"));

        //To X column
        toX.setCellValueFactory(new PropertyValueFactory<>("toX"));

        //To Y column
        toY.setCellValueFactory(new PropertyValueFactory<>("toY"));

        //To Name column
        toName.setCellValueFactory(new PropertyValueFactory<>("toName"));

        //Distance column
        distance.setCellValueFactory(new PropertyValueFactory<>("distance"));

        //Author column
        author.setCellValueFactory(new PropertyValueFactory<>("author"));
        if(showRoutes != null) {
            tableRoute.setItems(showRoutes);
        }
        usernameLabel.setText(user.getUsername());

    }
    public void handleExitButton(ActionEvent event){
        Platform.exit();
        event.consume();
    }
    private ObservableList<ShowRoute> getRouteList(){
        CommandManager commandManager = ApplicationContext.getInstance().getBean(CommandManager.class);
        Set<Route> routeTable = (Set<Route>) commandManager.handle("show", "", null).getSecond();
        if (routeTable != null) {
            ObservableList<ShowRoute> observableList = FXCollections.observableArrayList();
            for (Route route : routeTable) {
                observableList.add(new ShowRoute(route.getId(),
                        route.getName(),
                        route.getCoordinates().getX(),
                        route.getCoordinates().getY(),
                        route.getCreationDate(),
                        route.getFrom().getX(),
                        route.getFrom().getY(),
                        route.getFrom().getZ(),
                        route.getTo().getX(),
                        route.getTo().getY(),
                        route.getTo().getName(),
                        route.getDistance(),
                        route.getAuthor().getUsername()));
            }
            return observableList;
        }
        else return null;
    }

    public void onClick(MouseEvent touchEvent) {
        System.out.println("Click");
    }


    public static class ShowRoute{
        @Getter
        Integer id;
        @Getter
        String name;
        @Getter
        Double coordinateX;
        @Getter
        Double coordinateY;
        @Getter
        LocalDate creationDate;
        @Getter
        Integer fromX;
        @Getter
        Float fromY;
        @Getter
        Double fromZ;
        @Getter
        Float toX;
        @Getter
        Long toY;
        @Getter
        String toName;
        @Getter
        Long distance;
        @Getter
        String author;

        public ShowRoute(Integer id, String name, Double coordinateX, Double coordinateY, LocalDate creationDate, Integer fromX, Float fromY, Double fromZ, Float toX, Long toY, String toName, Long distance, String author) {
            this.id = id;
            this.name = name;
            this.coordinateX = coordinateX;
            this.coordinateY = coordinateY;
            this.creationDate = creationDate;
            this.fromX = fromX;
            this.fromY = fromY;
            this.fromZ = fromZ;
            this.toX = toX;
            this.toY = toY;
            this.toName = toName;
            this.distance = distance;
            this.author = author;
        }
    }
}
