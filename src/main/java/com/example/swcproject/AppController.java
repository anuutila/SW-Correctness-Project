package com.example.swcproject;

import com.example.scala.*;
import com.example.scala.Message;
import com.example.scala.MessageType.*;
import javafx.fxml.FXML;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.ListView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.image.PixelWriter;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.Text;

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
    private final int GRID_SPACING = 25;
    private int canvasHeight;
    private int canvasWidth;

    @FXML
    private void initialize() {
        gc = canvas.getGraphicsContext2D();
        canvasHeight = (int) Math.floor(canvas.getHeight());
        canvasWidth = (int) Math.floor(canvas.getWidth());
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

            // Show the value of every 5th line
            if (i % 5 == 0 && i != 0) {
                drawText(String.valueOf(i), 10, i*25-5, canvasHeight-2);
                drawText(String.valueOf(i), 10, 1, canvasHeight-i*25+4);
            }
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
    private void drawText(String text, int size, int x_coord, int y_coord) {
        gc.setFont(new Font(size));
        gc.fillText(text, x_coord, y_coord);
    }


    /**
     * Append the current user input to the command feed and clear the input field.
     */
    @FXML
    private void enterCommand() {
        String command = userInput.getText();
        commandFeed.appendText(command + "\n");

        // TESTS
        if ("Warn".equals(userInput.getText())) {
            enterMessage(new Message("This is a test warning message", MessageType.WARNING()));
        }
        if ("Error".equals(userInput.getText())) {
            enterMessage(new Message("This is a test error message", MessageType.ERROR()));
        }
        userInput.clear();
    }

    /**
     * Send the program with all the entered commands to the Scala interpreter.
     */
    @FXML
    private void processGraphicsCommands() {
        resetCanvas();
        drawGrid();
        enterMessage(new Message("Commands sent to Scala interpreter.", MessageType.INFO()));

        GraphicsLanguageInterpreter gli = new GraphicsLanguageInterpreter(
                commandFeed.getText(),
                new CanvasInformation(GRID_SPACING, canvasHeight)
        );

        List<CommandResult> response = gli.interpretProgram();
        for (CommandResult result : response) {
            enterMessage(result.message());
            drawPixels(result.pixels());
        }
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
        resetCanvas();
        commandFeed.clear();
        clearMessageFeed();
        drawGrid();
        enterMessage(new Message("Canvas and graphics language editor cleared.", MessageType.INFO()));
    }

    /**
     * Empty the canvas.
     */
    private void resetCanvas() {
        gc.clearRect(0, 0, canvas.getWidth(), canvas.getHeight());
    }

    /**
     * Empty the message feed.
     */
    @FXML
    private void clearMessageFeed() {
        messageFeed.clear();
    }

}
