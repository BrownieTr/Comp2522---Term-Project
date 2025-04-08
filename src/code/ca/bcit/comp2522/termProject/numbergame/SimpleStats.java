package ca.bcit.comp2522.termProject.numbergame;

/**
 * SimpleStats is a class that implements the GameStats interface.
 * It is responsible for tracking the statistics of the game, including wins, losses, and total placements.
 *
 * @author Brownie Tran
 * @version 1.0
 */
public class SimpleStats implements GameStats {

    // Constant representing the initial value for stats (used to reset values)
    private final int INITIAL_VALUE = 0;

    // Constant representing the maximum number of placements per game
    private final int MAX_PLACEMENTS_PER_GAME = 20;

    // Fields to store the number of wins, losses, and total placements across all games
    private int wins;
    private int losses;
    private int totalPlacements;

    /**
     * Constructor for SimpleStats. Initializes the statistics by calling resetStats().
     */
    public SimpleStats() {
        resetStats();
    }

    /**
     * Resets the statistics to their initial values.
     * This method is used to start tracking statistics from scratch.
     */
    public void resetStats() {
        wins = INITIAL_VALUE;
        losses = INITIAL_VALUE;
        totalPlacements = INITIAL_VALUE;
    }

    /**
     * Records a win in the game statistics.
     * Increments the win counter and adds the maximum placements for this game to the total.
     */
    @Override
    public final void recordWin() {
        wins++; // Increment win counter
        totalPlacements += MAX_PLACEMENTS_PER_GAME; // Add maximum placements for this game
    }

    /**
     * Records a loss in the game statistics.
     * Increments the loss counter and adds the successful placements to the total.
     *
     * @param successfulPlacements The number of successful placements made during the loss.
     */
    @Override
    public final void recordLoss(final int successfulPlacements) {
        losses++; // Increment loss counter
        totalPlacements += successfulPlacements; // Add the successful placements to the total
    }

    /**
     * Generates a string summarizing the game statistics.
     * This includes the number of wins, losses, and the average number of placements per game.
     *
     * @return A string containing a summary of the game statistics.
     */
    @Override
    public final String getStatistics() {
        final int totalGames; // Total number of games played
        final double avg; // Average number of placements per game
        final StringBuilder sb; // StringBuilder to construct the statistics message

        // Calculate the total number of games
        totalGames = wins + losses;

        // Calculate the average number of placements per game, ensuring no division by zero
        avg = totalGames > INITIAL_VALUE ? (double) totalPlacements / totalGames : INITIAL_VALUE;

        // Initialize StringBuilder to build the result message
        sb = new StringBuilder();

        // Add win/loss details to the message
        if (wins > 0 && losses > 0) {
            sb.append(String.format("You won %d out of %d games and lost %d out of %d games",
                    wins, totalGames, losses, totalGames));
        } else if (wins > 0) {
            sb.append(String.format("You won %d out of %d games", wins, totalGames));
        } else {
            sb.append(String.format("You lost %d out of %d games", losses, totalGames));
        }

        // Add details about total placements and average per game
        sb.append(String.format(", with %d successful placements, an average of %.2f per game",
                totalPlacements, avg));

        // Return the constructed statistics string
        return sb.toString();
    }
}
