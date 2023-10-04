package com.example.mandelbrotset2;

import javafx.application.Platform;
import javafx.concurrent.Task;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

public class msTask extends Task<GraphicsContext> {

    private final GraphicsContext pixel;
    private final double xMin;
    private final double xMax;
    private final double yMin;
    private final double yMax;
    private int startX, endX;
    private final int width;
    private final int height;
    private double xRef;

    public msTask(GraphicsContext pixel, double xMin, double xMax, double yMin, double yMax, int startX, int endX, int width, int height, double xRef) {
        this.pixel = pixel;
        this.xMin = xMin;
        this.xMax = xMax;
        this.yMin = yMin;
        this.yMax = yMax;
        this.startX= startX;
        this.xRef = xRef;
        this.endX =endX;
        this.width = width;
        this.height = height;
    }


    @Override
    protected GraphicsContext call() {
        long start = System.currentTimeMillis();
        Platform.runLater(() -> {
            double precision = Math.max((xMax - xMin) / width, (yMax - yMin) / height);
            System.out.println(precision);
            int maxIterations = 1000;
            for (double c = xRef, x = startX; x < endX; c = c + precision, x++) {
                for (double ci = yMin, y = 0; y < height; ci = ci + precision, y++) {
                    /*The escape time algorithm*/
                    double z = 0, zi = 0, z2 = 0, zi2 = 0;
                    double iterations = 0;
                    while (z2 + zi2 <= 4 && iterations < maxIterations) {
                        zi = 2 * z * zi + ci;
                        z = z2 - zi2 + c;
                        z2 = z * z;
                        zi2 = zi * zi;
                        iterations++;
                    }

                    /* We divide the no. of iterations with the max no. of iterations because
                     *  the rgb value can go up to 255, while we have 1000 max iterations.
                     *  NOTE: if the calculation of the pixel does not "escape", means that
                     *  iterations==maxIterations, and n/n=1, hence we will have black color for that pixel.*/
                    double i = iterations / maxIterations;
                    double c1 = Math.min(255 * 2 * i, 255);
                    double c2 = Math.max(255 * (2 * i - 1), 0);

                    if (iterations != maxIterations) {
                        pixel.setFill(Color.color(c2 / 255.0, c1 / 255.0, c2 / 255.0));
                    } else {
                        pixel.setFill(Color.DARKBLUE); /*The convergence color*/
                    }
                    pixel.fillRect(x, y, 1, 1); /*Fills a rectangle using the current fill paint.*/

                }
            }
            long end = System.currentTimeMillis();
            System.out.println("The execution time for the thread: " + (end-start) + "ms.");
        });

        return pixel;
    }
}
