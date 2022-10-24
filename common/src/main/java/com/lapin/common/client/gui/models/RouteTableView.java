package com.lapin.common.client.gui.models;

import com.lapin.common.client.gui.controllers.HomePageController;
import com.lapin.common.controllers.CommandManager;
import com.lapin.common.data.Coordinates;
import com.lapin.common.data.Route;
import com.lapin.di.context.ApplicationContext;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.Pane;
import lombok.Getter;

import java.time.LocalDate;
import java.util.Set;

public class RouteTableView {
    private static TableView tableView;
    private static final RouteTableView ROUTE_TABLE_VIEW = new RouteTableView();
    private final ObservableList<ShowRoute> showRoutes = getRouteList();
    private RouteTableView(){
        tableInit();
    }
    public void setHeight(double height){
        tableView.setMinHeight(height);
    }
    public void setPane(Pane pane){
        pane.getChildren().add(tableView);
        tableView.setMinHeight(500);
    }
    public void removeFromPane(Pane pane){
        pane.getChildren().remove(tableView);
    }
    public static TableView<?> getTableView(){
        return tableView;
    }
    public static RouteTableView getInstance(){
        return ROUTE_TABLE_VIEW;
    }
    private void tableInit(){
        tableView = new TableView<>();
        
        //Id column
        TableColumn<ShowRoute, Integer> id = new TableColumn<>("id");
        id.setCellValueFactory(new PropertyValueFactory<>("id"));
        tableView.getColumns().add(id);

        //Name column
        TableColumn<ShowRoute, String> name = new TableColumn<>("name");
        name.setCellValueFactory(new PropertyValueFactory<>("name"));
        tableView.getColumns().add(name);


        //Coordinates
        TableColumn coordinates = new TableColumn("coordinates");
        coordinates.setEditable(false);

        //Coordinate X column
        TableColumn<ShowRoute, Double> coordinateX = new TableColumn<>("x");
        coordinateX.setCellValueFactory(new PropertyValueFactory<>("coordinateX"));

        //Coordinate Y column
        TableColumn<ShowRoute, Double> coordinateY = new TableColumn<>("y");
        coordinateY.setCellValueFactory(new PropertyValueFactory<>("coordinateY"));

        coordinates.getColumns().addAll(coordinateX,coordinateY);
        tableView.getColumns().add(coordinates);


        //CreationDate column
        TableColumn<ShowRoute, LocalDate> creationDate = new TableColumn<>("creation date");;
        creationDate.setCellValueFactory(new PropertyValueFactory<>("creationDate"));
        tableView.getColumns().add(creationDate);


        //Location From column
        TableColumn locationFrom = new TableColumn("location from");
        coordinates.setEditable(false);

        //From X column
        TableColumn<ShowRoute, Integer> fromX = new TableColumn<>("x");;
        fromX.setCellValueFactory(new PropertyValueFactory<>("fromX"));

        //From Y column
        TableColumn<ShowRoute, Float> fromY = new TableColumn<>("y");;
        fromY.setCellValueFactory(new PropertyValueFactory<>("fromY"));

        //From Z column
        TableColumn<ShowRoute, Double> fromZ = new TableColumn<>("z");;
        fromZ.setCellValueFactory(new PropertyValueFactory<>("fromZ"));

        locationFrom.getColumns().addAll(fromX,fromY,fromZ);
        tableView.getColumns().add(locationFrom);

        //Location To column
        TableColumn locationTo = new TableColumn("location to");
        coordinates.setEditable(false);

        //To X column
        TableColumn<ShowRoute, Float> toX = new TableColumn<>("x");;
        toX.setCellValueFactory(new PropertyValueFactory<>("toX"));

        //To Y column
        TableColumn<ShowRoute, Long> toY = new TableColumn<>("y");;
        toY.setCellValueFactory(new PropertyValueFactory<>("toY"));

        //To Name column
        TableColumn<ShowRoute, String> toName = new TableColumn<>("name");;
        toName.setCellValueFactory(new PropertyValueFactory<>("toName"));

        locationTo.getColumns().addAll(toX,toY,toName);
        tableView.getColumns().add(locationTo);

        //Distance column
        TableColumn<ShowRoute, Long> distance = new TableColumn<>("distance");;
        distance.setCellValueFactory(new PropertyValueFactory<>("distance"));
        tableView.getColumns().add(distance);

        //Author column
        TableColumn<ShowRoute, String> author = new TableColumn<>("author");;
        author.setCellValueFactory(new PropertyValueFactory<>("author"));
        tableView.getColumns().add(author);
        if(showRoutes != null) {
            tableView.setItems(showRoutes);
        }
        
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
