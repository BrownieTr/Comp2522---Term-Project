package ca.bcit.comp2522.termProject.numbergame;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

/**
 * MainMenu class represents the initial screen of the Number Placement Game.
 * It contains options to start a new game or quit the game.
 *
 * @author Brownie Tran
 * @version 1.0
 */
public class MainMenu extends Application
{

    private final int BOX_SIZE = 20;
    private final int INSETS_GAP = 20;
    private final int TITLE_FONT_SIZE = 24;
    private final int QUIT_BUTTON_FONT_SIZE = 16;
    private final int QUIT_BUTTON_MIN_WIDTH = 150;
    private final int SCENE_WIDTH = 400;
    private final int SCENE_HEIGHT = 300;

    private final Stage primaryStage;  // The main stage (window) of the application
    private final SimpleStats stats;   // Object to track and display game statistics

    /**
     * Initializes the primary stage and sets up the game statistics.
     *
     * @param primaryStage The primary stage for the application
     */
    public MainMenu(final Stage primaryStage)
    {
        this.primaryStage = primaryStage;// Assign the primary stage to the class variable
        this.stats = new SimpleStats(); // Initialize the statistics object
    }

    /**
     * Initializes the primary stage and sets up the game statistics.
     * Displays the main menu to the user.
     *
     * @param primaryStage The primary stage for the application
     */
    @Override
    public void start(final Stage primaryStage)
    {
        new MainMenu(primaryStage);
        showMenu();  // Display the main menu
    }

    /**
     * Sets up the main menu UI, including the title, "Start New Game" button, and "Quit" button.
     * The user can choose to start a new game or quit from this menu.
     */
    public final void showMenu()
    {
        final VBox layout;
        final Label titleLabel;
        final Button startButton;
        final Button quitButton;
        final Scene scene;

        // Create a vertical box layout for the menu
        layout = new VBox(BOX_SIZE);
        layout.setAlignment(Pos.CENTER);  // Center align the content
        layout.setPadding(new Insets(INSETS_GAP));  // Add padding around the layout

        // Title label for the game
        titleLabel = new Label("Number Placement Game");
        titleLabel.setStyle("-fx-font-size: " + TITLE_FONT_SIZE + "px; -fx-font-weight: bold;");  // Style for the title

        // Start button to begin a new game
        startButton = new Button("Start New Game");
        startButton.setStyle("-fx-font-size:" + QUIT_BUTTON_FONT_SIZE + "px; -fx-min-width: "
                + QUIT_BUTTON_MIN_WIDTH + "px;");  // Style for the button
        startButton.setOnAction(e -> {
            primaryStage.hide();  // Hide the main menu window
            new NumberGame(this, stats);  // Start a new number game
        });

        // Quit button to exit the game and show final statistics
        quitButton = new Button("Quit");
        quitButton.setStyle("-fx-font-size:" + QUIT_BUTTON_FONT_SIZE + "px; -fx-min-width:"
                + QUIT_BUTTON_MIN_WIDTH + "px;");  // Style for the button
        quitButton.setOnAction(e -> {
            showFinalStats();  // Show the final statistics alert
            primaryStage.close();  // Close the main menu window
        });

        // Add the title label and buttons to the layout
        layout.getChildren().addAll(titleLabel, startButton, quitButton);

        // Set up the scene with the layout and configure the primary stage
        scene = new Scene(layout, SCENE_WIDTH, SCENE_HEIGHT);  // Set the scene size
        primaryStage.setScene(scene);  // Set the scene on the primary stage
        primaryStage.setTitle("Number Game - Main Menu");  // Set the window title
        primaryStage.show();  // Display the main menu window
    }

    /**
     * Displays an alert with the final game statistics and resets the stats for the next game.
     */
    private final void showFinalStats()
    {
        // Create an information alert to show the statistics
        Alert alert;
        alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Game Statistics");  // Set the alert title
        alert.setHeaderText("Final Game Statistics");  // Set the header text
        alert.setContentText(stats.getStatistics());  // Set the content text with the statistics
        alert.showAndWait();  // Show the alert and wait for the user to close it
        stats.resetStats();  // Reset the statistics for the next game
    }

    /**
     * Hides the main menu window if the primary stage is not null.
     */
    public final void hide()
    {
        if (primaryStage != null)
        {
            primaryStage.hide();  // Hide the primary stage
        }
    }

    /**
     * The main entry point of the application. Launches the JavaFX application.
     *
     * @param args Command-line arguments
     */
    public static void main(final String[] args)
    {
        launch(args);  // Launch the JavaFX application
    }
}
