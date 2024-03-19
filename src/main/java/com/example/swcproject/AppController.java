package com.example.swcproject;

//import com.example.swcproject.scala.Pixel;
import com.example.scala.MidpointCircle;
import com.example.scala.Pixel;
import com.example.scala.Test;
import javafx.fxml.FXML;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.ListView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.image.PixelWriter;
import javafx.scene.paint.Color;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class AppController {

    @FXML
    private TextField userInput;
    @FXML
    private TextArea commandFeed;
    @FXML
    private TextArea messageFeed;
    @FXML
    private Canvas canvas;
    @FXML
    private ListView<String> commandOptions;
    private GraphicsContext gc;
    private List<String> validCommands;

    @FXML
    private void initialize() {
        gc = canvas.getGraphicsContext2D();
        drawGrid();
        addTextFieldOptions();
        validCommands = Arrays.asList(
                "(LINE (x1 y1) (x2 y2))",
                "(RECTANGLE (x1 y1) (x2 y2))",
                "(CIRCLE (x1 y1) r)",
                "(TEXT-AT (x1 y1) t)",
                "(BOUNDING-BOX (x1 y1) (x2 y2))",
                "(DRAW c g1 g2 ... )",
                "(FILL c g)"
        );
    }

    /**
     * Draw a grid for the canvas.
     */
    private void drawGrid() {
        int canvasHeight = (int) Math.floor(canvas.getHeight());
        int canvasWidth = (int) Math.floor(canvas.getWidth());
        int GRID_SPACING = 25;
        int rows = canvasHeight / GRID_SPACING;
        int columns = canvasWidth / GRID_SPACING;

        gc.setStroke(Color.LIGHTGRAY);
        gc.setLineWidth(1);

        // Draw horizontal lines
        for (int i = 0; i < rows; i++) {
            double y = i * GRID_SPACING;
            gc.strokeLine(0, y, canvasWidth, y);
        }

        // Draw vertical lines
        for (int i = 0; i < columns; i++) {
            double x = i * GRID_SPACING;
            gc.strokeLine(x, 0, x, canvasHeight);
        }
    }

    /**
     * Provide the user with a list of the available valid graphics language commands based on the user input.
     * The user can click any of the shown commands to autofill the input field.
     */
    private void addTextFieldOptions() {
        commandOptions.setVisible(false);

        commandOptions.setOnMouseClicked(event -> {
            String selectedOption = commandOptions.getSelectionModel().getSelectedItem();
            if (selectedOption != null) {
                userInput.setText(selectedOption);
                commandOptions.setVisible(false);
            }
        });

        userInput.textProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue.isEmpty()) {
                List<String> filteredOptions = filterCommandOptions(newValue);
                if (!filteredOptions.isEmpty()) {
                    commandOptions.getItems().setAll(filteredOptions);
                    commandOptions.setMaxHeight(filteredOptions.size()*29);
                    commandOptions.setVisible(true);
                } else {
                    commandOptions.setVisible(false);
                }
            } else {
                commandOptions.setVisible(false);
            }
        });
    }

    /**
     * Filter the list of shown commands based on the user input.
     */
    private List<String> filterCommandOptions(String input) {
        List<String> filteredOptions = new ArrayList<>();
        for (String option : validCommands) {
            if (option.toLowerCase().contains(input.toLowerCase())) {
                filteredOptions.add(option);
            }
        }
        return filteredOptions;
    }

    /**
     *  Draw pixels to the canvas in the GUI.
     */
    private void drawPixels(List<Pixel> pixels) {
        PixelWriter pixelWriter = gc.getPixelWriter();
        for (Pixel pixel : pixels) {
            pixelWriter.setColor(pixel.Get_X(), pixel.Get_Y(), Color.web(pixel.Get_Color()));
        }
    }

    /**
     * Draw text on the canvas.
     */
    private void drawText(String text, int x_coord, int y_coord) {
        gc.fillText(text, x_coord, y_coord);
    }


    /**
     * Append the current user input to the command feed and clear the input field.
     */
    @FXML
    private void enterCommand() {
        String command = userInput.getText();
        commandFeed.appendText(command + "\n");


        // Test drawing a rectangle
        PixelWriter pixelWriter = gc.getPixelWriter();
        for (int i=200; i<300; i++) {
            for (int j=100; j<200; j++) {
                pixelWriter.setColor(i, j, Color.BLACK);
            }
        }

        //Test drawing text
        drawText("Testi", 50, 50);

        // TESTS
        if ("Warn".equals(userInput.getText())) {
            enterMessage(new Message("This is a test warning message", Message.MESSAGE_TYPE.WARNING));
        }
        if ("Error".equals(userInput.getText())) {
            enterMessage(new Message("This is a test error message", Message.MESSAGE_TYPE.ERROR));
        }

        // Test Scala integration
        //Test test = new Test();
        MidpointCircle test = new MidpointCircle();
        //List<Pixel> pixels = test.generatePixels();
        List<Pixel> pixels = test.midpoint_circle(1,1,11);
        drawPixels(pixels);
        enterMessage(new Message(String.format("Random rectangle was drawn between pixels: [%d, %d] and [%d, %d]", pixels.getFirst().Get_X(), pixels.getFirst().Get_Y(), pixels.getLast().Get_X(), pixels.getLast().Get_Y()), Message.MESSAGE_TYPE.INFO));

        userInput.clear();
    }

    /**
     * Send the program with all the entered commands to the Scala interpreter.
     */
    @FXML
    private void processGraphicsCommands() {
        enterMessage(new Message("Commands sent to Scala interpreter.", Message.MESSAGE_TYPE.INFO));
    }

    /**
     * Append a message to the message feed.
     */
    private void enterMessage(Message message) {
        messageFeed.appendText(message.toString() + "\n");
    }

    /**
     * Clear the drawing area and the command feed.
     */
    @FXML
    private void resetGui() {
        gc.clearRect(0, 0, canvas.getWidth(), canvas.getHeight());
        commandFeed.clear();
        clearMessageFeed();
        drawGrid();
        enterMessage(new Message("Drawing area and command feed cleared.", Message.MESSAGE_TYPE.INFO));
    }

    /**
     * Empty the message feed.
     */
    @FXML
    private void clearMessageFeed() {
        messageFeed.clear();
    }

}
