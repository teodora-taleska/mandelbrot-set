package com.example.mandelbrotset2;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import java.io.IOException;
import java.util.Optional;


public class MandelbrotParallel extends Application {

    @FXML
    private AnchorPane anchorPane;

    Scene1Controller scene1 = new Scene1Controller();
    private final int CANVAS_WIDTH = scene1.getWidth() - 60;
    private final int CANVAS_HEIGHT = scene1.getHeight() - 50;
    // Left and right border
    private static final int X_OFFSET = 25;
    // Top and Bottom border
    private static final int Y_OFFSET = 25;

    @FXML
    private final Canvas canvas = new Canvas(CANVAS_WIDTH, CANVAS_HEIGHT);;
    // Values for the Mandelbrot set
    private double MANDELBROT_X_MIN = -2;
    private double MANDELBROT_X_MAX = 1;
    private double MANDELBROT_Y_MIN = -1.2;
    private double MANDELBROT_Y_MAX = 1.2;
    double precision = Math.max((MANDELBROT_X_MAX - MANDELBROT_X_MIN) / CANVAS_WIDTH,
                                (MANDELBROT_Y_MAX - MANDELBROT_Y_MIN) / CANVAS_HEIGHT);;

    private Stage stage;


    @Override
    public void start(Stage primaryStage) throws IOException {
        long start = System.currentTimeMillis();
        System.out.println(
                "The canvas width: " + CANVAS_WIDTH + "\n" +
                        "The canvas height: " + CANVAS_HEIGHT
        );
        stage = primaryStage;
        new Pane();
        Pane fractalRootPane;
        FXMLLoader scene3 = new FXMLLoader(Mandelbrot.class.getResource("MandelbrotSetP.fxml"));
        fractalRootPane = scene3.load();

        canvas.setLayoutX(X_OFFSET);
        canvas.setLayoutY(Y_OFFSET);

        msTask ms = new msTask(canvas.getGraphicsContext2D(),
                            MANDELBROT_X_MIN,
                            MANDELBROT_X_MAX,
                            MANDELBROT_Y_MIN,
                            MANDELBROT_Y_MAX,
                            0, CANVAS_WIDTH/3,
                            CANVAS_WIDTH,
                            CANVAS_HEIGHT,
                             MANDELBROT_X_MIN);
        Thread a = new Thread(ms);
        a.setDaemon(true);
        a.start();

        msTask ms2 = new msTask(canvas.getGraphicsContext2D(),
                MANDELBROT_X_MIN,
                MANDELBROT_X_MAX,
                MANDELBROT_Y_MIN,
                MANDELBROT_Y_MAX,
                CANVAS_WIDTH/3, (CANVAS_WIDTH/3)*2,
                CANVAS_WIDTH,
                CANVAS_HEIGHT,
                ((CANVAS_WIDTH/3)*precision)+MANDELBROT_X_MIN);
        Thread b = new Thread(ms2);
        b.setDaemon(true);
        b.start();

        msTask ms3 = new msTask(canvas.getGraphicsContext2D(),
                MANDELBROT_X_MIN,
                MANDELBROT_X_MAX,
                MANDELBROT_Y_MIN,
                MANDELBROT_Y_MAX,
                (CANVAS_WIDTH/3)*2, CANVAS_WIDTH,
                CANVAS_WIDTH,
                CANVAS_HEIGHT,
                (((CANVAS_WIDTH/3)*2)*precision)+MANDELBROT_X_MIN);
        Thread c = new Thread(ms3);
        c.setDaemon(true);
        c.start();

        try{
            a.join();
            b.join();
            c.join();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

        /* Implementation with 1 background thread */
        /* msTask ms1 = new msTask(canvas.getGraphicsContext2D(),
                            MANDELBROT_X_MIN,
                            MANDELBROT_X_MAX,
                            MANDELBROT_Y_MIN,
                            MANDELBROT_Y_MAX,
                            0, CANVAS_WIDTH,
                            CANVAS_WIDTH,
                            CANVAS_HEIGHT,
                             MANDELBROT_X_MIN);
        new Thread(ms1).start(); */

        fractalRootPane.getChildren().add(canvas);
        Scene scene = new Scene(fractalRootPane, CANVAS_WIDTH + 2 * X_OFFSET, CANVAS_HEIGHT + 2 * Y_OFFSET);

        /* Key events (move down, left, up, right, zoomIn, zoomOut) */
        scene.setOnKeyPressed(event -> {
            /* System.out.println(event.getCode()); */
            switch (event.getCode()) {
                case UP -> {
                    System.out.println("up");
                    MANDELBROT_Y_MIN=MANDELBROT_Y_MIN+0.12;
                    MANDELBROT_Y_MAX=MANDELBROT_Y_MAX+0.12;
                }
                case DOWN -> {
                    System.out.println("down");
                    MANDELBROT_Y_MIN=MANDELBROT_Y_MIN-0.12;
                    MANDELBROT_Y_MAX=MANDELBROT_Y_MAX-0.12;
                }
                case LEFT -> {
                    System.out.println("left");
                    MANDELBROT_X_MIN=MANDELBROT_X_MIN+0.12;
                    MANDELBROT_X_MAX=MANDELBROT_X_MAX+0.12;
                }
                case RIGHT -> {
                    System.out.println("right");
                    MANDELBROT_X_MIN=MANDELBROT_X_MIN-0.12;
                    MANDELBROT_X_MAX=MANDELBROT_X_MAX-0.12;
                }
                case ADD -> {
                    System.out.println("zoomed in");
                    MANDELBROT_X_MIN=MANDELBROT_X_MIN+0.12;
                    MANDELBROT_X_MAX=MANDELBROT_X_MAX-0.12;
                    MANDELBROT_Y_MIN=MANDELBROT_Y_MIN+0.12;
                    MANDELBROT_Y_MAX=MANDELBROT_Y_MAX-0.12;
                }
                case MINUS, SUBTRACT -> {
                    System.out.println("zoomed out");
                    MANDELBROT_X_MIN=MANDELBROT_X_MIN-0.12;
                    MANDELBROT_X_MAX=MANDELBROT_X_MAX+0.12;
                    MANDELBROT_Y_MIN=MANDELBROT_Y_MIN-0.12;
                    MANDELBROT_Y_MAX=MANDELBROT_Y_MAX+0.12;
                }
                default -> {
                }
            }
            msTask keyT1 = new msTask(canvas.getGraphicsContext2D(),
                    MANDELBROT_X_MIN,
                    MANDELBROT_X_MAX,
                    MANDELBROT_Y_MIN,
                    MANDELBROT_Y_MAX,
                    0, CANVAS_WIDTH/3,
                    CANVAS_WIDTH,
                    CANVAS_HEIGHT,
                    MANDELBROT_X_MIN);
            Thread k1 = new Thread(keyT1);
            k1.setDaemon(true);
            k1.start();

            msTask keyT2 = new msTask(canvas.getGraphicsContext2D(),
                    MANDELBROT_X_MIN,
                    MANDELBROT_X_MAX,
                    MANDELBROT_Y_MIN,
                    MANDELBROT_Y_MAX,
                    CANVAS_WIDTH/3, (CANVAS_WIDTH/3)*2,
                    CANVAS_WIDTH,
                    CANVAS_HEIGHT,
                    ((CANVAS_WIDTH/3)*precision)+MANDELBROT_X_MIN);
            Thread k2 = new Thread(keyT2);
            k2.setDaemon(true);
            k2.start();

            msTask keyT3 = new msTask(canvas.getGraphicsContext2D(),
                    MANDELBROT_X_MIN,
                    MANDELBROT_X_MAX,
                    MANDELBROT_Y_MIN,
                    MANDELBROT_Y_MAX,
                    (CANVAS_WIDTH/3)*2, CANVAS_WIDTH,
                    CANVAS_WIDTH,
                    CANVAS_HEIGHT,
                    (((CANVAS_WIDTH/3)*2)*precision)+MANDELBROT_X_MIN);
            Thread k3 = new Thread(keyT3);
            k3.setDaemon(true);
            k3.start();

            try{
                k1.join();
                k2.join();
                k3.join();
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        });
        
        stage.setTitle("Mandelbrot Set Demo - parallel");
        stage.setScene(scene);
        stage.show();

        long end = System.currentTimeMillis();
        System.out.println("The execution time for the parallel part: " + (end-start) + "ms.");
    }

    public void newSetButtonP() {
        Runnable task = () -> Platform.runLater(() -> {
        System.out.println("switch");
        FXMLLoader loader = new FXMLLoader(Scene1Controller.class.getResource("HomePageS1.fxml"));
            Parent root;
            try {
                root = loader.load();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            Scene scene = new Scene(root);
        stage = (Stage) anchorPane.getScene().getWindow();
        stage.setScene(scene);
        stage.show();
        });
        Thread newS = new Thread(task);
        newS.setDaemon(true);
        newS.start();
    }

    public void exitP() {
        Runnable task = () -> Platform.runLater(() -> {
            System.out.println("exit");
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Exit");
            alert.setHeaderText("Close the window");
            alert.setContentText("Are you sure you want to exit the program?");
            Optional<ButtonType> result = alert.showAndWait();
            if (result.isPresent() && result.get() == ButtonType.OK) {
                Platform.exit();
            }
        });
        Thread exit = new Thread(task);
        exit.setDaemon(true);
        exit.start();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
