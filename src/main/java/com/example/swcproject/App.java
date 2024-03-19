package com.example.swcproject;

// import com.example.swcproject.scala.Pixel;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class App extends Application {
    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(App.class.getResource("/fxml/main-view.fxml"));
        Scene scene = new Scene(fxmlLoader.load());
        stage.setTitle("SWC Project");
        stage.setScene(scene);
        stage.setResizable(false);
        //stage.setMaximized(true);
        //stage.setMaxHeight(1080);
        //stage.setMaxWidth(1920);
        stage.show();

       // ArrayList<Pixel> lol = new ArrayList<Pixel>();

    }

    public static void main(String[] args) {
        launch();
    }
}
