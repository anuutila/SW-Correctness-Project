module com.example.swcproject {
    requires javafx.controls;
    requires javafx.fxml;


    opens com.example.swcproject to javafx.fxml;
    exports com.example.swcproject;
}