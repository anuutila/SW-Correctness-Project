module com.example.swcproject {
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.base;
    requires javafx.graphics;

    requires org.kordamp.ikonli.core;
    requires org.kordamp.ikonli.javafx;
    // add icon pack modules
    requires org.kordamp.ikonli.fontawesome5;

    requires scala.library;


    opens com.example.swcproject to javafx.fxml;
    exports com.example.swcproject;
}