package com.example.mandelbrotset2;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;

import java.net.URL;
import java.util.ResourceBundle;

public class Scene1Controller implements Initializable {

    @FXML
    public Spinner<Integer> widthSpinner = new Spinner<>(1, 2000, 800);
    @FXML
    public Spinner<Integer> heightSpinner= new Spinner<>(1, 2000, 600);
    @FXML
    private Button startButton;

    @FXML
    private RadioButton seqButton, parButton, disButton;
    SpinnerValueFactory<Integer> valueFactory1 = widthSpinner.getValueFactory();
    SpinnerValueFactory<Integer> valueFactory2 = heightSpinner.getValueFactory();

    public void displayMandelbrot(ActionEvent event) throws Exception {
        Stage stage;
        Scene scene;
        Parent root;
        if(seqButton.isSelected()){
            System.out.println("You are running the application in sequential mode");
            Mandelbrot mandelbrot = new Mandelbrot();
            FXMLLoader scene2 = new FXMLLoader(Mandelbrot.class.getResource("MandelbrotSetS2.fxml"));
            root = scene2.load();
            stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            scene = new Scene(root, widthSpinner.getValue(), heightSpinner.getValue());
            stage.setScene(scene);
            mandelbrot.start(stage);
            stage.show();
        } else if (parButton.isSelected()){
            System.out.println("You are running the application in parallel mode");
            MandelbrotParallel manPar = new MandelbrotParallel();
            FXMLLoader scene3 = new FXMLLoader(MandelbrotParallel.class.getResource("MandelbrotSetP.fxml"));
            root = scene3.load();
            stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            scene = new Scene(root, widthSpinner.getValue(), heightSpinner.getValue());
            stage.setScene(scene);
            manPar.start(stage);
            stage.show();
        } else if (disButton.isSelected()) {
            System.out.println("You are running the application in distributed mode");
            MandelbrotDistributive manDis = new MandelbrotDistributive();
            FXMLLoader scene4 = new FXMLLoader(MandelbrotDistributive.class.getResource("MandelbrotSetD.fxml"));
            root = scene4.load();
            stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            scene = new Scene(root, widthSpinner.getValue(), heightSpinner.getValue());
            stage.setScene(scene);
            manDis.start(stage);
            stage.show();
        } else {
            System.out.println("Please select mode");
        }

    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
            /* To be able to use the spinner we have to declare a spinner value factory */
                widthSpinner.setValueFactory(valueFactory1);
                widthSpinner.setEditable(true);

                heightSpinner.setValueFactory(valueFactory2);
                heightSpinner.setEditable(true);
        }

        public int getWidth() {
            return widthSpinner.getValue();
        }
        public int getHeight () {
            return heightSpinner.getValue();
        }

    /* Mouse hover (start button) */
    public void handleMouseEnter(){
        startButton.setScaleX(1.2);
        startButton.setScaleY(1.2);
    }

    public void handleMouseExit(){
        startButton.setScaleX(1.0);
        startButton.setScaleY(1.0);
    }


}


