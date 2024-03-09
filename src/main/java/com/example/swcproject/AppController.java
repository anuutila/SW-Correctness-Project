package com.example.swcproject;

import javafx.fxml.FXML;
import javafx.scene.canvas.Canvas;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.image.PixelWriter;
import javafx.scene.paint.Color;

public class AppController {

    @FXML
    private TextField userInput;

    @FXML
    private TextArea commandFeed;

    @FXML
    private Canvas canvas;

    @FXML
    private void enterCommand() {
        String command = userInput.getText();
        commandFeed.appendText(command + "\n");
        userInput.clear();

        PixelWriter pixelWriter = canvas.getGraphicsContext2D().getPixelWriter();
        // Test drawing a rectangle
        for (int i=200; i<300; i++) {
            for (int j=100; j<200; j++) {
                pixelWriter.setColor(i, j, Color.BLACK);
            }
        }

    }

}
