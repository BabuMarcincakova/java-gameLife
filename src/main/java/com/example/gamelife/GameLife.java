package com.example.gamelife;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.util.Duration;
/**
 * Hlavná trieda aplikácie Game of Life
 */
public class GameLife extends Application {
    static Cell[][] map = new Cell[40][40];
    private static double speed = 500;
    private EventHandler<ActionEvent> handler;
    private Timeline animation;

    /**
     * Metóda nastavuje rýchlosť simulácie.
     * @param s Rýchlosť v milisekundách.
     */
    public static void setSpeed(double s) {
        speed = s;
    }

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        LifePane panel = new LifePane();

        handler = e -> {
            double s = 50;
            if (panel.getSection() == 2) {
                panel.paint();
                s = speed;
            }
            animation.stop();
            animation.getKeyFrames().setAll(new KeyFrame(Duration.millis(s), handler));
            animation.play();
        };
        animation = new Timeline(new KeyFrame(Duration.millis(50), handler));

        animation.setCycleCount(Timeline.INDEFINITE);
        animation.play();

        Scene scene = new Scene(panel, 600, 500);
        primaryStage.setTitle("Game Life");
        primaryStage.setScene(scene);
        primaryStage.show();

        panel.settings();
    }
}