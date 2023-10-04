package com.example.mandelbrotset2;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

import java.io.IOException;
import java.rmi.Remote;
import java.rmi.RemoteException;

public interface MSInterface extends Remote{
    /*Defining the API*/
    void setPainting(GraphicsContext pixel, double xMin, double xMax, double yMin, double yMax, int width, int height) throws RemoteException;
    void newSetButton(Stage stage, AnchorPane anchorPane) throws IOException;
    void exit() throws RemoteException;
}
