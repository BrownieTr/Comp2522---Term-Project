package ca.bcit.comp2522.termProject.numbergame;

import javafx.scene.layout.GridPane;

/**
 * GameBoard is an abstract class representing the game board.
 * It contains the game grid and methods to manipulate the board state.
 * The class is intended to be extended by specific game boards,
 * which will provide implementations for placing numbers and creating the grid.
 *
 * @author Brownie Tran
 * @version 1.0
 */
public abstract class GameBoard {

    private final int INITIAL_VALUE = 0;

    // A 2D array representing the grid of the game board.
    protected int[][] grid;

    // An instance of SimpleStats used to track the game statistics.
    protected SimpleStats stats;

    /**
     * Counts how many cells in the grid are filled with non-zero values.
     * This method iterates through each cell in the grid and counts the non-zero cells.
     *
     * @return The number of non-zero cells in the grid.
     */
    protected int countFilled() {
        int count;

        count = INITIAL_VALUE;
        // Iterate through each row in the grid
        for (int[] row : grid)
        {
            // Iterate through each cell in the row
            for (int cell : row)
            {
                if (cell != INITIAL_VALUE)
                {
                    count++; // Increment count if the cell is not zero
                }
            }
        }

        return count; // Return the total count of filled cells
    }

    /**
     * Abstract method to place a number on the grid at a specific row and column.
     * This method should be implemented by subclasses to define the behavior of placing a number.
     *
     * @param row The row in which the number should be placed.
     * @param col The column in which the number should be placed.
     * @param number The number to place in the specified cell.
     */
    protected abstract void placeNumber(final int row, final int col, final int number);

    /**
     * Abstract method to create and return the graphical representation of the grid as a GridPane.
     * This method should be implemented by subclasses to define the layout of the game grid.
     *
     * @return A GridPane containing the graphical elements of the game grid.
     */
    protected abstract GridPane createGrid();
}
