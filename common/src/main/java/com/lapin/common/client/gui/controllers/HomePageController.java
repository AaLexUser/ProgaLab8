package com.lapin.common.client.gui.controllers;

import com.lapin.common.client.gui.models.RouteTableView;
import com.lapin.common.controllers.CommandManager;
import com.lapin.common.data.*;
import com.lapin.di.context.ApplicationContext;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.*;

import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.shape.ArcType;
import javafx.scene.shape.StrokeLineJoin;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import lombok.Getter;

import java.net.URL;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.ResourceBundle;
import java.util.Set;

public class HomePageController extends AbstractController implements Initializable {
    @FXML
    private SplitPane verticalSplitPane;
    @FXML
    private SplitPane horizontalSplitPane;
    @FXML
    private ScrollPane tableScrollPane;
    @FXML
    private VBox headerVbox;
    @FXML
    private VBox vboxBodyLeft;
    @FXML
    private VBox vboxTable;
    @FXML
    private BorderPane borderPane;

    @FXML
    private Label usernameLabel;
    private final User user= ApplicationContext.getInstance().getBean(CommandManager.class).getClient().getUser();
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        RouteTableView.getInstance().setPane(vboxTable);
        addButton();
        usernameLabel.setText(user.getUsername());
    }
    public void initSize(){
        Stage stage = super.getCurrentStage();
        horizontalSplitPane.setMinHeight(stage.getHeight()*1/8);
        verticalSplitPane.setMinHeight(stage.getHeight()*6/8);
        vboxTable.setMinHeight(verticalSplitPane.getMinHeight()*10/11);
        RouteTableView.getInstance().setHeight(vboxTable.getHeight());
    }
    public void handleExitButton(ActionEvent event){
        RouteTableView.getInstance().removeFromPane(vboxTable);
    }
    private void addButton2(){
        Canvas canvas  = new Canvas(40, 40);
        GraphicsContext graphicsContext = canvas.getGraphicsContext2D();
        Paint backgroundColor = Color.web("#FDDD71");
        graphicsContext.setFill(backgroundColor);
        graphicsContext.setStroke(Color.LIGHTGRAY);
        graphicsContext.setLineWidth(1);
        graphicsContext.fillRoundRect(
                1,
                1,
                canvas.getWidth() - 1,
                canvas.getHeight() - 1, 10, 10);
        graphicsContext.setLineWidth(2);
        graphicsContext.strokeRoundRect(
                0,
                0,
                canvas.getWidth(),
                canvas.getHeight(), 10, 10);
        graphicsContext.setLineWidth(2);
        int counter = 0;
        ArrayList<Double> xM = new ArrayList<>();
        ArrayList<Double> yM = new ArrayList<>();
        for(int x = 10*100; x <= 20*100; x+=1){
            xM.add((x/100.0));
            yM.add(Math.sqrt(100-Math.pow((x/100.0),2) + 20));
            counter+=1;
        }

    }
    private void addButton() {
        Canvas canvas = new Canvas(40, 40);
        GraphicsContext graphicsContext = canvas.getGraphicsContext2D();
        Paint backgroundColor = Color.web("#FDDD71");
        graphicsContext.setFill(backgroundColor);
        graphicsContext.setStroke(Color.LIGHTGRAY);
        graphicsContext.setLineWidth(1);
        graphicsContext.fillRoundRect(
                1,
                1,
                canvas.getWidth() - 1,
                canvas.getHeight() - 1, 10, 10);
        graphicsContext.setLineWidth(2);
        graphicsContext.strokeRoundRect(
                0,
                0,
                canvas.getWidth(),
                canvas.getHeight(), 10, 10);
        graphicsContext.setLineWidth(2);
        graphicsContext.setStroke(Color.RED);

        //plus image
        //vertical line
        graphicsContext.strokeLine(8, 3, 8, 13);
        //horizontal line
        graphicsContext.strokeLine(3, 8, 13, 8);
        graphicsContext.setStroke(Color.BLACK);
        graphicsContext.setLineWidth(2);
        graphicsContext.strokePolygon(new double[]{1, 6, 6, 10, 10, 15, 15, 10, 10, 6, 6, 1}, new double[]{6, 6, 1, 1, 6, 6, 10, 10, 15, 15, 10, 10}, 12);
        graphicsContext.setFill(Color.RED);
        graphicsContext.setStroke(Color.BLACK);
        graphicsContext.fillPolygon(new double[]{10, 20, 30}, new double[]{20, 40, 20}, 3);
        graphicsContext.setLineJoin(StrokeLineJoin.ROUND);
        graphicsContext.strokeLine(20, 40, 10, 20);
        graphicsContext.strokeLine(20, 40, 30, 20);
        graphicsContext.fillArc(10, 10, 20, 20, 0, 180, ArcType.OPEN);
        graphicsContext.strokeArc(10, 10, 20, 20, 0, 180, ArcType.OPEN);
        graphicsContext.setFill(backgroundColor);
        graphicsContext.fillArc(15, 15, 10, 10, 0, 360, ArcType.OPEN);
        graphicsContext.strokeArc(15, 15, 10, 10, 0, 360, ArcType.OPEN);
        headerVbox.getChildren().add(canvas);
        canvas.setOnMouseEntered(ent -> {
            canvas.setScaleX(1.3);
            canvas.setScaleY(1.3);
        });
        canvas.setOnMouseExited(event -> {
            canvas.setScaleX(1);
            canvas.setScaleY(1);
        });
        canvas.setOnMouseClicked(event ->
                headerVbox.getChildren().remove(canvas));
    }
//    private void tableInit(){
//        TableView tableView = new TableView();
//        headerVbox.getChildren().add(tableView);
//        TableColumn<ShowRoute,Integer> idcol = new TableColumn<>();
//        tableView.getColumns().add(idcol);
//        tableView.setItems(showRoutes);
//        idcol.setCellValueFactory(new PropertyValueFactory<>("id"));
//
//        //Id column
//        id.setCellValueFactory(new PropertyValueFactory<>("id"));
//
//        //Name column
//        name.setCellValueFactory(new PropertyValueFactory<>("name"));
//
//        //Coordinate X column
//        coordinateX.setCellValueFactory(new PropertyValueFactory<>("coordinateX"));
//
//        //Coordinate Y column
//        coordinateY.setCellValueFactory(new PropertyValueFactory<>("coordinateY"));
//
//        //CreationDate column
//        creationDate.setCellValueFactory(new PropertyValueFactory<>("creationDate"));
//
//        //From X column
//        fromX.setCellValueFactory(new PropertyValueFactory<>("fromX"));
//
//        //From Y column
//        fromY.setCellValueFactory(new PropertyValueFactory<>("fromY"));
//
//        //From Z column
//        fromZ.setCellValueFactory(new PropertyValueFactory<>("fromZ"));
//
//        //To X column
//        toX.setCellValueFactory(new PropertyValueFactory<>("toX"));
//
//        //To Y column
//        toY.setCellValueFactory(new PropertyValueFactory<>("toY"));
//
//        //To Name column
//        toName.setCellValueFactory(new PropertyValueFactory<>("toName"));
//
//        //Distance column
//        distance.setCellValueFactory(new PropertyValueFactory<>("distance"));
//
//        //Author column
//        author.setCellValueFactory(new PropertyValueFactory<>("author"));
//        if(showRoutes != null) {
//            tableRoute.setItems(showRoutes);
//        }
//    }
//    private ObservableList<ShowRoute> getRouteList(){
//        CommandManager commandManager = ApplicationContext.getInstance().getBean(CommandManager.class);
//        Set<Route> routeTable = (Set<Route>) commandManager.handle("show", "", null).getSecond();
//        if (routeTable != null) {
//            ObservableList<ShowRoute> observableList = FXCollections.observableArrayList();
//            for (Route route : routeTable) {
//                observableList.add(new ShowRoute(route.getId(),
//                        route.getName(),
//                        route.getCoordinates().getX(),
//                        route.getCoordinates().getY(),
//                        route.getCreationDate(),
//                        route.getFrom().getX(),
//                        route.getFrom().getY(),
//                        route.getFrom().getZ(),
//                        route.getTo().getX(),
//                        route.getTo().getY(),
//                        route.getTo().getName(),
//                        route.getDistance(),
//                        route.getAuthor().getUsername()));
//            }
//            return observableList;
//        }
//        else return null;
//    }
    public void onClick(MouseEvent touchEvent) {
        System.out.println("Click");
    }


//    public static class ShowRoute{
//        @Getter
//        Integer id;
//        @Getter
//        String name;
//        @Getter
//        Double coordinateX;
//        @Getter
//        Double coordinateY;
//        @Getter
//        LocalDate creationDate;
//        @Getter
//        Integer fromX;
//        @Getter
//        Float fromY;
//        @Getter
//        Double fromZ;
//        @Getter
//        Float toX;
//        @Getter
//        Long toY;
//        @Getter
//        String toName;
//        @Getter
//        Long distance;
//        @Getter
//        String author;
//
//        public ShowRoute(Integer id, String name, Double coordinateX, Double coordinateY, LocalDate creationDate, Integer fromX, Float fromY, Double fromZ, Float toX, Long toY, String toName, Long distance, String author) {
//            this.id = id;
//            this.name = name;
//            this.coordinateX = coordinateX;
//            this.coordinateY = coordinateY;
//            this.creationDate = creationDate;
//            this.fromX = fromX;
//            this.fromY = fromY;
//            this.fromZ = fromZ;
//            this.toX = toX;
//            this.toY = toY;
//            this.toName = toName;
//            this.distance = distance;
//            this.author = author;
//        }
//    }
}
