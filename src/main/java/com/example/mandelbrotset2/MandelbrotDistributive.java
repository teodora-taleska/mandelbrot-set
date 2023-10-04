package com.example.mandelbrotset2;

import javafx.application.Application;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

import java.io.IOException;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

public class MandelbrotDistributive extends Application {
    public AnchorPane anchorPane;
    private static MSImplementation msImpl;

    static {
        try {
            msImpl = new MSImplementation();
        } catch (RemoteException e) {
            throw new RuntimeException(e);
        }
    }

    private static final int X_OFFSET = 25;
    private static final int Y_OFFSET = 25;
    @FXML
    Canvas canvas;
    private double MANDELBROT_X_MIN = -2;
    private double MANDELBROT_X_MAX = 1;
    private double MANDELBROT_Y_MIN = -1.2;
    private double MANDELBROT_Y_MAX = 1.2;

    private Stage stage;
    Scene1Controller scene1 = new Scene1Controller();
    private final int CANVAS_WIDTH = scene1.getWidth() - 60;
    private final int CANVAS_HEIGHT = scene1.getHeight() - 50;

    @Override
    public void start(Stage primaryStage) throws Exception {

        long start = System.currentTimeMillis();
        System.out.println(
                "The canvas width: " + CANVAS_WIDTH + "\n" +
                        "The canvas height: " + CANVAS_HEIGHT
        );
        stage = primaryStage;

        new Pane();
        Pane fractalRootPane;
        FXMLLoader scene2 = new FXMLLoader(Mandelbrot.class.getResource("MandelbrotSetS2.fxml"));
        fractalRootPane = scene2.load();

        canvas = new Canvas(CANVAS_WIDTH, CANVAS_HEIGHT);
        canvas.setLayoutX(X_OFFSET);
        canvas.setLayoutY(Y_OFFSET);

        /* Invoking the method for painting the mandelbrot set*/
        msImpl.setPainting(canvas.getGraphicsContext2D(),
                MANDELBROT_X_MIN,
                MANDELBROT_X_MAX,
                MANDELBROT_Y_MIN,
                MANDELBROT_Y_MAX,
                CANVAS_WIDTH, CANVAS_HEIGHT);

        fractalRootPane.getChildren().add(canvas);

        Scene scene = new Scene(fractalRootPane, CANVAS_WIDTH + 2 * X_OFFSET, CANVAS_HEIGHT + 2 * Y_OFFSET);

        /* Key events (move down, left, up, right, zoomIn, zoomOut) */
        scene.setOnKeyPressed(event -> {
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
            try {
                msImpl.setPainting(canvas.getGraphicsContext2D(),
                        MANDELBROT_X_MIN,
                        MANDELBROT_X_MAX,
                        MANDELBROT_Y_MIN,
                        MANDELBROT_Y_MAX,
                        CANVAS_WIDTH, CANVAS_HEIGHT);
            } catch (RemoteException e) {
                throw new RuntimeException(e);
            }
        });

        stage.setTitle("Mandelbrot Set Demo - distributive");
        stage.setScene(scene);
        stage.show();

        long end = System.currentTimeMillis();
        System.out.println("The execution time for the distributive part: " + (end-start)/1000 + "s.");
    }

    public void exitD() throws RemoteException {
        msImpl.exit();
    }

    public void newSetButtonD() throws IOException {
        msImpl.newSetButton(stage, anchorPane);
    }

    public static void main(String[] args) {
        try {
            Registry registry = LocateRegistry.getRegistry("127.0.0.1", 1099);
            msImpl = (MSImplementation) registry.lookup("ms");
            launch(args);
        } catch (RemoteException | NotBoundException e) {
            throw new RuntimeException(e);
        }
    }
}
