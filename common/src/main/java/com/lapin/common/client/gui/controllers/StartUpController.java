package com.lapin.common.client.gui.controllers;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.animation.TranslateTransition;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Bounds;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.util.Duration;

import java.util.ArrayList;


public class StartUpController extends Application implements Runnable {
    private int level;
    private Pane pane;
    private Label levelLabel;
    private ArrayList<Circle> balls = new ArrayList<>();
    @Override
    public void start(Stage stage) throws Exception {
        pane = new Pane();
        Scene scene = new Scene(pane, 500, 500, Color.WHITE);
        levelLabel = new Label();
        levelLabel.setVisible(false);
        levelLabel.setLayoutX(scene.getX() / 2);
        levelLabel.setLayoutY(scene.getY() / 2);
        stage.initStyle(StageStyle.TRANSPARENT);
        createBall();
        stage.setTitle("Animated Ball");
        stage.setScene(scene);
        stage.show();
        animate();

    }
    public static void main(String[] args) {
        launch();
    }

    @Override
    public void run() {
        launch();
    }
    private Circle createBall(){
        Circle ball = new Circle((10+Math.random()*30), Color.color( Math.random(),Math.random(),Math.random()));
        ball.relocate(Math.random()*(pane.getWidth()-ball.getRadius()), Math.random()*(pane.getHeight()-ball.getRadius()));
        ball.setOnMouseClicked(event -> caught(event));
        balls.add(ball);
        pane.getChildren().add(ball);
        return ball;
    }
    private void caught(MouseEvent event){
        ArrayList<Circle> ballsToRemove = new ArrayList<>();
        for (Circle ball:balls){
            if(ball.getCenterX() + ball.getRadius() >= event.getX() && ball.getCenterX() - ball.getRadius() <= event.getX()){
                if(ball.getCenterY() + ball.getRadius() >= event.getY() && ball.getCenterY() - ball.getRadius() <= event.getY()){
                    pane.getChildren().remove(ball);
                    ballsToRemove.add(ball);
                }
            }
        }
        ballsToRemove.stream().forEach(ball -> balls.remove(ball));
        createBall();
        animate();
    }
    public void animate(){
        while (true){
            double dx = 7; //Step on x or velocity
            double dy = 3; //Step on y
            int count = 0;
            for (Circle ball : balls) {
                TranslateTransition tt = new TranslateTransition();
                tt.setNode(ball);

                tt.setByX(ball.getLayoutX() + dx);
                tt.setByY(ball.getLayoutY() + dy);
                //move the ball
                tt.setDelay(Duration.seconds(1));
                tt.setCycleCount(0);
                //If the ball reaches the left or right border make the step negative
                if (ball.getLayoutX() <= (0 + ball.getRadius()) ||
                        ball.getLayoutX() >= (pane.getWidth() - ball.getRadius())) {

                    dx = -dx;

                }

                //If the ball reaches the bottom or top border make the step negative
                if ((ball.getLayoutY() >= (pane.getHeight() - ball.getRadius())) ||
                        (ball.getLayoutY() <= (0 + ball.getRadius()))) {

                    dy = -dy;
                }
                tt.play();
            }
            if(balls.size() < 10 && count == 1000){
                count = 0;
                createBall();
            }
            count+=1;
        }
    }
    public void animated(){
        Timeline timeline = new Timeline(new KeyFrame(Duration.millis(100), new EventHandler<ActionEvent> () {
            double dx = 7; //Step on x or velocity
            double dy = 3; //Step on y
            int count = 0;
            @Override
            public void handle (ActionEvent t){
                for (Circle ball : balls) {
                    TranslateTransition tt = new TranslateTransition();
                    tt.setNode(ball);

                    tt.setByX(ball.getLayoutX() + dx);
                    tt.setByY(ball.getLayoutY() + dy);
                    //move the ball
//                    ball.setLayoutX(ball.getLayoutX() + dx);
//                    ball.setLayoutY(ball.getLayoutY() + dy);

                    tt.setDelay(Duration.seconds(1));
                    tt.setCycleCount(0);


                    //If the ball reaches the left or right border make the step negative
                    if (ball.getLayoutX() <= (0 + ball.getRadius()) ||
                            ball.getLayoutX() >= (pane.getWidth() - ball.getRadius())) {

                        dx = -dx;

                    }

                    //If the ball reaches the bottom or top border make the step negative
                    if ((ball.getLayoutY() >= (pane.getHeight() - ball.getRadius())) ||
                            (ball.getLayoutY() <= (0 + ball.getRadius()))) {

                        dy = -dy;
                    }
                    tt.play();
                }
                if(balls.size() < 10 && count == 1000){
                    count = 0;
                    createBall();
                }
                count+=1;
            }
        }));
        timeline.setCycleCount(Timeline.INDEFINITE);
        timeline.play();
    }
}
