package com.example.mandelbrotset2;
import javafx.application.Platform;
//import javafx.embed.swing.SwingFXUtils;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.image.WritableImage;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javax.imageio.ImageIO;
import java.awt.image.RenderedImage;
import java.io.File;
import java.io.IOException;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.Optional;

public class MSImplementation extends UnicastRemoteObject implements MSInterface {

    protected MSImplementation() throws RemoteException {
        super();
    }

    @Override
    public void setPainting(GraphicsContext pixel, double xMin, double xMax, double yMin, double yMax, int width, int height) throws RemoteException {
        try{
            long start = System.currentTimeMillis();
            double precision = Math.max((xMax - xMin) / width, (yMax - yMin) / height);
            int maxIterations = 1000;
            for (double c = xMin, x = 0; x < width; c = c + precision, x++) {
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
            System.out.println("The execution time for the remote painting method: " + (end-start) + "ms.");
        } catch (Exception e) {
            System.out.println("MSImplementation: " +e.getMessage());
            e.printStackTrace();
        }
    }

    @Override
    public void newSetButton(Stage stage, AnchorPane anchorPane) throws IOException {
        System.out.println("switch");
        FXMLLoader loader = new FXMLLoader(Scene1Controller.class.getResource("HomePageS1.fxml"));
        Parent root = loader.load();
        Scene scene = new Scene(root);
        stage = (Stage) anchorPane.getScene().getWindow();
        stage.setScene(scene);
        stage.show();
    }

    @Override
    public void exit() throws RemoteException {
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
}
