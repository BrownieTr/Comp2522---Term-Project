package ca.bcit.comp2522.termProject.numbergame;

import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.Stage;
import javafx.scene.text.Font;
import java.util.*;

/**
 * NumberGame Class
 *
 * This class represents a game in which the player must place a series of numbers onto a grid in ascending order.
 * The game is played on a 4x5 grid, where each cell is initially empty. The player must place 20 numbers randomly generated
 * between 1 and 1000 onto the grid. The player must ensure that the numbers are placed in ascending order across the grid.
 * The game tracks the number of wins, losses, and successful placements. Upon completion of the game, statistics are
 * updated and presented to the player.
 *
 * @author Brownie Tran
 * @version 1.0
 */
public class NumberGame extends GameBoard
{
    // Constants define the boundaries and settings for the game
    private final int INITIAL_VALUE = 0;
    private final int MAX_ROWS = 4;
    private final int MAX_COLS = 5;
    private final int MAX_NUMBERS_PER_GAME = 20;
    private final int MIN_NUMBER_IN_GAME = 1;
    private final int MAX_NUMBER_IN_GAME = 1000;
    private final int GRID_PANE_GAP = 10;
    private final int INSETS_GAP = 20;
    private final int NUMBER_FONT_SIZE = 20;
    private final int STATS_FONT_SIZE = 16;
    private final int BUTTON_FONT_SIZE = 18;
    private final int SCENE_WIDTH = 500;
    private final int SCENE_HEIGHT = 400;
    private final int BUTTON_SIZE = 60;

    // Game-related UI components
    private final Stage gameStage;
    private final Button[][] buttons;
    private final Label numberLabel;
    private final Label statsLabel;

    // List to store randomly generated numbers for the game
    private final List<Integer> numbers;

    // 2D array representing the game grid and the placements
    private final int[][] list;
    private int currentIndex; // Index to track the current number in the game
    private int currentNumber; // The current number that needs to be placed

    // The main menu instance to navigate back to the main menu
    private final MainMenu mainMenu;

    /**
     * Constructor for NumberGame, initializes the game, UI, and statistics.
     *
     * @param mainMenu The main menu to allow navigation after the game ends.
     * @param stats The statistics object to track wins, losses, and placements.
     */
    public NumberGame(final MainMenu mainMenu, final SimpleStats stats) {
        final BorderPane root;
        final GridPane gridPane;
        final Scene scene;

        // Set up components to handle game UI
        this.mainMenu = mainMenu;
        this.stats = stats;
        this.gameStage = new Stage();
        this.buttons = new Button[MAX_ROWS][MAX_COLS];
        this.numbers = new ArrayList<>();
        this.currentIndex = INITIAL_VALUE;
        this.list = new int[MAX_ROWS][MAX_COLS];
        this.grid = new int[MAX_ROWS][MAX_COLS];

        // Initialize grid with zeros
        for (int i = 0; i < MAX_ROWS; i++) {
            Arrays.fill(list[i], INITIAL_VALUE);
            Arrays.fill(grid[i], INITIAL_VALUE);
        }

        // Create UI components and layout
        root = new BorderPane();
        root.setPadding(new Insets(INSETS_GAP));

        // Number label to display the current number to place
        numberLabel = new Label("Place this number: ");
        numberLabel.setFont(Font.font(NUMBER_FONT_SIZE));
        numberLabel.setAlignment(Pos.CENTER);
        BorderPane.setAlignment(numberLabel, Pos.CENTER);
        root.setTop(numberLabel);

        // Create the grid layout
        gridPane = createGrid();
        root.setCenter(gridPane);

        // Stats label to display current game statistics
        statsLabel = new Label("Stats: " + stats.getStatistics());
        statsLabel.setFont(Font.font(STATS_FONT_SIZE));
        statsLabel.setAlignment(Pos.CENTER);
        BorderPane.setAlignment(statsLabel, Pos.CENTER);
        root.setBottom(statsLabel);

        // Set up the scene and stage
        scene = new Scene(root, SCENE_WIDTH, SCENE_HEIGHT);
        gameStage.setScene(scene);
        gameStage.setTitle("Number Game");
        gameStage.show();

        // Initialize the list of numbers for the game and display the first number
        initialNumbers();
        showNextNumber();
    }

    /**
     * Creates a grid layout for the game board with buttons for each cell.
     *
     * @return A GridPane containing the buttons for each grid cell.
     */
    @Override
    protected final GridPane createGrid() {
        // Set up GridPane to hold buttons
        GridPane gridPane;

        gridPane = new GridPane();
        gridPane.setAlignment(Pos.CENTER);
        gridPane.setHgap(GRID_PANE_GAP);
        gridPane.setVgap(GRID_PANE_GAP);

        // Create buttons for each cell in the grid
        for (int i = 0; i < MAX_ROWS; i++) {
            for (int j = 0; j < MAX_COLS; j++) {
                int row = i, col = j;
                buttons[i][j] = new Button();
                buttons[i][j].setPrefSize(BUTTON_SIZE, BUTTON_SIZE);
                buttons[i][j].setFont(Font.font(BUTTON_FONT_SIZE));
                buttons[i][j].setOnAction(e -> handleClick(row, col)); // Handle button click
                gridPane.add(buttons[i][j], j, i); // Add the button to the grid
            }
        }
        return gridPane;
    }


    /**
     * Initializes the list of random numbers for the game.
     * Generates numbers between MIN_NUMBER_IN_GAME and MAX_NUMBER_IN_GAME.
     */
    private final void initialNumbers() {
        Random rand = new Random();
        for (int i = 0; i < MAX_NUMBERS_PER_GAME; i++) {
            numbers.add(rand.nextInt(MAX_NUMBER_IN_GAME) + MIN_NUMBER_IN_GAME); // Generate a random number
        }
    }

    /**
     * Handles the click event for placing a number on the grid.
     * Checks if the placement is valid and whether the grid remains sorted.
     *
     * @param row The row index where the number was clicked.
     * @param col The column index where the number was clicked.
     */
    private final void handleClick(final int row, final int col) {
        if (grid[row][col] != INITIAL_VALUE) {
            return; // Do nothing if the cell is already filled
        }

        // Place the number and check if the grid remains sorted
        list[row][col] = currentNumber;
        if (!isSorted(list)) {
            endGame(false); // End game if grid is not sorted
            return;
        }

        // Place the number on the grid and update button text
        placeNumber(row, col, currentNumber);
        buttons[row][col].setText(String.valueOf(currentNumber));

        // If all numbers are placed, end the game; otherwise, show next number
        if (++currentIndex >= MAX_NUMBERS_PER_GAME) {
            endGame(true); // Game ends with a win
        } else {
            showNextNumber(); // Show the next number to be placed
        }
    }

    /**
     * Checks if the grid is sorted in ascending order (ignores empty cells).
     *
     * @param grid The grid to be checked for ascending order.
     * @return True if the grid is sorted; false otherwise.
     */
    private boolean isSorted(final int[][] grid) {
        int last = INITIAL_VALUE;
        for (int[] row : grid) {
            for (int value : row) {
                if (value == INITIAL_VALUE) continue; // Ignore empty cells
                if (value < last) return false; // Check if the order is maintained
                last = value; // Update last value for comparison
            }
        }
        return true;
    }

    /**
     * Updates the number to be placed and changes the display label accordingly.
     */
    private void showNextNumber() {
        currentNumber = numbers.get(currentIndex); // Get the next number
        numberLabel.setText("Place this number: " + currentNumber); // Update the label
    }

    /**
     * Ends the game, records the result (win or loss), and updates statistics.
     * Displays an alert with the option to play again or quit to the main menu.
     *
     * @param won True if the game was won; false otherwise.
     */
    private final void endGame(final boolean won) {
        if (won) {
            stats.recordWin(); // Record a win
        } else {
            stats.recordLoss(countFilled()); // Record a loss with the count of placements
        }

        statsLabel.setText("Stats: " + stats.getStatistics()); // Update the stats label
        gameStage.close(); // Close the game window

        // Display a pop-up alert with the game result
        Platform.runLater(() -> {
            Alert alert;
            ButtonType playAgain;
            ButtonType quit;
            Optional<ButtonType> result;

            alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle(won ? "You Won!" : "Game Over!");
            alert.setHeaderText(null);
            alert.setContentText(stats.getStatistics());

            playAgain = new ButtonType("Play Again");
            quit = new ButtonType("Quit to Menu");
            alert.getButtonTypes().setAll(playAgain, quit);

            result = alert.showAndWait();
            if (result.isPresent() && result.get() == playAgain) {
                // Start a new game if "Play Again" is selected
                new NumberGame(mainMenu, stats);
            } else {
                // Go back to the main menu if "Quit to Menu" is selected
                mainMenu.showMenu();
            }
        });
    }

    /**
     * Places a number on the grid at the specified location.
     *
     * @param row The row where the number should be placed.
     * @param col The column where the number should be placed.
     * @param number The number to place in the specified cell.
     */
    @Override
    protected final void placeNumber(final int row, final int col, final int number) {
        grid[row][col] = number; // Place the number on the grid
    }
}
