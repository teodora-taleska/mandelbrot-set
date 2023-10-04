module com.example.mandelbrotset2 {
    requires javafx.controls;
    requires javafx.fxml;

    requires org.controlsfx.controls;
    requires com.dlsc.formsfx;
    requires org.kordamp.bootstrapfx.core;
    requires java.desktop;
//    requires javafx.swing;
    requires java.rmi;

    opens com.example.mandelbrotset2 to javafx.fxml;
    exports com.example.mandelbrotset2;

}