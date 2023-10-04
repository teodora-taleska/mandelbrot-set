package com.example.mandelbrotset2;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import java.io.IOException;
import java.util.Optional;


public class Mandelbrot extends Application{
    Scene1Controller scene1 = new Scene1Controller();
    @FXML
    private AnchorPane anchorPane;
    // Size of the canvas for the Mandelbrot set
    private final int CANVAS_WIDTH = scene1.getWidth() - 60;
    private final int CANVAS_HEIGHT = scene1.getHeight() - 50;
    // Left and right border
    private static final int X_OFFSET = 25;
    // Top and Bottom border
    private static final int Y_OFFSET = 25;
    /*used to create space between the border and the canvas */
    @FXML
    Canvas canvas;
    // Values for the Mandelbrot set
    private double MANDELBROT_X_MIN = -2;
    private double MANDELBROT_X_MAX = 1;
    private double MANDELBROT_Y_MIN = -1.2;
    private double MANDELBROT_Y_MAX = 1.2;

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
        FXMLLoader scene2 = new FXMLLoader(Mandelbrot.class.getResource("MandelbrotSetS2.fxml"));
        fractalRootPane = scene2.load();

        canvas = new Canvas(CANVAS_WIDTH, CANVAS_HEIGHT);
        canvas.setLayoutX(X_OFFSET);
        canvas.setLayoutY(Y_OFFSET);

        paintSet(canvas.getGraphicsContext2D(),
                MANDELBROT_X_MIN,
                MANDELBROT_X_MAX,
                MANDELBROT_Y_MIN,
                MANDELBROT_Y_MAX);

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
            paintSet(canvas.getGraphicsContext2D(),
                    MANDELBROT_X_MIN,
                    MANDELBROT_X_MAX,
                    MANDELBROT_Y_MIN,
                    MANDELBROT_Y_MAX);
        });


        stage.setTitle("Mandelbrot Set Demo - sequential");
        stage.setScene(scene);
        stage.show();

        long end = System.currentTimeMillis();
        System.out.println("The execution time for the sequential part: " + (end-start) + "ms.");
    }

    private void paintSet(GraphicsContext pixel, double xMin, double xMax, double yMin, double yMax) {
        double precision = Math.max((xMax - xMin) / CANVAS_WIDTH, (yMax - yMin) / CANVAS_HEIGHT);
        int maxIterations = 1000;
        for (double c = xMin, x = 0; x < CANVAS_WIDTH; c = c + precision, x++) {
            for (double ci = yMin, y = 0; y < CANVAS_HEIGHT; ci = ci + precision, y++) {
                double iterations = checkNoOFIterations(ci, c, maxIterations);
                double i = iterations / maxIterations;
                double c1 = Math.min(255 * 2 * i, 255);
                double c2 = Math.max(255 * (2 * i - 1), 0);

                if (iterations != maxIterations) {
                    pixel.setFill(Color.color(c2 / 255.0, c1 / 255.0, c2 / 255.0));
                } else {
                    pixel.setFill(Color.DARKBLUE); /*The convergence color*/
                }
                pixel.fillRect(x, y, 1, 1); //Fills a rectangle using the current fill paint.
            }
        }

    }

    /*
     * Checks the convergence of a coordinate (c, ci) with the help of the escape time algorithm.
     * The convergence factor determines the color of the point.
     */
    private int checkNoOFIterations(double ci, double c, int maxIterations) {
        double z = 0, zi=0, z2=0, zi2=0;
        int iteration = 0;
        while (z2+zi2<=4 && iteration < maxIterations ){
            zi = 2*z*zi + ci;
            z = z2 - zi2 + c;
            z2 = z*z;
            zi2 = zi*zi;
            iteration++;
        }
        return iteration;
        /* If the number of iterations does not escape the limit then iteration = maxIterations */
    }

    /* Function for going "back" on the home scene */
    public void newSetButton() throws IOException {
        System.out.println("switch");
        FXMLLoader loader = new FXMLLoader(Scene1Controller.class.getResource("HomePageS1.fxml"));
        Parent root = loader.load();
        Scene scene = new Scene(root);
        stage = (Stage) anchorPane.getScene().getWindow();
        stage.setScene(scene);
        stage.show();
    }

    /* Function for exiting the application */
    public void exit(){
        System.out.println("exit");
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Exit");
        alert.setHeaderText("Close the window");
        alert.setContentText("Are you sure you want to exit the program?");
        Optional<ButtonType> result = alert.showAndWait();
        if (result.isPresent() && result.get() == ButtonType.OK){
            Platform.exit();
        }
    }

    public static void main(String[] args) {
        launch(args);
    }

}