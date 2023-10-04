package com.example.mandelbrotset2;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.stage.Stage;
import java.util.Optional;


public class Main extends Application {

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage stage) {
        try{
            FXMLLoader fxmlLoader = new FXMLLoader(Scene1Controller.class.getResource("HomePageS1.fxml"));
            Parent root =  fxmlLoader.load();
            Scene scene1 = new Scene(root, 800, 600);
            stage.setTitle("Mandelbrot set");
            stage.setScene(scene1);
            stage.setResizable(false);
            stage.show();
            stage.centerOnScreen();

            stage.setOnCloseRequest(event -> {
                /* to prevent our program to close the program
                 when we click on the x button */
                event.consume();
                exit(stage);
            });

        }catch (Exception e){
            System.out.println("Something went wrong!");
        }
    }

    public void exit(Stage stage){
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
